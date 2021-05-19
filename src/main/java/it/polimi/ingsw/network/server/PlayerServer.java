package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class PlayerServer implements Runnable {

    private final Socket socket;
    private ControllerGame controllerGame;
    private VirtualView virtualView;

    public PlayerServer(Socket socket) throws IOException {
        this.socket = socket;
        this.virtualView = new VirtualView(socket);
    }

    public void run() {
        try {
            virtualView.start();
            controllerGame = Connection.ConnectionPlayers();
            int viewID = controllerGame.addView(virtualView);
            while (viewID == -1){
                controllerGame = Connection.ConnectionPlayers();
                viewID = controllerGame.addView(virtualView);
            }
            virtualView.setViewID(viewID);
            virtualView.addObserver(controllerGame);
            virtualView.join();
            disconnect(virtualView.getNickname(), virtualView.getViewID());
        } catch (IOException | InterruptedException e) {
            disconnect(virtualView.getNickname(), virtualView.getViewID());
        }
    }

    private void closeConnections(){
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    private void disconnect(String nickName, int position){
        controllerGame.quitGame(nickName, position);
        closeConnections();
    }
}
