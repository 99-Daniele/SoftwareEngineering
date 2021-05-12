package it.polimi.ingsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;

    public Server(int port){
        this.port=port;
    }

    public void startServer() {
        ServerSocket serverSocket;
        try {
            serverSocket=new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server pronto");
        while(true){
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("\nAccettato nuovo Client\n");
                    Thread thread = new Thread(new PlayerServer(socket));
                    thread.start();
                } catch (IOException e) {
                    System.err.println(e.getMessage()); //si attiva se serversocket viene chiuso
                }
        }
    }

    public static void main(String[] args) {
        Server server=new Server(1234); //Integer.parseInt(args[0]) parametro
        server.startServer();
    }
}
