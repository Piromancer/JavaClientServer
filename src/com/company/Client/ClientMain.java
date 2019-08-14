package com.company.Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        String clientName = "";

        try (Socket socket = new Socket("localhost", 45777);
            Scanner console_in = new Scanner(System.in);
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream()); )
        {

            System.out.println("You connected to Server.");
            System.out.println();

            while(!socket.isOutputShutdown()){
                    if(clientName.isEmpty()) {
                        System.out.println("Please enter your login. It should only contain lower case latin letters and have 4 symbols at most");
                        clientName = console_in.nextLine();
                    }
                    oos.writeUTF(clientName);
                    oos.flush();
                    if(clientName.equalsIgnoreCase(":quit")){
                        System.out.println("Killing connection...");
                        oos.close();
                        ois.close();
                        break;
                    }
                    if(clientName.equalsIgnoreCase(":changeLogin")) {clientName = ""; continue;}
                    String loginAnswer = ois.readUTF();
                    System.out.println(loginAnswer);
                    if(loginAnswer.equals("Failed to login")) continue;
                    String command = "";
                    do{
                        command = console_in.nextLine();
                        oos.writeUTF(command);
                        System.out.println(ois.readUTF());
                    } while(!(command.trim().equals(":changeLogin") || command.trim().equals(":quit")));
                    if(command.equalsIgnoreCase(":quit")){
                        System.out.println("Killing connection...");
                        oos.close();
                        ois.close();
                        break;
                    } else clientName = "";

            }
            System.out.println("Goodbye!");

        } catch (UnknownHostException e) {
            System.out.println("Failed to connect");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Input or output troubles");
            e.printStackTrace();
        }
    }
}
