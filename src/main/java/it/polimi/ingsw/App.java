package it.polimi.ingsw;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class App {

    private static ClientSocket client;

    public static void main(String[] args) {
        if (args.length >= 1 && (args[0].equals("--server") || args[0].equals("-s"))) {
            Server server;
            if (args.length >= 3 && (args[1].equals("--port") || args[1].equals("-p"))) {
                server = new Server(Integer.parseInt(args[2]));
            } else {
                server = new Server();
            }
            server.startServer();
        }
        else if (args.length >= 1 && (args[0].equals("--gui") || args[0].equals("-g"))) {
            ClientView gui = new GUI();
            connectToServer(args);
            gui.launchGUI();
        } else if (args.length >= 1 && (args[0].equals("--cli") || args[0].equals("-c"))) {
            ClientView cli = new CLI();
            connectToServer(args);
            cli.launchCLI();
        }
    }

    private static void connectToServer(String[] args){
        try {
            if (args.length > 4 && (args[1].equals("--hostname") || args[1].equals("-h")) &&
                    (args[3].equals("--port") || args[3].equals("-p"))) {
                connectionInfo(args[2], Integer.parseInt(args[4]));
            } else {
                connectionInfo();
            }
        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void connectionInfo() throws IOException, ExecutionException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("\nEnter hostname [localhost]: ");
            String hostName = stdIn.readLine();
            if (hostName == null || hostName.isBlank() || hostName.equals(""))
                hostName = "localhost";
            System.out.println("Enter port [12460]: ");
            String portNumber = stdIn.readLine();
            if (portNumber == null || portNumber.isBlank() ||portNumber.equals(""))
                portNumber = "12460";
            if(connectionInfo(hostName, Integer.parseInt(portNumber)))
                break;
        }
    }

    private static boolean connectionInfo(String hostName, int port) throws IOException, ExecutionException {
        try {
            client = new ClientSocket(new Socket(hostName, port));
            System.out.println("Accepted by Server");
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
        } catch (IOException e) {
            System.err.println("Can't connect to host " + hostName);
        }
        return false;
    }

    private static void startClient(ClientView clientView){
        client.addObserver(clientView);
        client.start();
    }
}
