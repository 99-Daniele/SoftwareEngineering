package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.FullGameException;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.Socket;

/**
 * ServerSocket handle Client - Server socket connection and then create a new VirtualView for Client and associates
 * to correct ControllerGame.
 */
public class ServerSocket implements Runnable {

    private final Socket socket;
    private ControllerGame controllerGame;
    private final VirtualView virtualView;
    private Object lock;

    public ServerSocket(Socket socket, Object lock) throws IOException {
        this.socket = socket;
        this.virtualView = new VirtualView(socket);
        this.lock=lock;
    }

    /**
     * start VirtualView and then find its associated ControllerGame. Then add ControllerGame to VirtualView observers.
     */
    public void run() {
        try {
            virtualView.start();
            while (true) {
                try {
                    synchronized (lock) {
                        controllerGame = ControllerConnection.ConnectionPlayers();
                        controllerGame.addView(virtualView);
                    }
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

    /**
     * close Socket CConnection if not previously closed.
     */
    private void closeConnections(){
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * @param nickName is player nickName.
     *
     * inform ControllerGame that player has disconnected from Game and then close Socket connection.
     */
    private void disconnect(String nickName){
        controllerGame.quitGame(virtualView, nickName);
        closeConnections();
    }
}
