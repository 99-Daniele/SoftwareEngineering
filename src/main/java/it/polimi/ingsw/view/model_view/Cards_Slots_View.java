package it.polimi.ingsw.view.model_view;

import java.util.ArrayList;

public class Cards_Slots_View {

    private ArrayList<Integer> firstSlot;
    private ArrayList<Integer> secondSlot;
    private ArrayList<Integer> thirdSlot;
    private int firstLeaderCard;
    private int secondLeaderCard;

    public Cards_Slots_View(){
        firstSlot = new ArrayList<>();
        secondSlot = new ArrayList<>();
        thirdSlot = new ArrayList<>();
        firstLeaderCard = -1;
        secondLeaderCard = -1;
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        this.firstLeaderCard = firstLeaderCard;
        this.secondLeaderCard = secondLeaderCard;
    }

    public void addLeaderCard(int leaderCard){
        if(firstLeaderCard == -1)
            firstLeaderCard = leaderCard;
        else
            secondLeaderCard = leaderCard;
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

    public void print(){};
}
