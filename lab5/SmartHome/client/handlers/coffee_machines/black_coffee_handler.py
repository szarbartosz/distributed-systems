import Ice

from Devices import *
# from devices_ice import UnsupportedCoffeeTypeException, CoffeeType, ICoffeeMachinePrx
from handlers.utils import get_device_data, change_settings, test_connection

class BlackCoffeeMakerHandler:
    def __init__(self, name, communicator):
        self.name = name
        self._proxy = None
        self.communicator = communicator
        self.device_type = "coffeeMachines"
        self.allowed_actions = {"getDeviceData",
                                "changeSettings",
                                "prepareCoffee"}

        self.settings = ["temperature", "volume"]

        self.coffee_types = ["ESPRESSO",
                             "DOPPIO",
                             "AMERICANO"]

        self.coffee_types_map = {
            "ESPRESSO": CoffeeType.ESPRESSO,
            "DOPPIO": CoffeeType.DOPPIO,
            "AMERICANO": CoffeeType.AMERICANO}

    @property
    def proxy(self):
        if not self._proxy:
            proxy = self.communicator.propertyToProxy(self.name)
            self._proxy = ICoffeeMachinePrx.checkedCast(proxy)

        return self._proxy

    def print_allowed_actions(self):
        print()
        print("Allowed actions:")
        print("- getDeviceData")
        print("- changeSettings")
        print("- prepareCoffee", end="\n")

    def handle_action(self, action):
        if action == "getDeviceData":
            get_device_data(self)
        elif action == "changeSettings":
            change_settings(self)
        elif action == "prepareCoffee":
            print("Enter coffee type, one of:", self.coffee_types)
            coffee_type = input("> ")

            try:
                coffee_type = self.coffee_types_map[coffee_type.upper()]
            except KeyError:
                print("Error: provided coffee type was not recognized.")
                return

            try:
                test_connection(self)
                coffee = self.proxy.prepareCoffee(coffee_type)
            except UnsupportedCoffeeTypeException as e:
                print("Error:", e.reason)
                print()
                return
            except Ice.ObjectNotExistException:
                print("Error: servant object appropriate for this action was "
                      "not found on the server.")
                return

            print("Coffee ready: ", coffee.coffeeType.name.lower(), ", ",
                  "temperature: ", coffee.temperature, " deg. C, ",
                  "volume: ", coffee.volume, " ml", sep="")
