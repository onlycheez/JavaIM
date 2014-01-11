
package javaim.client.lib;

import javaim.client.ui.View;
import javaim.client.ui.ContactsListWindow;

import java.io.*;
import java.net.Socket;
import javax.swing.JTextArea;

class ServerListener implements Runnable {

    private ContactsListWindow contactsListWindow;
    private Socket socket;
    private ObjectInputStream inputStream;
    private View view;

    public ServerListener(View view, Socket socket) throws IOException {
        this.view = view;;
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        try {
            String[] message;

            while ((message = (String[])inputStream.readObject()) != null) {
                System.out.print("Received: \"" + message[3] + "\"");
                System.out.println();
                view.showMessage(message[1], message[3]);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found.");
        } catch (EOFException ex) {
            System.out.println("Client has disconnected.");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
}