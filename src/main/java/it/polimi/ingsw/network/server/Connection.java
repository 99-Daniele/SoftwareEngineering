package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.PosControllerGame;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connection {

    private static LinkedList<ControllerGame> controllerGames = new LinkedList<>();

    public static synchronized ControllerGame ConnectionPlayers() throws IOException{
        for(ControllerGame controllerGame: controllerGames){
            if(controllerGame.getCurrentNumPlayers() < controllerGame.getMaxNumPlayers()){
                return controllerGame;
            }
        }
        controllerGames.add(new ControllerGame());
        return controllerGames.getLast();
    }

}