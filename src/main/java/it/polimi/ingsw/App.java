package it.polimi.ingsw;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.net.Socket;

/**
 * App is the main application which creates a new Server or a new Client in CLI or GUI mode.
 */
public class App {

    private static ClientSocket client;

    /**
     * @param args are player main arguments.
     *
     * if args represent a server mode, new Server is created and started.
     * if args represent a CLI client, new CLI is created and launched.
     * if args represent a GUI client, new GUI is created and launched.
     */
    public static void main(String[] args) {

        // TODO:
        // Pls get number of args
        // use switch on that number.

        // TODD2: if no parms, does NOT star with any message!
        int paramCount = args.length;
        ClientView gui;

        switch (paramCount){
            // TODO: finire...
            case  0:
                gui = new GUI();
                gui.launchGUI();
                return;

        }

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
            gui = new GUI();
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

    /**
     * @param clientView is player view.
     *
     * create a new Socket connection with Server and add @param clientView to client observer.
     */
    public static void createClient(ClientView clientView){
        client = new ClientSocket();
        client.addObserver(clientView);
    }

    /**
     * @param hostname is Server address.
     * @param port is Server port.
     * @throws IOException if is not possible created a connection with Server.
     */
    public static void startClient(String hostname, int port) throws IOException {
        client.start(new Socket(hostname, port));
    }
}
