package edu.tseidler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Client {
    private final Socket client;
    private final PrintWriter clientWriter;
    private final Scanner clientInput;

    private Client(Socket client) throws IOException {
        this.client = client;
        clientWriter = new PrintWriter(client.getOutputStream());
        clientInput = new Scanner(client.getInputStream());
    }

    static Client of(Socket client) throws IOException {
        return new Client(client);
    }

    void writeMessage(String message) {
        clientWriter.println(message);
        clientWriter.flush();
    }

    public boolean hasNewMessage() {
        return clientInput.hasNextLine();
    }

    public String getNextMessage() {
        return clientInput.nextLine();
    }

    public void disconnect() throws IOException {
        client.close();
    }
}
