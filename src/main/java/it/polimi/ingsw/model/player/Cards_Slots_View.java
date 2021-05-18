package it.polimi.ingsw.model.player;

import java.util.ArrayList;

public class Cards_Slots_View {

    private ArrayList<Integer> firstSlot;
    private ArrayList<Integer> secondSlot;
    private ArrayList<Integer> thirdSlot;
    private int firstLeaderCard;
    private int secondLeaderCard;

    public Cards_Slots_View(int firstLeaderCard, int secondLeaderCard){
        this.firstLeaderCard = firstLeaderCard;
        this.secondLeaderCard = secondLeaderCard;
    }

    public void addDevelopmentCard(int cardID, int slot){
        if(slot == 1)
            firstSlot.add(cardID);
        else if(slot == 2)
            secondSlot.add(cardID);
        else
            thirdSlot.add(cardID);
    }

    public void discardLeaderCard(int chosenLeaderCard){
        if(chosenLeaderCard == 1){
            firstLeaderCard = secondLeaderCard;
        }
        secondLeaderCard = -1;
    }
}
