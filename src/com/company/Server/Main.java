package com.company.Server;

public class Main {
    final static int PORT1 = 45777;
    final static int PORT2 = 45778;

    public static void main(String[] args) throws InterruptedException {
        ServerHandler server = new ServerHandler();
        Runtime.getRuntime().addShutdownHook(new Thread(server::saveMessages));
        new Thread(() -> server.fileTransfer(PORT2)).start();
        server.serve(45777);
        server.saveMessages();
    }
}
