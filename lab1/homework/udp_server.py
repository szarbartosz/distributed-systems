import socket
from threading import Thread


class UdpServer(Thread):
    def __init__(self, address, port):
        Thread.__init__(self)
        self.server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.server.bind((address, port))
        self.udp_clients = []

    def run(self):
        while True:
            buff, address = self.server.recvfrom(1024)
            message = str(buff, 'utf-8')
            if message.strip() == "Hello":
                self.udp_clients.append(address)
                continue
            if message.strip() == "/quit":
                self.server.sendto(bytes("", 'utf-8'), address)
                self.udp_clients.remove(address)
                continue
            for client_address in self.udp_clients:
                if client_address == address:
                    continue
                self.server.sendto(buff, client_address)
