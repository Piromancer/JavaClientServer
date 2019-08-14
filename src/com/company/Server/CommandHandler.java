package com.company.Server;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class CommandHandler {
    private TreeMap<String, ArrayList<String>> fullmap;
    private ArrayList<String> messages;
    private String name;
    public CommandHandler(TreeMap<String, ArrayList<String>> msg, String name){
        fullmap = msg; messages = msg.get(name); this.name = name;
    }

    public TreeMap<String, ArrayList<String>> resolve(String command, DataOutputStream dos){
        int pos = -1;
        if(command.split(" ").length == 2 && (new Scanner(command.split(" ")[1]).hasNextInt())) pos = Integer.parseInt(command.split(" ")[1]);
        String reservedMsg = command;
        command = command.trim().split(" ")[0];
        switch (command){
            case ":show": {
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
            case ":delete":{
                if (pos - 1 > messages.size()){
                    try {
                        dos.writeUTF("No such id");
                        return fullmap;
                    } catch (IOException e) {
                        e.printStackTrace();
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
            case ":changeLogin": {
                try {
                    dos.writeUTF("You successfully left your account");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fullmap;
            }
            default: {
                if(command.startsWith(":")){
                    try {
                        dos.writeUTF("No such command");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return fullmap;
                }
                messages.add(reservedMsg);
                try {
                    dos.writeUTF("message sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fullmap.put(name, messages);
                return fullmap;
            }
        }
    }
}
