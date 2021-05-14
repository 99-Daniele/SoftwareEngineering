package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.PosControllerGame;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

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
            virtualView.start();
            controllerGame.waitPlayers();
            virtualView.pronto(controllerGame.getNumPlayers());
            virtualView.join();
            disconnect(virtualView.getPosition(), virtualView.getNickname());
        } catch (IOException | InterruptedException e) {
            closeConnections();
        } catch (ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeConnections(){
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    private void disconnect(int position, String nickName) throws IOException {
        closeConnections();
        controllerGame.quitGame(nickName, position);
    }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
