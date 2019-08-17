package com.company.Client;

import java.io.*;
import java.net.Socket;

public class FileTransferer {
    private static final int MAX_FILE_SIZE = 1022386;

    public static void transferFile(int port, File f){
        try(Socket socket = new Socket("localhost", port);
            FileInputStream fin = new FileInputStream(f);
            OutputStream os = socket.getOutputStream();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            ) {
            byte[] bytearray = new byte[(int) f.length()];
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray, 0, bytearray.length);
            os.write(bytearray, 0, bytearray.length);
            os.flush();
        } catch (Exception e) {
            System.out.println("Not able to transfer file");
        }

    }
}
