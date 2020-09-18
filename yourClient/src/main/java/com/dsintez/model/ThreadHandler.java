package com.dsintez.model;

import com.dsintez.ECommand;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThreadHandler implements Runnable{

    public static final int BUFFER_SIZE = 256;
    private Path path;
    private ECommand command;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void run() {
        switch (command) {
            case SEND:
                send();
                break;
            case RECEIVE:
                receive();
                break;
        }
    }

    private void send() {
        try {
            if (!path.toFile().isFile()) return;
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            dataOutputStream.write(command.getCommandCode());
            sendFileName();
            dataOutputStream.writeLong(path.toFile().length());
            byte[] buffer = new byte[BUFFER_SIZE];
            int bufferSize;
            while (fileInputStream.available() > 0) {
                bufferSize = fileInputStream.read(buffer);
                dataOutputStream.write(buffer, 0, bufferSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        try {
            dataOutputStream.write(command.getCommandCode());
            sendFileName();
            int cod = dataInputStream.read();
            switch (cod){
                case 21:
                    System.out.println(getMessage());
                    return;
                case 20:
                    path.toFile().createNewFile();
                    long fileSize = dataInputStream.readLong();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())){
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int sizeBuffer;
                        while (fileSize > 0) {
                            sizeBuffer = dataInputStream.read(buffer);
                            fileOutputStream.write(buffer, 0, sizeBuffer);
                            fileSize =- sizeBuffer;
                        }
                    }
                    break;
                default:
                    //TODO LOG cod
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMessage() throws IOException {
        byte[] buffer = new byte[dataInputStream.read()];
        dataInputStream.read(buffer);
        return new String(buffer);
    }

    private void sendFileName() throws IOException {
        dataOutputStream.write(path.getFileName().toFile().getName().getBytes().length);
        dataOutputStream.write(path.getFileName().toFile().getName().getBytes());
    }

    public ThreadHandler(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public ThreadHandler setPath(Path path) {
        this.path = path;
        return this;
    }

    public ThreadHandler setCommand(ECommand command) {
        this.command = command;
        return this;
    }
}
