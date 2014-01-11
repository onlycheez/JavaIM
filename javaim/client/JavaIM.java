
package javaim.client;

import javaim.client.ui.View;
import javaim.client.lib.Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.SwingUtilities;


public class JavaIM {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println ("Invalid number of arguments.");
            System.out.println ("  Usage: client <username> <password>");
            return;
        }

        final String username = args[0];
        final String password = args[1];

        SwingUtilities.invokeLater(new Runnable() {
            private View view;
            Client client;

            @Override
            public void run() {
                view = new View();
                try {
                    client = new Client(view, username, password);
                    client.run();
                } catch (UnknownHostException ex) {
                    System.out.println("Unknown host");
                } catch (IOException ex) {
                    System.out.println("IO error");
                }
            }
        });
    }
}