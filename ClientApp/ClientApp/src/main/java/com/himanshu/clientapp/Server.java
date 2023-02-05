/**********************************************
 Workshop 10
 Course: JAC 444 - Summer 2022
 Last Name: Himanshu
 First Name: Himanshu
 ID: 146109202
 Section: ZBB
 This assignment represents my own work in accordance with Seneca Academic Policy.
 Signature  Himanshu
 Date: 08/14/2022
 **********************************************/

package com.himanshu.clientapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends Application {
    public TextArea messageBoard;
    List<ServerThread> clientList = new ArrayList<ServerThread>();

    @Override
    public void start(Stage primaryStage) {

        messageBoard = new TextArea();
        messageBoard.setEditable(false);
        Font font = Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 13);
        messageBoard.setFont(font);

        ScrollPane scrollPane = new ScrollPane();   //pane to display text messages      
        scrollPane.setContent(messageBoard);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        Scene scene = new Scene(scrollPane, 750, 500);
        primaryStage.setTitle("Multi-threaded Server");
        primaryStage.setScene(scene);
        primaryStage.show();


        new Thread(() -> {
            try {
                // Create  socket
                ServerSocket serverSocket = new ServerSocket(8000);

                Platform.runLater(() -> messageBoard.appendText("MultiThreadServer started at " + new Date() + '\n'));

                while (true) {
                    Socket socket = serverSocket.accept();
                    ServerThread newCLient = new ServerThread(socket, this);
                    clientList.add(newCLient);
                    Thread thread = new Thread(newCLient);
                    thread.start();
                }
            } catch (IOException ex) {
                messageBoard.appendText(ex.toString() + '\n');
            }
        }).start();
    }

    public void sendToAllClients(String message) {
        for (ServerThread clientConnection : this.clientList) {
            if (!message.startsWith("Connection from Socket[addr") && !message.isEmpty()) {
                clientConnection.send(message);
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
