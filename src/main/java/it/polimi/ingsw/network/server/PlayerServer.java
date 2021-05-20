package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.exceptions.FullGameException;
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
            while (true) {
                try {
                    controllerGame = Connection.ConnectionPlayers();
                    controllerGame.addView(virtualView);
                    break;
                } catch (FullGameException e) {}
            }
            virtualView.addObserver(controllerGame);
            virtualView.join();
            disconnect(virtualView.getNickname());
        } catch (IOException | InterruptedException e) {
            disconnect(virtualView.getNickname());
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

    private void disconnect(String nickName){
        controllerGame.quitGame(virtualView, nickName);
        closeConnections();
    }
}
