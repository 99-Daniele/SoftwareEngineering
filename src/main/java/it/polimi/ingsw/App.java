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
        int paramCount = args.length;
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

    /**
     * @param param is parameter inserted by user in main.
     *
     * in case user select server or CLI mode, launch them. In any other cases launch GUI.
     */
    private static void oneParamStart(String param){
        switch (param){
            case "--server":
            case "-s":
                startServer();
                break;
            case "--devcli":
            case "-dc":
                startDevCLI();
                break;
            case "--cli":
            case "-c":
                startCLI();
                break;
            case "--devgui":
            case "-dg":
                startDevGUI();
                break;
            default:
                startGUI();
                break;
        }
    }

    /**
     * @param params are parameters inserted by user in main.
     *
     * in case user select server, create and start it. In any other cases launch GUI.
     */
    private static void threeParamStart(String[] params){
        switch (params[0]){
            case "--server":
            case "-s":
                startServer(params[1], params[2]);
                break;
            default:
                startGUI();
                break;
        }
    }

    /**
     * @param params are parameters inserted by user in main.
     *
     * in case user select CLI mode, launch it. In any other cases launch GUI.
     */
    private static void fiveParamStart(String[] params){
        switch (params[0]){
            case "--devcli":
            case "-dc":
                if(!startDevCLI(params[1], params[2], params[3], params[4]))
                    startDevGUI();
                break;
            case "--cli":
            case "-c":
                if(!startCLI(params[1], params[2], params[3], params[4]))
                    startGUI();
                break;
            case "--devgui":
            case "-dg":
                if(!startDevGUI(params[1], params[2], params[3], params[4]))
                    startDevGUI();
                break;
            default:
                if(!startGUI(params[1], params[2], params[3], params[4]))
                    startGUI();
                break;
        }
    }

    /**
     * create and start a new Server on default port 12460.
     */
    private static void startServer(){
        Server server = new Server();
        server.startServer();
    }

    /**
     * @param param is one parameter inserted by user in main.
     * @param portNumber is the port number inserted by user in main parameters.
     *
     * if user has inserted wrong parameters create a new default Server and start it.
     */
    private static void startServer(String param, String portNumber){
        Server server;
        if(!param.equals("--port") && !param.equals("-p")){
            server = new Server();
            server.startServer();
        }
        try {
            server = new Server(Integer.parseInt(portNumber));
            server.startServer();
        } catch (NumberFormatException e){
            server = new Server();
            server.startServer();
        }
    }

    /**
     * launch GUI with any connection with Server.
     */
    private static void startGUI(){
        ClientView gui = new GUI();
        gui.launchGUI();
    }

    /**
     * launch GUI in DevelopersMode with any connection with Server
     */
    private static void startDevGUI(){
        ClientView gui = new GUI();
        ClientView.setDevelopers();
        gui.launchGUI();
    }

    /**
     * @param param1 is one parameter inserted by user in main.
     * @param hostname is the hostname inserted by user in main parameters.
     * @param param2 is one parameter inserted by user in main.
     * @param portNumber is the portNumber inserted by user in main.
     * @return false if connection with Server @param hostname on port @param portNumber fails or user hs inserted wrong
     * parameters.
     */
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

    /**
     * @param param1 is one parameter inserted by user in main.
     * @param hostname is the hostname inserted by user in main parameters.
     * @param param2 is one parameter inserted by user in main.
     * @param portNumber is the portNumber inserted by user in main.
     * @return false if connection with Server @param hostname on port @param portNumber fails or user hs inserted wrong
     * parameters.
     */
    private static boolean startDevGUI(String param1, String hostname, String param2, String portNumber){
        if((!param1.equals("--hostname") && !param1.equals("-h"))
                || (!param2.equals("--port") && !param2.equals("-p")))
            return false;
        try {
            ClientView gui = new GUI();
            ClientView.setDevelopers();
            gui.launchGUI(hostname, Integer.parseInt(portNumber));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * launch CLI with any connection with Server.
     */
    private static void startCLI(){
        ClientView cli = new CLI();
        cli.launchCLI();
    }

    /**
     * launch CLI on DevelopersMode with any connection with Server.
     */
    private static void startDevCLI(){
        ClientView cli = new CLI();
        ClientView.setDevelopers();
        cli.launchCLI();
    }

    /**
     * @param param1 is one parameter inserted by user in main.
     * @param hostname is the hostname inserted by user in main parameters.
     * @param param2 is one parameter inserted by user in main.
     * @param portNumber is the portNumber inserted by user in main.
     * @return false if connection with Server @param hostname on port @param portNumber fails or user hs inserted wrong
     * parameters.
     */
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
     * @param param1 is one parameter inserted by user in main.
     * @param hostname is the hostname inserted by user in main parameters.
     * @param param2 is one parameter inserted by user in main.
     * @param portNumber is the portNumber inserted by user in main.
     * @return false if connection with Server @param hostname on port @param portNumber fails or user hs inserted wrong
     * parameters.
     */
    private static boolean startDevCLI(String param1, String hostname, String param2, String portNumber){
        if((!param1.equals("--hostname") && !param1.equals("-h"))
                || (!param2.equals("--port") && !param2.equals("-p")))
            return false;
        try {
            ClientView cli = new CLI();
            ClientView.setDevelopers();
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
