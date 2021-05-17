package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class PlayerServer implements Runnable {

    private final Socket socket;
    private ControllerGame controllerGame;
    private final VirtualView virtualView;

    public PlayerServer(Socket socket) throws IOException {
        this.socket = socket;
        this.virtualView = new VirtualView(socket);
    }

    public void run() {
        try {
            virtualView.start();
            controllerGame = Connection.ConnectionPlayers();
            int viewID = controllerGame.addView(virtualView);
            virtualView.setViewID(viewID);
            virtualView.addObserver(controllerGame);
            virtualView.join();
            disconnect(viewID, virtualView.getNickname());
        } catch (IOException | InterruptedException e) {
            try {
                controllerGame.quitGame(virtualView.getNickname(), virtualView.getViewID());
            } catch (IOException ioException) { }
            finally {
                closeConnections();
            }
        }
    }

    private void closeConnections(){
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
        }
        finally {
            System.out.println("Closed Client connection");
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
