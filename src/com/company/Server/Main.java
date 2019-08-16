package com.company.Server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {
    final static int PORT1 = 45777;
    final static int PORT2 = 45778;

    public static void main(String[] args) throws InterruptedException {
        ServerHandler server = new ServerHandler();
        new Thread(() -> server.fileTransfer(PORT2)).start();
        server.serve(45777);

//        TreeMap<String, ArrayList<String>> messages = new TreeMap<>();
//
//        while (true) {
//
//            try (ServerSocket server = new ServerSocket(45777)) {
//                Socket client = server.accept();
//
//                System.out.print("Connection accepted.");
//                DataOutputStream out = new DataOutputStream(client.getOutputStream());
//                DataInputStream in = new DataInputStream(client.getInputStream());
//
//                while (!client.isClosed()) {
//                    System.out.println("Login attempt");
//                    String entry = in.readUTF();
//                    if (entry.equalsIgnoreCase(":quit")) {
//                        System.out.println("Client initialize connections suicide ...");
//                        out.flush();
//                        Thread.sleep(3000);
//                        break;
//                    }
//                    if (entry.equalsIgnoreCase(":changeLogin")) continue;
//                    if (Verifier.validLogin(entry)) {
//                        out.writeUTF(entry + " logged in");
//                        if(!messages.containsKey(entry)) messages.put(entry, new ArrayList<>());
//                    } else {
//                        out.writeUTF("Failed to login");
//                        continue;
//                    }
//                    String nextMsg = "";
//                    CommandHandler ch = new CommandHandler(messages, entry);
//                    do{
//                        nextMsg = in.readUTF();
//                        messages = ch.resolve(nextMsg, out);
//                    } while(!(nextMsg.trim().equals(":changeLogin") || nextMsg.trim().equals(":quit")));
//                    if (entry.equalsIgnoreCase(":quit")) {
//                        System.out.println("Client initialize connections suicide ...");
//                        out.flush();
//                        Thread.sleep(3000);
//                        break;
//                    }
//
//                }
//
//                System.out.println("Client disconnected");
//                System.out.println("Closing connections & channels.");
//                in.close();
//                out.close();
//                client.close();
//                System.out.println("Closing connections & channels - DONE.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }
}
