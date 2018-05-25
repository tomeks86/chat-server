package edu.tseidler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class Server {
    private final ServerSocket serverSocket;
    private final List<Client> clients;
    private static final Logger logger = Logger.getAnonymousLogger();

    public Server(int port) throws IOException {
        logger.info("attempt to start server");
        serverSocket = new ServerSocket(port);
        logger.info("server up and running...");
        clients = new CopyOnWriteArrayList<>();
        acceptClients();
    }

    private void acceptClients() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            Client client = Client.of(socket);
            logger.info("registration of user: " + (clients.size() + 1));
            registerClient(client);
            new Thread(() -> handleClients(client)).start();
        }
    }

    private void registerClient(Client client) {
        clients.add(client);
        client.writeMessage("hello from server");
    }

    private void handleClients(Client client) {
        String command = "";
        while (!"quit".equalsIgnoreCase(command)) {
            if (client.hasNewMessage()) {
                command = client.getNextMessage();
                emmitMessageToAll(command);
            }
        }
        try {
            disconnect(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void emmitMessageToAll(String message) {
        clients.forEach(c -> c.writeMessage(message));
    }

    private void disconnect(Client client) throws IOException {
        clients.remove(client);
        client.disconnect();
        logger.info("client disconnected, remaining: " + clients.size());
    }
}
