package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            PosControllerGame posControllerGame = Connection.ConnectionPlayers(virtualView, nickname);
            controllerGame = posControllerGame.getControllerGame();
            virtualView.position(posControllerGame.getPosition());
            controllerGame.waitPlayers();
            virtualView.pronto(controllerGame.getNumPlayers());
            virtualView.run();
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
