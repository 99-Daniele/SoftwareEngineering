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
            if (args.length > 4 && (args[1].equals("--hostname") || args[1].equals("-h")) &&
                    (args[3].equals("--port") || args[3].equals("-p"))) {
                gui.launchGUI(args[2], Integer.parseInt(args[4]));
            }
            else
                gui.launchGUI();
        } else if (args.length >= 1 && (args[0].equals("--cli") || args[0].equals("-c"))) {
            ClientView cli = new CLI();
            if (args.length > 4 && (args[1].equals("--hostname") || args[1].equals("-h")) &&
                    (args[3].equals("--port") || args[3].equals("-p"))) {
                cli.launchCLI(args[2], Integer.parseInt(args[4]));
            }
            else
                cli.launchCLI();
        }
    }

    public static void createClient(ClientView clientView){
        client = new ClientSocket();
        client.addObserver(clientView);
    }

    public static void startClient(String hostname, int port) throws UnknownHostException, IOException {
        client.start(new Socket(hostname, port));
    }
}
