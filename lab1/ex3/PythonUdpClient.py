import socket;

serverIP = '127.0.0.1'
serverPort = 9008
msg_bytes = (300).to_bytes(4, byteorder='little')

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg_bytes, (serverIP, serverPort))

data, addr = client.recvfrom(1024)
data = int.from_bytes(data, byteorder='big')
print(f'received message: {data}')
