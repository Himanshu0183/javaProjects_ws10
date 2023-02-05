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

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientThread implements Runnable {

    Socket socket;
    Client client;
    DataInputStream input;

    public ClientThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override 
    public void run() {
        while (true) {
            try {
                //input stream
                input = new DataInputStream(socket.getInputStream());
                String message = input.readUTF();
                Platform.runLater(() -> client.messageBoard.appendText(message + "\n"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }
}
