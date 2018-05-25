package edu.tseidler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(8189);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
