package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class WaitingRoom {

    private static ArrayList<VirtualView> waitingPlayers;

    private static void addNewPlayer(VirtualView virtualView){
        waitingPlayers.add(virtualView);
    }

    private static VirtualView getFirstVirtualView(){
        return waitingPlayers.remove(0);
    }
}
