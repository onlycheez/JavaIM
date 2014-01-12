
package javaim.server.lib;

import java.io.*;
import java.net.Socket;

/**
 * Is used for communication with client over the network.
 */
public class Request {

    private ObjectOutputStream outputStream;

    public Request(Socket socket) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Send message to client.
     * @param from Author of message.
     * @param to Adressee of message.
     * @param content Text of message.
     */
    public void sendMessage(String from, String to, String content) {
        final String[] message = new String[4];
        message[0] = Protocol.MESSAGE;
        message[1] = from;
        message[2] = to;
        message[3] = content;

        writeToStreamAsync((Object) message);
    }

    /**
     * Sends contactc list to client.
     * @param contacts List of actually logged contacts.
     */
    public void sendContactsList(String[] contacts) {
        String[] message = new String[contacts.length + 1];
        message[0] = Protocol.CONTACTS_LIST;

        System.out.print("Sending contacts list: ");

        for (int i = 0; i < contacts.length; i++) {
            message[i + 1] = contacts[i];
            System.out.print(contacts[i] + ", ");
        }

        System.out.println();

        writeToStreamAsync((Object) message);
    }

    /**
     * Asynchronously writes object to output stream.
     * @param message Serializable object to be written. Should be cast
     * to {@link java.lang.Object} from {@link java.lang.String[]}.
     */
    private void writeToStreamAsync(final Object message) {
        new Thread(new Runnable() {
          public void run() {
              try {
                  outputStream.writeObject(message);
                } catch (IOException ex) {
                    ;
                }
            }
        }).start();
    }
}
