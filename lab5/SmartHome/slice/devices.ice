
#ifndef DEVICES_ICE
#define DEVICES_ICE

module Devices
{
  enum CoffeeType
  {
    ESPRESSO, DOPPIO, AMERICANO, CORTADO, CAPPUCCINO, LATTE
  };

  sequence<CoffeeType> supportedCoffeeTypes;

  struct CoffeOrder 
  {
    CoffeeType coffeeType;
    short temperature;
    short volume;
  };

  struct Coffee
  {
    CoffeeType coffeeType;
    short temperature;
    short volume;
  };

  exception ValueOutOfRangeException
  { string reason; };

  exception UnknownSettingException
  { string reason; };

  exception UnsupportedCoffeeTypeException
  { string reason; };

  dictionary<string, string> DeviceData;
  dictionary<string, string> NewSettings;
    
  class Device
  {
    string name;
    string deviceType;
    string serialNumber;
  };

  interface IDevice
  {
    DeviceData getDeviceData();
    idempotent void changeSettings(NewSettings settings) throws UnknownSettingException, ValueOutOfRangeException;
  };

  class CoffeeMachine extends Device
  {
    short temperature;
    short minTemperature;
    short maxTemperature;

    short volume;
    short minVolume;
    short maxVolume;

    supportedCoffeeTypes supportedTypes;
  };

  interface ICoffeeMachine extends IDevice
  {
    Coffee prepareCoffee(CoffeeType coffeeType) throws UnsupportedCoffeeTypeException;
  };

  class Bulbulator extends Device
  {
    short iterationsNumber;
  };

  interface IBulbulator extends IDevice
  {
    string bulbulbul();
  };
};

#endif
