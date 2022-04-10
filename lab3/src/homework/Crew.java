package homework;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Crew {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final static String PRODUCT_EXCHANGE = "products";
    private final static String MESSAGE_EXCHANGE = "messages";

    public static void main(String[] argv) throws Exception {
        System.out.println("Podaj nazwę ekipy: ");
        String crewName = br.readLine();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(MESSAGE_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(crewName + "_queue", false, false, false, null);
        channel.queueBind(crewName + "_queue", MESSAGE_EXCHANGE, crewName);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(crewName + "_queue", false, consumer);
        channel.basicConsume("admin_queue", false, consumer);

        while (true) {
            System.out.println("Zamów zasób: ");
            String key = br.readLine();
            if (key.equals("exit")) {
                break;
            }
            channel.basicPublish(PRODUCT_EXCHANGE, key, null, (crewName + " " + key).getBytes(StandardCharsets.UTF_8));
            Thread.sleep(200L);
        }
    }
}
