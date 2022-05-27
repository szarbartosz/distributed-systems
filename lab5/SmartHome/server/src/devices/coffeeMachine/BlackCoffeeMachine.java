package devices.coffeeMachine;

import Devices.*;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.InputStream;
import com.zeroc.Ice.OutputStream;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class BlackCoffeeMachine extends CoffeeMachine implements ICoffeeMachine {
    private final ReentrantLock lock;

    public BlackCoffeeMachine(String name)
    {
        this.name = name;
        this.serialNumber = UUID.randomUUID().toString();
        this.deviceType = this.getClass().getSimpleName();

        this.temperature = 80;
        this.minTemperature = 60;
        this.maxTemperature = 95;

        this.volume = 100;
        this.minVolume = 40;
        this.maxVolume = 300;

        this.supportedTypes = new CoffeeType[]{
                CoffeeType.ESPRESSO,
                CoffeeType.DOPPIO,
                CoffeeType.AMERICANO};

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
