package it.polimi.ingsw.controller;

import java.util.LinkedList;

/**
 * ControllerConnection handle the connection between client and server matching each View with the right Controller.
 */
public class ControllerConnection {

    private static LinkedList<ControllerGame> controllerGames = new LinkedList<>();
    private static final Object lock = new Object();

    /**
     * @return the matched ControllerGame
     * @throws InterruptedException if a waiting thread is interrupted.
     * When a view tries to connect to Server, the ControllerConnection finds if there is any game where the number of
     * player is not maxed, or if there is any game where the number of players has not yet been chosen.
     */
    public static ControllerGame ConnectionPlayers() throws InterruptedException {
        for (ControllerGame controllerGame : controllerGames) {
            while (controllerGame.getCurrentNumPlayers() == 1 && controllerGame.getMaxNumPlayers() == 0) {
                synchronized (lock) {
                    lock.wait();
                }
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
    public static void newGame() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}