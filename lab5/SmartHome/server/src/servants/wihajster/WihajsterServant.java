package servants.wihajster;

import Devices.IBulbulator;
import Devices.UnknownSettingException;
import Devices.ValueOutOfRangeException;
import com.zeroc.Ice.Current;
import devices.wihajster.Wihajster;

import java.util.Map;

public class WihajsterServant implements IBulbulator {
    private String name;
    private static Wihajster instance;

    public WihajsterServant(String name)
    {
        super();
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String bulbulbul(Current current) {
        if (instance == null) {
            instance = new Wihajster(this.name);
        }

        return instance.bulbulbul(current);
    }

    @Override
    public Map<String, String> getDeviceData(Current current) {
        if (instance == null) {
            instance = new Wihajster(this.name);
        }

        return instance.getDeviceData(current);
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnknownSettingException, ValueOutOfRangeException {
        if (instance == null) {
            instance = new Wihajster(this.name);
        }

        instance.changeSettings(settings, current);
    }
}
