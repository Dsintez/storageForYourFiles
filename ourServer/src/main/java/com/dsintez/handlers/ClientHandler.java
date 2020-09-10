package com.dsintez.handlers;

import com.dsintez.ECommand;
import com.dsintez.ThreadHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ThreadHandler threadHandler;
    private final static String prePath = "Files/server";

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            threadHandler = new ThreadHandler(dataInputStream, dataOutputStream);
            boolean isRunning = true;
            while (isRunning) {
                //TODO 09/09/2020 Authentification

                int command = dataInputStream.read();
                System.out.println("Step 1 " + command);
                switch (command) {
                    case 10:
                        threadHandler.setPath(Paths.get(prePath)).setCommand(ECommand.RECEIVE).run();
                        break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
