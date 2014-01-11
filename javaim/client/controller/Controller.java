
package javaim.client.lib;

import javaim.client.controller.lib.Request;
import javaim.client.controller.lib.ServerListener;
import javaim.client.view.View;
import javaim.client.view.event.MessageSentListener;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    private View view;
    private String username;
    private String password;
    private Socket socket;
    private Request request;
    private Thread serverListener;

    public Controller(View view, final String username, String password)
            throws UnknownHostException, IOException {
        this.view = view;
        this.username = username;
        this.password = password;
        this.socket = new Socket("localhost", 4444);
        this.request = new Request(socket);

        serverListener = new Thread(new ServerListener(this.view, socket));

        this.view.setMessageSentListener(new MessageSentListener() {
            public void messageSent(String to, String message) {
                try {
                    request.sendMessage(username, to, message);
                } catch (IOException ex) {
                    // Exception
                    System.out.println("IO Error");
                }
            }
        });
    }

    public void run() {
        serverListener.start();

        try {
            boolean isLogged = false;
            while (!isLogged) {
                isLogged = request.login(username, password);
            }
            request.askForContactsList();
        } catch (IOException ex) {
            System.out.println("Failed to login. IO error.");
        }
    }
}
