
package javaim.server.lib;

import javaim.server.lib.Protocol;
import javaim.server.lib.Request;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

class Worker implements Runnable {

    private static final String USERS_DB_FILE = "javaim/server/data/users";
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
            /* Firstly ask client for login information. */
            request.login(false);

            while (true) {
                String[] message = (String[]) inputStream.readObject();

                if (message.length == 0) {
                    continue;
                }

                switch (message[0]) {
                    case Protocol.LOGIN:
                        username = message[1].toLowerCase();
                        isLogged = login(username, message[2]);
                        request.login(isLogged);

                        if (isLogged) {
                            System.out.println(username + " has logged in.");
                            server.broadcastLoggedUsersUpdate(username);
                        } else {
                            System.out.println(username + " login failed.");
                        }
                        break;
                    case Protocol.CONTACTS_LIST:
                        System.out.println(username +
                                " asked for contacts list.");
                        request.sendContactsList(server.getLoggedContacts());
                        break;
                    case Protocol.MESSAGE:
                        server.passMessage(username, message[2].toLowerCase(),
                                message[3]);
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
        } catch (IOException ex) {
            System.out.println("IOException");
        } finally {
            server.clientDisconnected(username);
            server.broadcastLoggedUsersUpdate(username);
        }
    }

    public void sendContactsList(String[] contacts) {
        request.sendContactsList(contacts);
    }

    public void sendMessage(String from, String to, String content) {
        request.sendMessage(from, to, content);
    }

    /**
     * Veryfies login information provided by client.
     * @param username Username.
     * @param password Password.
     * @return true when credentials are valid false otherwise.
     */
    private boolean login(String username, String password)
            throws ClassNotFoundException, EOFException, IOException {
        String lowercasedUsername = username.toLowerCase();

        BufferedReader reader = new BufferedReader(
                new FileReader(USERS_DB_FILE));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] splittedLine = line.split(" ", 2);
            if (lowercasedUsername.equals(splittedLine[0]) &&
                    password.equals(splittedLine[1])) {
                reader.close();
                return true;
            }
        }
        reader.close();

        return false;
    }
}
