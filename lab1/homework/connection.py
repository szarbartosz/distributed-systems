from threading import Thread


class Connection(Thread):
    def __init__(self, connection, address, client_repository):
        Thread.__init__(self)
        self.connection = connection
        self.address = address
        self.client_repository = client_repository
        self.nick = None

    def run(self):
        self.nick = str(self.connection.recv(1024), 'utf-8').strip()

        while True:
            message = str(self.connection.recv(1024), 'utf-8')
            if message.strip() == "/quit":
                self.connection.send(bytes("Closed", 'utf-8'))
                self.client_repository.remove_client(self)
                return
            self.client_repository.broadcast(self, message)
