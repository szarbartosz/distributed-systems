import Ice

from handlers.wihajsters.wihajster_handler import WihajsterHandler
from handlers.coffee_machines.black_coffee_handler import BlackCoffeeMakerHandler
from handlers.coffee_machines.milk_coffee_handler import MilkCoffeeMakerHandler

config_file = "config.client"

def get_proxies(communicator):
    devices = dict()
    with open(config_file) as file:
        for line in file:
            if line == "\n":
                continue
            if line.startswith("# END DEVICE DEFINITIONS"):
                break

            name = line.split("=", 1)[0]

            if "wihajster" in name:
                device = WihajsterHandler(name, communicator)
            elif "blackCoffeeMachine" in name:
                device = BlackCoffeeMakerHandler(name, communicator)
            elif "milkCoffeeMachine" in name:
                device = MilkCoffeeMakerHandler(name, communicator)
            else:
                raise ValueError("Device", name, " not recognized.")

            devices[name] = device
    return devices


if __name__ == "__main__":
    with Ice.initialize(config_file) as communicator:
        # don't create proxies yet - they'll be created lazily with first
        # communication attempt, this way server can lazily create servants
        devices = get_proxies(communicator)
        if not devices:
            print("No proxies found for provided file.")
            exit(1)

        print("Client ready, entering processing loop.")

        print("Available devices:")
        for device_name in devices:
            print("-", device_name)

        print("\nTo use device, enter it's name first. Empty input exits to "
              "main menu (unless written otherwise).")

        while True:
            print("\nSelect device:", end="")
            name = input("\n> ")
            if not name or name == "exit":
                print("Goodbye!")
                exit(0)
            if name not in devices:
                print("Unknown device.")
                continue

            device = devices[name]
            device.print_allowed_actions()

            action = input("\n> ")
            if not action:
                continue
            if action not in device.allowed_actions:
                print("Illegal action for ", device.name, ".", sep="")
                print()
                continue

            try:
                device.handle_action(action)
            except Ice.EndpointParseException:
                print("Incorrect port for device ", device.name,
                      ", removing it from available devices.", sep="")
                del devices[name]
                print()
                print("Available devices:")
                for device_name in devices:
                    print(device_name)
                print()
