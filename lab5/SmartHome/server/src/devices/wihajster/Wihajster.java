package devices.wihajster;

import Devices.Bulbulator;
import Devices.IBulbulator;
import Devices.UnknownSettingException;
import Devices.ValueOutOfRangeException;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.InputStream;
import com.zeroc.Ice.OutputStream;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Wihajster extends Bulbulator implements IBulbulator {
    private final ReentrantLock lock;

    public Wihajster(String name)
    {
        this.name = name;
        this.serialNumber = UUID.randomUUID().toString();
        this.deviceType = this.getClass().getSimpleName();
        this.iterationsNumber = 3;

        this.lock = new ReentrantLock();
    }

    @Override
    public String bulbulbul(Current current) {
        this.lock.lock();
        String bulbulbul = "bul ".repeat(Math.max(0, this.iterationsNumber));
        this.lock.unlock();
        return bulbulbul;
    }

    @Override
    public Map<String, String> getDeviceData(Current current) {
        this.lock.lock();
        Map<String, String> data = WihajsterUtils.getDeviceData(this);
        this.lock.unlock();
        return data;
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnknownSettingException, ValueOutOfRangeException {
        this.lock.lock();
        WihajsterUtils.changeSettings(this, settings);
        this.lock.unlock();
    }

    @Override
    public void _iceWriteImpl(OutputStream ostr) {
        IBulbulator.super._iceWriteImpl(ostr);
    }

    @Override
    public void _iceReadImpl(InputStream istr) {
        IBulbulator.super._iceReadImpl(istr);
    }
}
