
package javaim.client.controller;

import javaim.client.controller.lib.Request;
import javaim.client.controller.lib.ServerListener;
import javaim.client.view.View;
import javaim.client.view.event.MessageSentListener;

import java.io.*;
import java.lang.Exception;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {

    protected class ExceptionNotLogged extends Exception {};

    private View view;
    private String username;
    private Socket socket;
    private Request request;
    private Thread serverListener;

    public Controller(View view)
            throws UnknownHostException, IOException {
        this.view = view;
        this.socket = new Socket("localhost", 4444);
        this.request = new Request(socket);

        serverListener = new Thread(new ServerListener(this.view, socket));
    }

    public void run() {
        serverListener.start();

        try {
            String[] credentials = null;
            boolean isLogged = false;
            while (!isLogged) {
                credentials = view.showLoginDialog();
                if (credentials[0] == null) {
                    throw new ExceptionNotLogged();
                }
                isLogged = request.login(credentials[0], credentials[1]);
            }
            username = credentials[0];

            setMessageSentListener();
            request.askForContactsList();
        } catch (IOException ex) {
            System.out.println("Failed to login. IO error.");
        } catch (ExceptionNotLogged ex) {
            System.out.println("User hasn't logged in.");
        }
    }

    private void setMessageSentListener() {
        view.setMessageSentListener(new MessageSentListener() {
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
}
