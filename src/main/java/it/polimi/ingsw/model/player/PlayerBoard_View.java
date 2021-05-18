package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.depots.Warehouse_Strongbox_View;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class PlayerBoard_View {

    private final String nickName;
    private Warehouse_Strongbox_View warehouse_strongbox;
    private Cards_Slots_View cards_slots;
    private int victoryPoints;

    public PlayerBoard_View(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
