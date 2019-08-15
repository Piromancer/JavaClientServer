package com.company.Server;


import java.io.DataOutputStream;
import java.io.IOException;
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
        if(ms.getText().split(" ").length == 2 && (new Scanner(ms.getText().split(" ")[1]).hasNextInt())) pos = Integer.parseInt(ms.getText().split(" ")[1]);
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
                if (pos - 1 > messages.size() || pos < 0){
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
            case CHANGE_LOGIN: {
                try {
                    dos.writeUTF("You successfully left your account");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            default: {
                if(reservedMsg.trim().startsWith(":")){
                    try {
                        dos.writeUTF("No such command");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return fullmap;
                }
                messages.add(reservedMsg);
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
