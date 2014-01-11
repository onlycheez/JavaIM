
package javaim.client.lib;

import java.io.*;
import java.net.Socket;

public class UserListener implements Runnable {

    private Client client = null;
    private String username = "alice";

    public UserListener(Client client, Socket socket) {
        this.client = client;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String line;

        try {
            while (!(line = reader.readLine()).equals("exit")) {
                System.out.println("Sending: \"" + line + "\"");
                client.sendMessage("Dashie", line);
            }
        } catch (IOException ex) {
            System.out.println("Sending message failed. IO error.");
        }
    }
}
