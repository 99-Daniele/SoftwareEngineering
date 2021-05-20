package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.CLI.CLI;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

    private final String hostName;
    private final int portNumber;
    private CLI cli;

    public Client(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * create and start new socket connection with localhost.
     */
    private void startClient() {
        try {
            ClientSocket clientSocket = new ClientSocket(new Socket(hostName, portNumber));
            System.out.println("Accepted by Server");
            clientSocket.start();
            launchCLI(clientSocket);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can't connect to host " + hostName);
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("localhost", 1234);
        client.startClient();
    }

    public void launchCLI(ClientSocket clientSocket){
        this.cli = new CLI(clientSocket);
    }
}

