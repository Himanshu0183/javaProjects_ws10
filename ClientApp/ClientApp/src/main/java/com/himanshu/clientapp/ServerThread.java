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
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ServerThread implements Runnable {

    Socket socket;
    Server server;
    DataInputStream input;
    DataOutputStream output;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try { //respond to clients
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String message = input.readUTF();
                server.sendToAllClients(message);
                Platform.runLater(() -> {
                    server.messageBoard.appendText(message + "\n");
                });
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }

    }

    //send message
    public void send(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException ex) {
            ex.getMessage();
        }

    }

}
