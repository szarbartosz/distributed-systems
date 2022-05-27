package servants.coffeeMachine;

import Devices.*;
import com.zeroc.Ice.Current;
import devices.coffeeMachine.MilkCoffeeMachine;

import java.util.Map;

public class MilkCoffeeMachineServant implements ICoffeeMachine {
    private String name;
    private static MilkCoffeeMachine instance;

    public MilkCoffeeMachineServant(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Coffee prepareCoffee(CoffeeType coffeeType, Current current) throws UnsupportedCoffeeTypeException {
        if (instance == null)
            instance = new MilkCoffeeMachine(this.name);

        return instance.prepareCoffee(coffeeType, current);
    }

    @Override
    public Map<String, String> getDeviceData(Current current) {
        if(instance == null) {
            instance = new MilkCoffeeMachine(this.name);
        }

        return instance.getDeviceData(current);
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnknownSettingException, ValueOutOfRangeException {
        if (instance == null)
            instance = new MilkCoffeeMachine(this.name);

        instance.changeSettings(settings, current);
    }
}
