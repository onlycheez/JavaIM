
package javaim.server.lib;

import javaim.server.lib.Protocol;
import javaim.server.lib.Request;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

class Worker implements Runnable {

    private Socket socket;
    private Server server;
    private Request request;
    private ObjectInputStream inputStream;
    private boolean isLogged;
    private String username;

    public Worker(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.request = new Request(socket);
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public String getUsername() {
        return username;
    }

    public void run() {
        try {
            while (true) {
                String[] message = (String[]) inputStream.readObject();

                if (message.length == 0) {
                    continue;
                }

                switch (message[0]) {
                    case Protocol.LOGIN:
                        login(message[1], message[2]);
                        System.out.println(username + " has logged in.");
                        server.broadcastLoggedUsersUpdate(username);
                        break;
                    case Protocol.CONTACTS_LIST:
                        System.out.println(username +
                                " asked for contacts list.");
                        request.sendContactsList(server.getLoggedContacts());
                        break;
                    case Protocol.MESSAGE:
                        server.passMessage(message[1], message[2], message[3]);
                        System.out.println(username + " sends message to " +
                                message[2]);
                        break;
                    default:
                        System.out.println(username +
                                " called invalid method \"" +
                                message[0] + "\".");
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        } catch (EOFException ex) {
            System.out.println(username + " has disconnected.");
            server.clientDisconnected(username);
            server.broadcastLoggedUsersUpdate(username);
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    public void sendContactsList(String[] contacts) {
        request.sendContactsList(contacts);
    }

    public void sendMessage(String from, String to, String content) {
        request.sendMessage(from, to, content);
    }

    private void login(String contact, String password)
            throws ClassNotFoundException, EOFException, IOException {
        username = contact;
        isLogged = true;
    }
}
