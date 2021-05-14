package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.PosControllerGame;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class PlayerServer implements Runnable {

    private final Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    private ControllerGame controllerGame;

    public PlayerServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            VirtualView virtualView = new VirtualView(in, out);
            String nickname = virtualView.getNickname();
            while (nickname == null)
                nickname = virtualView.getNickname();
            PosControllerGame posControllerGame = Connection.ConnectionPlayers(virtualView, nickname);
            controllerGame = posControllerGame.getControllerGame();
            virtualView.position(posControllerGame.getPosition());
            controllerGame.waitPlayers();
            virtualView.run();
            disconnect(virtualView.getPosition(), virtualView.getNickname());
        } catch (IOException e) {
            closeConnections();
        }
    }

    private void closeConnections(){
        try {
            in.close();
            out.close();
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
        }
        finally {
            System.out.println("Connessione col Client chiusa");
        }
    }

    private void disconnect(int position, String nickName) throws IOException {
        try {
            controllerGame.quitGame(nickName, position);
            closeConnections();
        }catch (SocketException e){ }
    }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
