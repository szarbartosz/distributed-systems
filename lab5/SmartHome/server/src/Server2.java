import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import servants.coffeeMachine.BlackCoffeeMachineServant;
import servants.coffeeMachine.MilkCoffeeMachineServant;
import servants.wihajster.WihajsterServant;

public class Server2 {
    public void run(String[] args)
    {
        int status = 0;
        Communicator communicator = null;

        try	{
            // 1. Inicjalizacja ICE - utworzenie communicatora
            communicator = Util.initialize(args);

            // 2. Konfiguracja adaptera
            // METODA 1 (polecana produkcyjnie): Konfiguracja adaptera Adapter1 jest w pliku konfiguracyjnym podanym jako parametr uruchomienia serwera
            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter2");

            // METODA 2 (niepolecana, dopuszczalna testowo): Konfiguracja adaptera Adapter jest w kodzie źródłowym
            //ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "tcp -h 127.0.0.2 -p 10000");
            //ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "tcp -h 127.0.0.2 -p 10000 : udp -h 127.0.0.2 -p 10000");
//            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

            // 3. Stworzenie serwanta/serwantów
            WihajsterServant wihajsterServant = new WihajsterServant("wihajster");

            BlackCoffeeMachineServant blackCoffeeMachineServant = new BlackCoffeeMachineServant("blackCoffeeMachine");
            MilkCoffeeMachineServant milkCoffeeMachineServant = new MilkCoffeeMachineServant("milkCoffeeMachine");

            // 4. Dodanie wpisów do tablicy ASM, skojarzenie nazwy obiektu (Identity) z serwantem
            adapter.add(wihajsterServant, new Identity("wihajster2", "wihajsters"));
            adapter.add(blackCoffeeMachineServant, new Identity("blackCoffeeMachine2", "coffeeMachines"));
            adapter.add(milkCoffeeMachineServant, new Identity("milkCoffeeMachine2", "coffeeMachines"));

            // 5. Aktywacja adaptera i wejście w pętlę przetwarzania
            adapter.activate();

            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();

        }
        catch (Exception e) {
            System.err.println(e);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            }
            catch (Exception e) {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);
    }

    public static void main(String[] args)
    {
        Server2 app = new Server2();
        app.run(args);
    }
}
