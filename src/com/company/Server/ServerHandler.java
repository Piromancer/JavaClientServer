package com.company.Server;

import com.company.util.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {

    private static final int DEFAULT_NUMBER_THREADS = 10;

    Map<String, ArrayList<String>> messages = new TreeMap<>();
    ExecutorService executor;

    public ServerHandler(){
        this(DEFAULT_NUMBER_THREADS);
    }

    public ServerHandler(int nThreads){
        executor = Executors.newFixedThreadPool(nThreads);
    }

    private void serveClient(Socket client) throws IOException, InterruptedException{
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        DataInputStream in = new DataInputStream(client.getInputStream());
        while (!client.isClosed()) {
            System.out.println("Login attempt");
            String entry;
            try {
                entry = in.readUTF();
            } catch (Exception e){
                client.close();
                System.out.println("Client disconnected");
                return;
            }
            if (entry.equalsIgnoreCase(":quit")) {
                System.out.println("Client initialize connections suicide ...");
                out.flush();
                break;
            }
            if (entry.equalsIgnoreCase(":changeLogin")) continue;
            if (Verifier.validLogin(entry)) {
                out.writeUTF(entry + " logged in");
                if(!messages.containsKey(entry)) messages.put(entry, new ArrayList<>());
            } else {
                out.writeUTF("Failed to login");
                continue;
            }
            String nextMsg = "";
            CommandHandler ch = new CommandHandler(messages, entry);
            do{
                try{
                    nextMsg = in.readUTF();
                } catch (Exception e){
                    client.close();
                    System.out.println("Client disconnected");
                    return;
                }
                try {
                    Message parsed_msg = JSONParser.fromJson(nextMsg);
                    messages = ch.resolve(parsed_msg, out);
                    if (parsed_msg.getCommand() == Command.CHANGE_LOGIN) nextMsg = ":changeLogin";
                    if (parsed_msg.getCommand() == Command.QUIT) nextMsg = ":quit";
                } catch (ParseException e) {
                    out.writeUTF("A message caused a parsing error");
                }
            } while(!(nextMsg.trim().equals(":changeLogin") || nextMsg.trim().equals(":quit")));
            if (nextMsg.trim().equalsIgnoreCase(":quit")) {
                System.out.println("Client initialize connections suicide ...");
                out.flush();
                client.close();
                return;
            }
        }
    }

    public void serve(int port) throws InterruptedException{

            try (ServerSocket server = new ServerSocket(port)) {
                while (!server.isClosed()) {
                    Socket client = server.accept();
                    System.out.print("Connection accepted.");
                    executor.execute(() -> {
                        try {
                            serveClient(client);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
                executor.shutdown();
            } catch (IOException e) {
                System.out.println("IO troubles");
            }
        }
}
