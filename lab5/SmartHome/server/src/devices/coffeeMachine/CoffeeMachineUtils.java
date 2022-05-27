package devices.coffeeMachine;

import Devices.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeMachineUtils {
    public static void checkCoffeeType(CoffeeType coffeeType, CoffeeType[] allowedTypes, String deviceType)
            throws UnsupportedCoffeeTypeException {
        if (!Arrays.asList(allowedTypes).contains(coffeeType)) {
            String reason = "Coffee type " + coffeeType.name() + "is not supported by a " + deviceType;
            throw new UnsupportedCoffeeTypeException(reason);
        }
    }

    public static void checkTemperature(short newTemperature, short minTemperature, short maxTemperature,
                                        String deviceType) throws ValueOutOfRangeException {
        if (newTemperature < minTemperature || newTemperature > maxTemperature) {
            String reason = "Device " + deviceType + " can't have temperature set to " + newTemperature +
                    ", valid range is [" + minTemperature + ", " + maxTemperature + "]";
            throw new ValueOutOfRangeException(reason);
        }
    }

    public static void checkVolume(short newVolume, short minVolume, short maxVolume, String deviceType)
            throws ValueOutOfRangeException {
        if (newVolume < minVolume || newVolume > maxVolume) {
            String reason = "Device " + deviceType + " can't have volume set to " + newVolume +
                    ", valid range is [" + minVolume + ", " + maxVolume + "]";
            throw new ValueOutOfRangeException(reason);
        }
    }

    public static Map<String, String> getDeviceData(CoffeeMachine coffeeMachine)
    {
        Map<String, String> data = new HashMap<>();
        data.put("name", coffeeMachine.name);
        data.put("serialNumber", coffeeMachine.serialNumber);
        data.put("deviceType", coffeeMachine.deviceType);

        data.put("temperature", String.valueOf(coffeeMachine.temperature));
        data.put("minTemperature", String.valueOf(coffeeMachine.minTemperature));
        data.put("maxTemperature", String.valueOf(coffeeMachine.maxTemperature));

        data.put("volume", String.valueOf(coffeeMachine.volume));
        data.put("minVolume", String.valueOf(coffeeMachine.minVolume));
        data.put("maxVolume", String.valueOf(coffeeMachine.maxVolume));

        data.put("allowedTypes", Arrays.toString(coffeeMachine.supportedTypes));
        return data;
    }

    public static void changeSettings(CoffeeMachine coffeeMachine, Map<String, String> settings)
            throws ValueOutOfRangeException, UnknownSettingException {
        for (Map.Entry<String, String> entry: settings.entrySet()) {
            switch(entry.getKey()) {
                case "temperature":
                    short newTemperature = Short.parseShort(entry.getValue());
                    checkTemperature(newTemperature, coffeeMachine.minTemperature,
                            coffeeMachine.maxTemperature, coffeeMachine.deviceType);
                    coffeeMachine.temperature = newTemperature;
                case "volume":
                    short newVolume = Short.parseShort(entry.getValue());
                    checkVolume(newVolume, coffeeMachine.minVolume, coffeeMachine.maxVolume,
                            coffeeMachine.deviceType);
                    coffeeMachine.volume = newVolume;
                default:
                    String reason = "Setting \"" + entry.getKey() + "\" is not known or cannot be set for " +
                            coffeeMachine.deviceType;
                    throw new UnknownSettingException(reason);
            }
        }
    }
}
