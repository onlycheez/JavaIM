
package javaim.client;

import javaim.client.view.View;
import javaim.client.controller.Controller;

import java.io.*;
import java.net.UnknownHostException;
import javax.swing.SwingUtilities;

public class JavaIM {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            private View view;
            Controller controller;

            @Override
            public void run() {
                view = new View();
                try {
                    controller = new Controller(view);
                    controller.run();
                } catch (UnknownHostException ex) {
                    System.out.println("Unknown host");
                } catch (IOException ex) {
                    System.out.println("IO error");
                }
            }
        });
    }
}
