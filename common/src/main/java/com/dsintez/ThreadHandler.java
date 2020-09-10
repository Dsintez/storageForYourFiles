package com.dsintez;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThreadHandler implements Runnable{

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

    private void receive() {
        try {
            byte[] buffer = new byte[dataInputStream.read()];
            dataInputStream.read(buffer);
            String fileName = new String(buffer);
            path = Paths.get(path.toString(), fileName);
            path.toFile().createNewFile();
            long fileSize = dataInputStream.readLong();
            try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())){
                buffer = new byte[256];
                int sizeBuffer;
                while (fileSize > 0) {
                    sizeBuffer = dataInputStream.read(buffer);
                    fileOutputStream.write(buffer, 0, sizeBuffer);
                    fileSize =- sizeBuffer;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send() {
        try {
            if (!path.toFile().isFile()) return;
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            dataOutputStream.write(command.getCommandCode());
            dataOutputStream.write(path.getFileName().toFile().getName().getBytes().length);
            dataOutputStream.write(path.getFileName().toFile().getName().getBytes());
            dataOutputStream.writeLong(path.toFile().length());
            byte[] buffer = new byte[256];
            int bufferSize;
            while (fileInputStream.available() > 0) {
                bufferSize = fileInputStream.read(buffer);
                dataOutputStream.write(buffer, 0, bufferSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
