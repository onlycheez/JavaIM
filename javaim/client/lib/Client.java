
package javaim.client.lib;

import javaim.client.ui.View;
import javaim.client.lib.UserListener;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Client {
    private View view;
    private String username;
    private String password;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private Thread userListener;
    private Thread serverListener;

    public Client(View view, String username, String password)
            throws UnknownHostException, IOException {
        this.view = view;
        this.username = username;
        this.password = password;
        this.socket = new Socket("localhost", 4444);

        this.outputStream = new ObjectOutputStream(socket.getOutputStream());

        userListener = new Thread(new UserListener(this, socket));
        serverListener = new Thread(new ServerListener(this.view, socket));

        this.view.setMessageSentListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    sendMessage(event.getActionCommand().split("@", 2)[0],
                            event.getActionCommand().split("@", 2)[1]);
                } catch (IOException ex) {
                    // Exception
                    System.out.println("IO Error");
                }
            }
        });
    }

    public void run() {
        System.out.println("Client");

        userListener.start();
        serverListener.start();

        try {
            boolean isLogged = false;
            while (!isLogged) {
                isLogged = login();
          }
        } catch (IOException ex) {
            System.out.println("Failed to login. IO error.");
        }
    }

    private boolean login() throws IOException {
        String[] message = new String[3];
        message[0] = "login";
        message[1] = username;
        message[2] = password;

        outputStream.writeObject((Object)message);
        return true;
    }

    public synchronized void sendMessage(String to, String content)
            throws IOException {
        String[] message = new String[4];
        message[0] = "message";
        message[1] = username;
        message[2] = to;
        message[3] = content;

        outputStream.writeObject((Object)message);
    }
}
