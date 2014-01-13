
package javaim.server;

import javaim.server.lib.Server;
import java.io.IOException;

public class JavaIM {

    private static final String ValidPortRegex = "^[0-9]{1,4}$";

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Invalid number of arguments");
            System.out.println("    Usage: java javaim.server.JavaIM [PORT]");
            return;
        }

        int port = 4444;

        if (args.length == 1) {
            if (args[0].matches(ValidPortRegex)) {
                port = Integer.parseInt(args[1]);
            }
        }

        Server server = new Server(port);
        server.run();

    }

}
