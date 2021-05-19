package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.player.depots.Warehouse_Strongbox_View;
import it.polimi.ingsw.view.model_view.Cards_Slots_View;

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
