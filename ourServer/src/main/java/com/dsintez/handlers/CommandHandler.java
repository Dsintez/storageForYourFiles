package com.dsintez.handlers;

import com.dsintez.ECommand;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandHandler{

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
            getFileName();
            byte[] buffer;
            path.toFile().createNewFile();
            long fileSize = dataInputStream.readLong();
            try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())){
                buffer = new byte[BUFFER_SIZE];
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

    private void receive(){
        try {
            getFileName();
            if (!path.toFile().isFile()) {
                dataOutputStream.write(ECommand.ERROR.getCommandCode());
                String error = "Файл не найден!";
                dataOutputStream.write(error.getBytes().length);
                dataOutputStream.write(error.getBytes());
            } else {
                FileInputStream fileInputStream = new FileInputStream(path.toFile());
                dataOutputStream.write(ECommand.OK.getCommandCode());
                dataOutputStream.writeLong(path.toFile().length());
                byte[] buffer = new byte[BUFFER_SIZE];
                int bufferSize;
                while (fileInputStream.available() > 0) {
                    bufferSize = fileInputStream.read(buffer);
                    dataOutputStream.write(buffer, 0, bufferSize);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFileName() throws IOException {
        byte[] buffer = new byte[dataInputStream.read()];
        dataInputStream.read(buffer);
        String fileName = new String(buffer);
        path = Paths.get(path.toString(), fileName);
    }

    public CommandHandler(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public CommandHandler setPath(Path path) {
        this.path = path;
        return this;
    }

    public CommandHandler setCommand(ECommand command) {
        this.command = command;
        return this;
    }
}
