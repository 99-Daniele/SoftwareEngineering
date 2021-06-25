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

        // TODO:if no params, does NOT star with any message!

        int paramCount = args.length;
        ClientView gui;

        switch (paramCount){
            case 1:
                oneParamStart(args[0]);
                return;
            case 3:
                threeParamStart(args);
                return;
            case 5:
                fiveParamStart(args);
                return;
            default:
                startGUI();
        }
    }

    private static void oneParamStart(String param){
        switch (param){
            case "--server":
            case "-s":
                startServer();
                break;
            case "--cli":
            case "-c":
                startCLI();
                break;
            default:
                startGUI();
                break;
        }
    }

    private static void threeParamStart(String[] params){
        switch (params[0]){
            case "--server":
            case "-s":
                if(!startServer(params[1], params[2]))
                    startGUI();
                break;
            default:
                startGUI();
                break;
        }
    }

    private static void fiveParamStart(String[] params){
        switch (params[0]){
            case "--cli":
            case "-c":
                if(!startCLI(params[1], params[2], params[3], params[4]))
                    startGUI();
                break;
            default:
                if(!startGUI(params[1], params[2], params[3], params[4]))
                    startGUI();
                break;
        }
    }

    private static void startServer(){
        Server server = new Server();
        server.startServer();
    }

    private static boolean startServer(String param, String portNumber){
        if(!param.equals("--port") && !param.equals("-p"))
            return false;
        try {
            Server server = new Server(Integer.parseInt(portNumber));
            server.startServer();
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private static void startGUI(){
        ClientView gui = new GUI();
        gui.launchGUI();
    }

    private static boolean startGUI(String param1, String hostname, String param2, String portNumber){
        if((!param1.equals("--hostname") && !param1.equals("-h"))
            || (!param2.equals("--port") && !param2.equals("-p")))
            return false;
        try {
            ClientView gui = new GUI();
            gui.launchGUI(hostname, Integer.parseInt(portNumber));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private static void startCLI(){
        ClientView cli = new CLI();
        cli.launchCLI();
    }

    private static boolean startCLI(String param1, String hostname, String param2, String portNumber){
        if((!param1.equals("--hostname") && !param1.equals("-h"))
                || (!param2.equals("--port") && !param2.equals("-p")))
            return false;
        try {
            ClientView cli = new CLI();
            cli.launchCLI(hostname, Integer.parseInt(portNumber));
            return true;
        } catch (NumberFormatException e){
            return false;
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
