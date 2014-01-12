
package javaim.client.controller.lib;

import javaim.client.controller.lib.Protocol;
import javaim.client.view.View;

import java.io.*;
import java.net.Socket;

/**
 * Listens to server and performs actions on client. Takes care of showing
 * received messages and updating contacts list.
 */
public class ServerListener implements Runnable {

    private View view;
    private Socket socket;
    private ObjectInputStream inputStream;

    public ServerListener(View view, Socket socket) throws IOException {
        this.view = view;;
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        try {
            while (true) {
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
                    case Protocol.MESSAGE:
                        view.showMessage(message[1], message[3]);
                        break;
                    case Protocol.CONTACTS_LIST:
                        view.updateContactsList(message);
                        break;
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
    }
}
