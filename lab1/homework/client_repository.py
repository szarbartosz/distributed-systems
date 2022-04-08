import threading


class ClientRepository:
    def __init__(self):
        self.connected_clients = []
        self.lock = threading.Lock()

    def add_client(self, client):
        with self.lock:
            self.connected_clients.append(client)

    def remove_client(self, client):
        if client in self.connected_clients:
            with self.lock:
                self.connected_clients.remove(client)

    def broadcast(self, sender, message):
        for client in self.connected_clients:
            if client != sender:
                client.connection.send(bytes(f"{sender.nick}: {message}", 'utf-8'))
