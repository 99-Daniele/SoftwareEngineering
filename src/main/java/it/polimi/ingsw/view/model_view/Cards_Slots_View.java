package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.parser.CardMapCLI;

import java.util.ArrayList;
import java.util.LinkedList;

public class Cards_Slots_View {

    private LinkedList<Integer> firstSlot;
    private LinkedList<Integer> secondSlot;
    private LinkedList<Integer> thirdSlot;
    private int firstLeaderCard;
    private int secondLeaderCard;

    public Cards_Slots_View(){
        firstSlot = new LinkedList<>();
        secondSlot = new LinkedList<>();
        thirdSlot = new LinkedList<>();
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

    public ArrayList<Integer> getDevelopmentCards() {
        ArrayList<Integer> activeCards = new ArrayList<>();
        if(firstSlot.size() > 0)
            activeCards.add(firstSlot.getLast());
        if(secondSlot.size() > 0)
            activeCards.add(secondSlot.getLast());
        if(thirdSlot.size() > 0)
            activeCards.add(firstSlot.getLast());
        return activeCards;
    }

    public int getLeaderCard(int leaderCard){
       if(leaderCard == 1)
           return firstLeaderCard;
       else
           return secondLeaderCard;
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

    public void printCliSlot(){
        System.out.println("\nDEVELOPMENT_CARDS:");
        System.out.println("First slot:");
        if (firstSlot.size()>0) {
            printSlot(firstSlot);
        }
        System.out.println("Second slot:");
        if (secondSlot.size()>0) {
            printSlot(secondSlot);
        }
        System.out.println("Third slot:");
        if (thirdSlot.size()>0) {
            printSlot(thirdSlot);
        }
    }

    private void printSlot(LinkedList <Integer> slot){
        CardMapCLI.getCard(slot.getLast()).print();
        for(int i = slot.size() - 1; i > 0; i--) {
            System.out.println("CARD " + i + ": ");
            CardMapCLI.getCard(slot.get(i)).printSmallInfo();
        }
    }

    public void printCliLeaderCard(){
        System.out.println("\nLEADER_CARDS: ");
        if(firstLeaderCard != -1)
            CardMapCLI.getCard(firstLeaderCard).print();
        if (secondLeaderCard != -1)
            CardMapCLI.getCard(secondLeaderCard).print();
    }
}
