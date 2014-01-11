
package javaim.server.lib;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

class Worker implements Runnable {

    private Socket socket = null;
    private Server server = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isLogged = false;
    private String username = null;

    public Worker(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public String getUsername() {
        return username;
    }

    public void run() {
        try {
            System.out.println(username + " has logged in.");
            listenToClient();
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        } catch (EOFException ex) {
            System.out.println(username + " has disconnected.");
            server.clientDisconnected(username);
            server.broadcastLoggedUsersUpdate();
        } catch (IOException ex) {
            System.out.println("IOException");
        }

        System.out.println(Thread.currentThread().getName() +
                ": Worker started");
    }

    public void sendMessage(String from, String to, String content) {
        final String[] message = new String[4];
        message[0] = "message";
        message[1] = from;
        message[2] = to;
        message[3] = content;

        new Thread(new Runnable() {
          public void run(){
              try {
                  outputStream.writeObject((Object)message);
                } catch (IOException ex) {
                    ;
                }
            }
        }).start();
    }

    private void login(String contact, String password)
            throws ClassNotFoundException, EOFException, IOException {
        System.out.println(contact + " has logged in.");
        username = contact;
        isLogged = true;
    }

    public void sendContactsList(String[] contacts) {
        String[] message = new String[contacts.length + 1];
        message[0] = "contactsList";
        for (int i = 0; i < contacts.length; i++) {
            message[i + 1] = contacts[i];
        }

        try {
              outputStream.writeObject((Object)message);
            } catch (IOException ex) {
                ;
            }
    }

    private void listenToClient()
            throws ClassNotFoundException, EOFException, IOException {
        String[] message;

        while ((message = (String[])inputStream.readObject()) != null) {
            System.out.print("Received: ");
            for (String argument : message) {
                System.out.print(argument + ", ");
            }
            System.out.println();

            switch (message[0]) {
                case "login":
                    login(message[1], message[2]);
                    server.broadcastLoggedUsersUpdate();
                    break;
                case "contactsList":
                    sendContactsList(server.getLoggedContacts());
                    break;
                case "message":
                    server.passMessage(message[1], message[2], message[3]);
                    break;
                default:
                    System.out.println("Invalid method called \"" +
                            message[0] + "\".");
            }
        }
    }
}
