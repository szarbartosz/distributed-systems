package devices.coffeeMachine;

import Devices.*;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.InputStream;
import com.zeroc.Ice.OutputStream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class MilkCoffeeMachine extends CoffeeMachine implements ICoffeeMachine {
    private final ReentrantLock lock;

    public MilkCoffeeMachine(String name)
    {
        this.name = name;
        this.serialNumber = UUID.randomUUID().toString();
        this.deviceType = this.getClass().getSimpleName();

        this.temperature = 75;
        this.minTemperature = 60;
        this.maxTemperature = 90;

        this.volume = 100;
        this.minVolume = 100;
        this.maxVolume = 400;

        this.supportedTypes = new CoffeeType[]{
                CoffeeType.CORTADO,
                CoffeeType.CAPPUCCINO,
                CoffeeType.LATTE};

        this.lock = new ReentrantLock();
    }

    @Override
    public Coffee prepareCoffee(CoffeeType coffeeType, Current current) throws UnsupportedCoffeeTypeException {
        this.lock.lock();
        CoffeeMachineUtils.checkCoffeeType(coffeeType, this.supportedTypes, this.deviceType);
        Coffee coffee = new Coffee(coffeeType, this.temperature, this.volume);
        this.lock.unlock();
        return coffee;
    }

    @Override
    public Map<String, String> getDeviceData(Current current) {
        this.lock.lock();
        Map<String, String> data = CoffeeMachineUtils.getDeviceData(this);
        this.lock.unlock();
        return data;
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnknownSettingException, ValueOutOfRangeException {
        this.lock.lock();
        CoffeeMachineUtils.changeSettings(this, settings);
        this.lock.unlock();
    }

    @Override
    public void _iceWriteImpl(OutputStream ostr) {
        ICoffeeMachine.super._iceWriteImpl(ostr);
    }

    @Override
    public void _iceReadImpl(InputStream istr) {
        ICoffeeMachine.super._iceReadImpl(istr);
    }
}
