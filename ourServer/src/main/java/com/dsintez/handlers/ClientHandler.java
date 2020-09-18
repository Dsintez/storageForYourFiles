package com.dsintez.handlers;

import com.dsintez.ECommand;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private CommandHandler commandHandler;
    private final Path prePath;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        prePath = Paths.get("Files/server");
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            commandHandler = new CommandHandler(dataInputStream, dataOutputStream);
            boolean isRunning = true;
            while (isRunning) {
                //TODO 09/09/2020 Authentification

                int command = dataInputStream.read();
                switch (command) {
                    case 10:
                        commandHandler
                                .setPath(prePath)
                                .setCommand(ECommand.SEND).run();
                        break;
                    case 11:
                        commandHandler
                                .setPath(prePath)
                                .setCommand(ECommand.RECEIVE).run();
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
