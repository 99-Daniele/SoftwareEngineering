package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Server class is a multiple games server.
 * port could be selected at hte beginning or be default one (12460)
 */
public class Server {

    private final int port;

    public Server(){
        port = 12460;
    }

    public Server(int port){
        this.port=port;
    }

    /**
     * start Server thread which constantly accept Client connections.
     */
    public void startServer() {
        java.net.ServerSocket serverSocket;
        try {
            serverSocket = new java.net.ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("\nNew Client\n");
                Thread thread = new Thread(new ServerSocket(socket));
                thread.start();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
