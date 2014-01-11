
package javaim.server.lib;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

class Worker implements Runnable {

  private Socket socket = null;
  private Server server = null;
  private ObjectInputStream inputStream = null;
  private ObjectOutputStream outputStream = null;
  private boolean isLogged = false;
  private String username = null;

  public Worker(Server server, Socket socket) throws IOException {
    this.server = server;
    this.socket = socket;
    inputStream = new ObjectInputStream(socket.getInputStream());
    outputStream = new ObjectOutputStream(socket.getOutputStream());
  }

  public boolean getIsLogged() {
    return isLogged;
  }

  public String getUsername() {
    return username;
  }

  public void run() {
    try {
      while (!isLogged) {
        isLogged = login();
      }
      System.out.println(username + " has logged in.");
      runChat();
      logout();
    } catch (ClassNotFoundException ex) {
      System.out.println("Class not found.");
    } catch (EOFException ex) {
      System.out.println(username + " has disconnected.");
      server.clientDisconnected(username);
    } catch (IOException ex) {
      System.out.println("IOException");
    }

    System.out.println(Thread.currentThread().getName() + ": Worker started");
  }

  public void sendMessage(String from, String to, String content) {
    final String[] message = new String[4];
    message[0] = "message";
    message[1] = from;
    message[2] = to;
    message[3] = content;

    new Thread(new Runnable() {
      public void run(){
        try {
          outputStream.writeObject((Object)message);
        } catch (IOException ex) {
          ;
        }
      }
    }).start();
  }

  private boolean login()
  throws ClassNotFoundException, EOFException, IOException {
    String[] message = (String[])inputStream.readObject();

    System.out.print("login: ");
    for (String s : message) {
      System.out.print(s + ", ");
    }
    System.out.println();

    if (!message[0].equals("login")) {
      return false;
    }

    username = message[1];

    return true;
  }

  private void logout() {
    ;
  }

  private void runChat()
  throws ClassNotFoundException, EOFException, IOException {
    String[] message;

    while ((message = (String[])inputStream.readObject()) != null) {
      System.out.print("Received: ");
      for (String argument : message) {
        System.out.print(argument + ", ");
      }
      System.out.println();

      switch (message[0]) {
        case "login":
          System.out.println("Multiple login attempt from " + message[1] + ".");
          break;
        case "message":
          server.passMessage(message[1], message[2], message[3]);
          break;
        default:
          System.out.println("Invalid method called \"" + message[0] + "\".");
      }
    }
  }
}
