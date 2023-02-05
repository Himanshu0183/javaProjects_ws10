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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client extends Application {

    TextField clientInput;
    ScrollPane scrollPane;
    public TextArea messageBoard;

    DataOutputStream output = null;
    private Stage primaryStage;
    String name = "emptyChat";

    @Override
    public void start(Stage stage) {

        this.primaryStage = stage;
        scrollPane = new ScrollPane();
        messageBoard = new TextArea();

        messageBoard.setEditable(false);
        scrollPane.setContent(messageBoard);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        Font font = Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 16);
        messageBoard.setFont(font);

        clientInput = new TextField();
        clientInput.setPromptText("Enter your name: ");
        Button btnSend = new Button("Send");

        Font font1 = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16);
        clientInput.setFont(font1);
        btnSend.setFont(font1);


        btnSend.setOnAction(actionEvent -> {
            if (name.equals("emptyChat")) {
                String user = clientInput.getText().trim();
                if (user.length() != 0) {
                    name = user;
                    updateTitle(name);
                    messageBoard.appendText("Enter your name: " + name + "\n");
                    clientInput.clear();
                    clientInput.setPromptText("Enter message: ");
                }
            } else {
                try {

                    String message = clientInput.getText().trim();
                    updateTitle(name);

                    if (message.length() == 0) {//avoid empty messages
                        return;
                    }

                    //send
                    output.writeUTF(name + ": " + message + "");
                    output.flush();
                    clientInput.clear();
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
            clientInput.requestFocus();
        });
        HBox hBox = new HBox(clientInput, btnSend);
        VBox vBox = new VBox(scrollPane, hBox);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(clientInput, Priority.ALWAYS);

        vBox.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vBox, 750, 500);
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();

        try {
            // Create a socket
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("Connection from Socket[addr=" + socket.getLocalAddress() + ",port="
                    + socket.getLocalPort() + ",localport=" + socket.getPort() + "] at " + new java.util.Date());
            output.flush();

            //create a client thread
            ClientThread task = new ClientThread(socket, this);
            Thread thread = new Thread(task);
            thread.start();

        } catch (IOException ex) {
            messageBoard.appendText(ex.toString() + '\n');
        }
    }

    public void updateTitle(String title) {
        primaryStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
