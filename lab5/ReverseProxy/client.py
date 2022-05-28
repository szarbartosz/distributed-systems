# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""The Python implementation of the GRPC helloworld.Greeter client."""

from __future__ import print_function

import logging

import grpc

import helloworld_pb2_grpc
import helloworld_pb2
import weather_pb2
import weather_pb2_grpc
from utils.cities import city_names


def run():
    # NOTE(gRPC Python Team): .close() is possible on a channel and should be
    # used in circumstances in which the with statement does not fit the needs
    # of the code.
    with grpc.insecure_channel('localhost:50050') as channel:
        print("ACTIONS:")
        print("-say hello [H]")
        print("-get weather [W]")
        print("-get weather from multiple random cities [R]")
        while True:
            print("\nSelect action:", end="")
            name = input("\n> ")
            if not name or name == "exit":
                print("Goodbye!")
                exit(0)
            if name.upper() == "H":
                stub = helloworld_pb2_grpc.GreeterStub(channel)
                response = stub.sayHello(helloworld_pb2.HelloRequest(name='you'))
                print("Greeter client received: " + response.message)
                response = stub.sayHelloAgain(helloworld_pb2.HelloRequest(name='you'))
                print("Greeter client received: " + response.message)
                continue
            if name.upper() == "W":
                location = input("Enter the location to check the weather: ")
                stub = weather_pb2_grpc.DataProviderStub(channel)
                response = stub.getWeather(weather_pb2.WeatherRequest(location=location))
                print(response.message)
                continue
            if name.upper() == "R":
                locations = city_names
                stub = weather_pb2_grpc.DataProviderStub(channel)
                for location in locations:
                    response = stub.getWeather(weather_pb2.WeatherRequest(location=location))
                    print(response.message)
                continue
            else:
                print("unknown action")
                continue


if __name__ == '__main__':
    logging.basicConfig()
    run()
