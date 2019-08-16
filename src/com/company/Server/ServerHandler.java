package com.company.Server;

import com.company.util.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {

    private static final int DEFAULT_NUMBER_THREADS = 10;
    private static final int MAX_FILE_SIZE = 1022386;

    Map<String, ArrayList<String>> messages = Collections.synchronizedSortedMap(new TreeMap<>());
    ExecutorService executor;

    public ServerHandler(){
        this(DEFAULT_NUMBER_THREADS);
    }

    public ServerHandler(int nThreads){
        loadMessages();
        executor = Executors.newFixedThreadPool(nThreads);
    }

    private void saveMessages(){
        StringBuilder save = new StringBuilder();
        messages.forEach((k,v) -> {
            save.append(k);
            save.append("\n");
            v.forEach((ms) -> save.append(' ' + ms +'\n'));
        });
        try (FileWriter fw = new FileWriter("messages.txt")) {
            fw.write(save.toString());
            fw.flush();
        } catch (IOException e) {
            System.out.println("lacking /resources/messages.txt");
        }
    }

    private void loadMessages(){
        try {
            String line;
            Scanner fr = new Scanner(new File("messages.txt"));
            String cur_owner = "";
            while (fr.hasNext()){
                line = fr.nextLine();
                if (!line.startsWith(" ")){
                    cur_owner = line;
                    if(!messages.containsKey(cur_owner)) messages.put(cur_owner, new ArrayList<>());
                }
                else {
                    if(messages.containsKey(cur_owner)) messages.get(cur_owner).add(line);
                }
            }
            System.out.println("Loaded data:");
            System.out.println(messages.toString());
        } catch (Exception e) {
            System.out.println("No data found. Creating an empty database");
        }
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
            Runtime.getRuntime().addShutdownHook(new Thread(this::saveMessages));
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("Primary module launched");
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

    void fileTransfer(int port){
        try (ServerSocket server = new ServerSocket(port)){
            System.out.println("File module launched");
            while (!server.isClosed()) {
                try {
                    Socket client = server.accept();
                    byte[] bytearray = new byte[MAX_FILE_SIZE];
                    InputStream is = client.getInputStream();
                    is.read(bytearray);
                    String content = new String(bytearray);
                    System.out.println(content);
                    client.close();
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error during reading");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Secondary module crash");
        }
    }
}
