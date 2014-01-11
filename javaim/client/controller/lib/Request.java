
package javaim.client.controller.lib;

import javaim.client.controller.lib.Protocol;

import java.io.*;
import java.net.Socket;

/**
 * Sends messages to server.
 */
public class Request {

    private ObjectOutputStream outputStream;

    public Request(Socket socket) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean login(String username, String password) throws IOException {
        String[] message = new String[3];
        message[0] = Protocol.LOGIN;
        message[1] = username;
        message[2] = password;

        outputStream.writeObject((Object) message);
        return true;
    }

    public void askForContactsList() throws IOException {
        String[] message = new String[1];
        message[0] = Protocol.CONTACTS_LIST;

        outputStream.writeObject((Object) message);
    }

    public synchronized void sendMessage(String from, String to, String content)
            throws IOException {
        String[] message = new String[4];
        message[0] = Protocol.MESSAGE;
        message[1] = from;
        message[2] = to;
        message[3] = content;

        outputStream.writeObject((Object) message);
    }
}
