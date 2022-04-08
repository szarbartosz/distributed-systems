import socket
import sys
from threading import Thread
from image import image
import struct

working = True

MULTICAST_PORT = 8081
MULTICAST_GROUP = '224.1.1.1'


def receiver_tcp():
    while working:
        message = str(tcp_client.recv(1024), 'utf-8')
        print(message)
    tcp_client.close()


def sender():
    global working
    while working:
        message = input()
        if message == '/quit':
            working = False
            print("Closing...")
            tcp_client.send(bytes(message, 'utf-8'))
            udp_client.sendto(bytes(message, 'utf-8'), (IP_ADDRESS, PORT))
            multicast_sender.sendto(bytes(message, 'utf8'), (MULTICAST_GROUP, MULTICAST_PORT))
        elif message == 'U':
            udp_client.sendto(bytes(image, 'utf-8'), (IP_ADDRESS, PORT))
        elif message == "M":
            multicast_sender.sendto(bytes(image, 'utf8'), (MULTICAST_GROUP, MULTICAST_PORT))
        else:
            tcp_client.send(bytes(message, 'utf-8'))


def receiver_udp():
    while working:
        buff, address = udp_client.recvfrom(1024)
        print(str(buff, 'utf-8'))


def receiver_multicast():
    while working:
        buff, address = multicast_receiver.recvfrom(1024)
        if str(buff, 'utf-8').strip() != '/quit':
            print(str(buff, 'utf-8'))


if len(sys.argv) == 3:
    IP_ADDRESS = str(sys.argv[1])
    PORT = int(sys.argv[2])
else:
    IP_ADDRESS = "127.0.0.1"
    PORT = 8080

question = "Podaj swój nick: "
nick = input(question)

tcp_client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcp_client.connect((IP_ADDRESS, PORT))
tcp_client.send(bytes(f'[{nick}]', 'utf-8'))

udp_client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
udp_client.sendto(bytes("Hello", 'utf-8'), (IP_ADDRESS, PORT))

multicast_sender = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
multicast_sender.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)

multicast_receiver = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
multicast_receiver.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
multicast_receiver.bind(('', MULTICAST_PORT))
mreq = struct.pack("4sl", socket.inet_aton(MULTICAST_GROUP), socket.INADDR_ANY)
multicast_receiver.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

Thread(target=sender).start()
Thread(target=receiver_tcp).start()
Thread(target=receiver_udp).start()
Thread(target=receiver_multicast).start()

while working:
    pass
