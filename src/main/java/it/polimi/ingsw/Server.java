package it.polimi.ingsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port){
        this.port=port;
    }

    public void startServer()
    {
        ServerSocket serverSocket;
        try {
            serverSocket=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Server pronto");
        while(true){
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread=new Thread(new PlayerServer(socket));
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace(); //si attiva se serversocket viene chiuso
                }
        }
    }

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        Server server=new Server(portNumber); //Integer.parseInt(args[0]) parametro
        server.startServer();
    }
}
