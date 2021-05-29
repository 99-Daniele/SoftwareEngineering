package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.GUI.GUI;

public class App {
    public static void main(String[] args) {
        if (args.length >= 1 && (args[0].equals("--server") || args[0].equals("-s"))) {
            Server server;
            if (args.length >= 3 && (args[1].equals("--port") || args[1].equals("-p"))) {
                server = new Server(Integer.parseInt(args[2]));
            } else {
                server = new Server();
            }
            server.startServer();
        } else if (args.length >= 1 && (args[0].equals("--gui") || args[0].equals("-g"))) {
            GUI gui = new GUI();
            if (args.length > 4 && (args[1].equals("--hostname") || args[1].equals("-h")) &&
                    (args[3].equals("--port") || args[3].equals("-p"))) {
                gui.launchGUI(args[2], Integer.parseInt(args[4]));
            } else {
               gui.launchGUI();
            }
        } else if (args.length >= 1 && (args[0].equals("--cli") || args[0].equals("-c"))) {
            CLI cli = new CLI();
            if (args.length > 4 && (args[1].equals("--hostname") || args[1].equals("-h")) &&
                    (args[3].equals("--port") || args[3].equals("-p"))) {
                cli.launchCLI(args[2], Integer.parseInt(args[4]));
            } else
                cli.launchCli();
        }
    }
}
