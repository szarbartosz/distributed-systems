package homework;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final static String PRODUCT_EXCHANGE = "products";
    private final static String MESSAGE_EXCHANGE = "messages";
    private final static List<String> products = new ArrayList<>();

    public static void main(String[] argv) throws Exception {
        System.out.println("Podaj nazwę dostawcy: ");
        String supplierName = br.readLine();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        while (true) {
            System.out.println("Nazwa sprzętu: ");
            String product = br.readLine();
            if (product.equals(";;")) {
                break;
            }
            products.add(product);
        }

        channel.exchangeDeclare(PRODUCT_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.basicQos(1);

        for (String product : products){
            channel.queueDeclare(product + "_queue", false, false, false, null);
            channel.queueBind(product + "_queue", PRODUCT_EXCHANGE, product);
        }

        channel.exchangeDeclare(MESSAGE_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(supplierName + "_queue", false, false, false, null);
        channel.queueBind(supplierName + "_queue", MESSAGE_EXCHANGE, supplierName);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String crewName = message.substring(0, message.indexOf(" "));
                String resourceType = message.substring(message.indexOf(" ") + 1);
                System.out.println("Otrzymano nowe zamówienie na \033[34m" + resourceType + "\033[0m od \033[34m" + crewName + "\033[0m");
                String responseMessage = "Zamówienie na: \033[34m" + resourceType + "\033[0m zostało zrealizowane przez \033[34m" + supplierName + "\033[0m";
                channel.basicPublish(MESSAGE_EXCHANGE, crewName, null, responseMessage.getBytes());
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        for (String product : products){
            channel.basicConsume(product + "_queue", false, consumer);
        }
        channel.basicConsume(supplierName + "_queue", false, consumer);
        System.out.println("Oczekiwanie na zamówienia...");
    }
}
