package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerConnection;
import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket implements Runnable {

    private final Socket socket;
    private ControllerGame controllerGame;
    private final VirtualView virtualView;

    public ServerSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.virtualView = new VirtualView(socket);
    }

    public void run() {
        try {
            virtualView.start();
            while (true) {
                try {
                    controllerGame = ControllerConnection.ConnectionPlayers();
                    controllerGame.addView(virtualView);
                    break;
                } catch (FullGameException ignored) {}
            }
            virtualView.addObserver(controllerGame);
            virtualView.join();
            disconnect(virtualView.getNickname());
        } catch (InterruptedException e) {
            disconnect(virtualView.getNickname());
        }
    }

    private void closeConnections(){
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    private void disconnect(String nickName){
        controllerGame.quitGame(virtualView, nickName);
        closeConnections();
    }
}
