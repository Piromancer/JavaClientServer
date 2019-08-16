package com.company.Server;


import com.company.Client.FileTransferer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CommandHandler {
    private Map<String, ArrayList<String>> fullmap;
    private ArrayList<String> messages;
    private String name;
    public CommandHandler(Map<String, ArrayList<String>> msg, String name){
        fullmap = msg; messages = msg.get(name); this.name = name;
    }

    public Map<String, ArrayList<String>> resolve(Message ms, DataOutputStream dos){
        int pos = -1;
        String file_to_trans = "";
        if(ms.getText().split(" ").length == 2 && (new Scanner(ms.getText().split(" ")[1]).hasNextInt())) pos = Integer.parseInt(ms.getText().split(" ")[1]);
        else if(ms.getText().split(" ").length == 2 && ms.getText().startsWith(":transferFile")) {file_to_trans = ms.getText().split(" ")[1];}
        String reservedMsg = ms.getText();
        Command command = ms.getCommand();
        switch (command){
            case SHOW: {
                StringBuilder output = new StringBuilder(); int i = 0;
                for (String message : messages)
                    output.append(i++ + ": " +message+"\n");
                try {
                    dos.writeUTF(output.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fullmap;
            }
            case DELETE:{
                if (pos >= messages.size() || pos < 0){
                    try {
                        dos.writeUTF("No such id");
                        return fullmap;
                    } catch (IOException e) {
                        System.out.println("Connection not found");
                    }
                }
                messages.remove(pos);
                fullmap.put(name, messages);
                try {
                    dos.writeUTF("Deleted message with id = "+ pos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fullmap;
            }
            case CHANGE_LOGIN: {
                try {
                    dos.writeUTF("You successfully left your account");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fullmap;
            }
            case TRANSFER_FILE: {
                try {
                    dos.writeUTF("File " + file_to_trans + " has been added to your storage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messages.add(file_to_trans + " " + ms.getCreationTime());
                fullmap.put(name, messages);
                return fullmap;
            }
            case QUIT:{
                try {
                    dos.writeUTF("Disconnection requested");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fullmap;
            }
            case HELP:{
                StringBuilder sb = new StringBuilder();
                sb.append("You can use commands by typing :{commandName} [argument]\n");
                sb.append(":show gives you the history of your messages and files sent\n");
                sb.append(":delete [arg] lets you remove an entry from your history. Argument is an ID of your message, which you can find in :show command\n");
                sb.append(":changeLogin lets you leave your account and enter another one\n");
                sb.append(":transferFile lets you load a file to a server. File name will be stored in your messages list\n");
                sb.append(":quit lets you quit the application\n");
                sb.append("Messages can't start with an ':' symbol. Any other messages will be stored in our database.\n");
                try{
                    dos.writeUTF(sb.toString());
                } catch (IOException e){
                    System.out.println("Couldn't receive connection");
                }
                return fullmap;
            }
            default: {
                if(reservedMsg.trim().startsWith(":")){
                    try {
                        dos.writeUTF("No such command");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return fullmap;
                }
                messages.add(reservedMsg + " " + ms.getCreationTime());
                try {
                    dos.writeUTF("Message sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fullmap.put(name, messages);
                return fullmap;
            }
        }
    }
}
