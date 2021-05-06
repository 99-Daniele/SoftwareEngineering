package it.polimi.ingsw;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

    private final String hostName;
    private final int portNumber;
    private  ClientSocket clientSocket;

    public Client(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * create and start new socket connection with localhost.
     */
    private void startClient() {
        try {
            clientSocket = new ClientSocket(new Socket(hostName, portNumber));
            System.out.println("Accepted by Server");
            clientSocket.start();
        } catch (UnknownHostException e) {
            System.err.println("Non conosco l'host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Non riesco a connettermi all'host " + hostName);
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Client client = new Client("localhost", 1234);
        client.startClient();
    }
}

