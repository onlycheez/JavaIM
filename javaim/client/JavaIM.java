
package javaim.client;

import javaim.client.view.View;
import javaim.client.controller.Controller;

import java.io.*;
import java.net.UnknownHostException;
import javax.swing.SwingUtilities;

public class JavaIM {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;
    private static final String ValidIpAddressRegex =
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private static final String ValidHostnameRegex =
            "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*" +
            "([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
    private static final String ValidPortRegex = "^[0-9]{1,4}$";

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Invalid number of arguments");
            System.out.println("    Usage: java javaim.client.JavaIM " +
                    "[hostname[ PORT]]");
            return;
        }

        final String host;
        final int port;

        if (args.length > 0) {
            if (args[0].matches(ValidHostnameRegex) ||
                        args[0].matches(ValidIpAddressRegex)) {
                host = args[0];
            } else {
                System.out.println("Invalid argument: " + args[0]);
                System.out.println("    Usage: java javaim.client.JavaIM " +
                        "[hostname[ PORT]]");
                return;
            }

            if (args.length > 1) {
                if (args[1].matches(ValidPortRegex)) {
                    port = Integer.parseInt(args[1]);
                } else {
                    System.out.println("Invalid argument: " + args[1]);
                    System.out.println("    Usage: java javaim.client.JavaIM " +
                            "[hostname[ PORT]]");
                    return;
                }
            } else {
                port = DEFAULT_PORT;
            }
        } else {
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }

        SwingUtilities.invokeLater(new Runnable() {
            private View view;
            Controller controller;

            @Override
            public void run() {
                view = new View();
                try {
                    controller = new Controller(view, host, port);
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
