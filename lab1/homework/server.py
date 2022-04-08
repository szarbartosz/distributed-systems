import sys
import socket
from connection import Connection
from client_repository import ClientRepository
from udp_server import UdpServer

if len(sys.argv) == 3:
    IP_ADDRESS = str(sys.argv[1])
    PORT = int(sys.argv[2])
else:
    IP_ADDRESS = '127.0.0.1'
    PORT = 8080

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((IP_ADDRESS, PORT))
server.listen(100)
client_repository = ClientRepository()

UdpServer(IP_ADDRESS, PORT).start()

while True:
    connection, address = server.accept()
    thread = Connection(connection, address, client_repository)
    client_repository.add_client(thread)
    thread.start()
