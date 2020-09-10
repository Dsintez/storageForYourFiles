package com.dsintez;

import com.dsintez.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port = 8982;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        System.out.println("Запускаем сервер");
        try {
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен");
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            System.out.println("Новый клиент");
            new Thread(new ClientHandler(socket)).start();
        }
    }
}
