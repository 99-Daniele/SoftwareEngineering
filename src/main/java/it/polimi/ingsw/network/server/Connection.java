package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;

import java.io.IOException;
import java.util.LinkedList;

public class Connection {

    private static LinkedList<ControllerGame> controllerGames = new LinkedList<>();
    private static final Object lock = new Object();

    public static synchronized ControllerGame ConnectionPlayers() throws IOException, InterruptedException {
        for(ControllerGame controllerGame: controllerGames){
            while(controllerGame.getMaxNumPlayers() == 0) {
                synchronized (lock) {
                    lock.wait();
                }
            }
            if(controllerGame.getCurrentNumPlayers() < controllerGame.getMaxNumPlayers()){
                return controllerGame;
            }
        }
        controllerGames.add(new ControllerGame());
        return controllerGames.getLast();
    }

    public static void newGame(){
        synchronized (lock) {
            lock.notify();
        }
    }
}