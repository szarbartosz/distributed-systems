package devices.wihajster;

import Devices.*;

import java.util.HashMap;
import java.util.Map;

public class WihajsterUtils {
    public static Map<String, String> getDeviceData(Bulbulator bulbulator)
    {
        Map<String, String> data = new HashMap<>();
        data.put("name", bulbulator.name);
        data.put("serialNumber", bulbulator.serialNumber);
        data.put("deviceType", bulbulator.deviceType);
        data.put("iterationsNumber", String.valueOf(bulbulator.iterationsNumber));
        return data;
    }

    public static void changeSettings(Bulbulator bulbulator, Map<String, String> settings)
            throws UnknownSettingException
    {
        for (Map.Entry<String, String> entry: settings.entrySet())
        {
            switch(entry.getKey())
            {
                case "iterationsNumber":
                    bulbulator.iterationsNumber = Short.parseShort(entry.getValue());
                    break;
                default:
                    String reason = "Setting \"" + entry.getKey() + "\" is not known or cannot be set for " +
                            bulbulator.deviceType;
                    throw new UnknownSettingException(reason);
            }
        }
    }
}
