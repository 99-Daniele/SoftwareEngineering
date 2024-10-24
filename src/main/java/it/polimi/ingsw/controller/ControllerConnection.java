package it.polimi.ingsw.controller;

import java.util.LinkedList;

/**
 * ControllerConnection handle the connection between client and server matching each View with the right Controller.
 */
public class ControllerConnection {

    private static final LinkedList<ControllerGame> controllerGames = new LinkedList<>();

    /**
     * @return the matched ControllerGame
     * @throws InterruptedException if a waiting thread is interrupted.
     *
     * When a view tries to connect to Server, the ControllerConnection finds if there is any game where the number of
     * player is not maxed, or if there is any game where the number of players has not yet been chosen.
     */
    public static synchronized ControllerGame ConnectionPlayers() throws InterruptedException {
        for (ControllerGame controllerGame : controllerGames) {
            while (controllerGame.getCurrentNumPlayers() == 1 && controllerGame.getMaxNumPlayers() == 0) {
                ControllerConnection.class.wait();
            }
            if (controllerGame.getCurrentNumPlayers() < controllerGame.getMaxNumPlayers())
                return controllerGame;
        }
        controllerGames.add(new ControllerGame());
        return controllerGames.getLast();
    }

    /**
     * this method is called by ControllerGame when first player decides the number of players.
     */
    public synchronized static void newGame() {
        ControllerConnection.class.notifyAll();
    }
}