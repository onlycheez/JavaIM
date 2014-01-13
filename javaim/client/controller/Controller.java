
package javaim.client.controller;

import javaim.client.controller.lib.Request;
import javaim.client.controller.lib.Protocol;
import javaim.client.controller.lib.ServerListener;
import javaim.client.view.View;
import javaim.client.view.event.MessageSentListener;

import java.io.*;
import java.lang.Exception;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Logs in to server, sets callbacks for view and
 * starts server listening thread.
 */
public class Controller {

    private View view;
    private Socket socket;
    private Request request;
    private ObjectInputStream inputStream;
    private ServerListener serverListener;

    public Controller(View view)
            throws UnknownHostException, IOException {
        this.view = view;
        this.socket = new Socket("localhost", 4444);
        this.request = new Request(socket);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.serverListener = new ServerListener(view, inputStream);
    }

    /**
     * Starts all the fun.
     */
    public void run() {
        final String username = login();

        view.setUsername(username);
        view.setMessageSentListener(new MessageSentListener() {
            public void messageSent(String to, String message) {
                try {
                    request.sendMessage(username, to, message);
                } catch (IOException ex) {
                    System.out.println("Failed to send message. IO Error.");
                }
            }
        });

        new Thread(serverListener).start();

        try {
            request.askForContactsList();
        } catch (IOException ex) {
            System.out.println("askForContactsList failed. IO error.");
        }
    }

    /**
     * Logs user to server. Returns username which was used to login.
     */
    private String login() {
        String username = null;

        try {
            boolean isLogged = false;

            while (!isLogged) {
                String[] message = (String[]) inputStream.readObject();

                if (message.length == 0) {
                    continue;
                }

                System.out.print("Received: ");
                for (int i = 0; i < message.length; i++) {
                    System.out.print(message[i] + ", ");
                }
                System.out.println();

                switch (message[0])
                {
                    case Protocol.LOGIN:
                        isLogged = Boolean.valueOf(message[1]);
                        if (isLogged) {
                        } else {
                            String[] credentials = view.showLoginDialog();
                            username = credentials[0];
                            request.login(username, credentials[1]);
                        }
                        break;
                    case Protocol.MESSAGE:
                    case Protocol.CONTACTS_LIST:
                        ; // Cannot happpen. Before client is logged in
                          // server only asks for credentials.
                          // These methods are handled in ServerListener.
                    default:
                        System.out.println("Unknown message received: " +
                                message[0]);
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        } catch (EOFException ex) {
            System.out.println("Client has disconnected.");
        } catch (IOException ex) {
            System.out.println("IOException");
        }

        return username;
    }
}
