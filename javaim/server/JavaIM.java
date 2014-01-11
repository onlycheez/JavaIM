
package javaim.server;

import javaim.server.lib.Server;
import java.io.IOException;

public class JavaIM {

  public static void main(String[] args) throws IOException {

    Server server = new Server();

    server.run();

  }

}
