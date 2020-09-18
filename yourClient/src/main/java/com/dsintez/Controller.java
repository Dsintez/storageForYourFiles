package com.dsintez;

import com.dsintez.model.ThreadHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final int port = 8982;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static Socket socket;
    private static ThreadHandler threadHandler;
    private final static String prePath = "Files/client";

    @FXML
    private TextField pathField;

    public void sendFile(ActionEvent actionEvent) {
        if ("".equals(pathField.getText())) return;
        new Thread(threadHandler
                .setPath(Paths.get(prePath, pathField.getText()))
                .setCommand(ECommand.SEND)
        ).start();
        pathField.setText("");
    }

    public void receiveFile(ActionEvent actionEvent) {
        if ("".equals(pathField.getText())) return;
        new Thread(threadHandler
                .setPath(Paths.get(prePath, pathField.getText()))
                .setCommand(ECommand.RECEIVE)
        ).start();
        pathField.setText("");
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            threadHandler = new ThreadHandler(dataInputStream, dataOutputStream);
            //TODO 09/09/2020
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
