
package javaim.server.lib;

import javaim.server.lib.Worker;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * Class representing instant messaging server.
 */
public class Server {

  private final static int PORT = 4444;
  private ServerSocket serverSocket = null;
  private ArrayList<Thread> clients = null;
  private ArrayList<Worker> workers = null;

  public Server() throws IOException {
    serverSocket = new ServerSocket(PORT);
    clients = new ArrayList<>();
    workers = new ArrayList<>();
  }

  /**
   * Opens TCP socket and waits for client connections.
   */
  public void run() throws IOException {
    System.out.println("Server is running");

    while (true) {
      Socket clientSocket = serverSocket.accept();
      Worker worker = new Worker(this, clientSocket);
      workers.add(worker);
      Thread client = new Thread(worker);
      client.start();
      clients.add(client);
    }
  }

  public void passMessage(String from, String to, String message) {
    boolean found = false;

    for (Worker worker : workers) {
      if (worker.getIsLogged() && worker.getUsername().equals(to)) {
        worker.sendMessage(from, to, message);
        found = true;
        break;
      }
    }

    if (!found) {
      System.out.println("Username " + to + " was not found.");
    }
  }
}