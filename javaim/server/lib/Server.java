
package javaim.server.lib;

import javaim.server.lib.Worker;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * Class representing instant messaging server.
 */
public class Server {

    private ServerSocket serverSocket = null;
    private ArrayList<Thread> clients = null;
    private ArrayList<Worker> workers = null;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        workers = new ArrayList<>();
    }

    /**
     * Opens TCP socket and waits for client connections.
     */
    public void run() throws IOException {
        System.out.println("Server started.");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Worker worker = new Worker(this, clientSocket);
            workers.add(worker);
            Thread client = new Thread(worker);
            client.start();
            clients.add(client);
        }
    }

    /**
     * Passes message to specified user.
     * @param from Sender of message.
     * @param to Receiver of message.
     * @param message Text of message.
     */
    public void passMessage(String from, String to, String message) {
        boolean found = false;

        for (Worker worker : workers) {
            if (worker.getIsLogged() && worker.getUsername().equals(to)) {
                worker.sendMessage(from, to, message);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Username " + to + " was not found.");
        }
    }

    /**
     * Sends actual list of logged users to all connected clients.
     * @param exception User who is not sent contacts list. It's user who has
     * just logged in or out (and why this update occurs).
     */
    synchronized public void broadcastLoggedUsersUpdate(String exception) {
        String[] contacts = getLoggedContacts();

        for (Worker worker : workers) {
            if (worker.getIsLogged() &&
                    !worker.getUsername().equals(exception)) {
                worker.sendContactsList(contacts);
            }
        }
    }

    /**
     * Removes specified client from servers array.
     * @param username User who will be removed.
     */
    synchronized public void clientDisconnected(String username) {
        for (int i = 0; i < workers.size(); i++) {
            if (workers.get(i).getIsLogged() &&
                    workers.get(i).getUsername().equals(username)) {
                workers.remove(i);
                // TODO: clients should be updated too.
                break;
            }
        }
    }

    /**
     * Returns all actually logged users.
     * @return Array of logged users.
     */
    public String[] getLoggedContacts() {
        ArrayList<String> contacts = new ArrayList<>();

        for (Worker worker : workers) {
            if (worker.getIsLogged()) {
                contacts.add(worker.getUsername());
            }
        }

        return contacts.toArray(new String[0]);
    }
}
