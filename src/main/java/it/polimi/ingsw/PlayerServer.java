package it.polimi.ingsw;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerServer implements Runnable {

    private final Socket socket;
    private ControllerGame controllerGame;

    public PlayerServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        Scanner in = null;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        VirtualView virtualView = new VirtualView(in, out);
        String nickname = virtualView.nickname();
        PosControllerGame posControllerGame= Connection.ConnectionPlayers(virtualView, nickname);
        controllerGame= posControllerGame.getControllerGame();
        virtualView.position(posControllerGame.getPosition());
        controllerGame.waitPlayers();
        virtualView.pronto();
        while (true){
            virtualView.start();
        }
    }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
