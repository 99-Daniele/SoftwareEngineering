package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.CLI.ColorAnsi;

import java.util.ArrayList;
import java.util.LinkedList;

public class CardsSlotsView {

    private LinkedList<Integer> firstSlot;
    private LinkedList<Integer> secondSlot;
    private LinkedList<Integer> thirdSlot;
    private int firstLeaderCard;
    private boolean firstActive;
    private int secondLeaderCard;
    private boolean secondActive;

    public CardsSlotsView(){
        firstSlot = new LinkedList<>();
        secondSlot = new LinkedList<>();
        thirdSlot = new LinkedList<>();
        firstLeaderCard = -1;
        secondLeaderCard = -1;
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        this.firstLeaderCard = firstLeaderCard;
        firstActive = false;
        this.secondLeaderCard = secondLeaderCard;
        secondActive = false;
    }

    public void addLeaderCard(int leaderCard){
        if(firstLeaderCard == -1) {
            firstLeaderCard = leaderCard;
            firstActive = true;
        }
        else if(secondLeaderCard == -1){
            secondLeaderCard = leaderCard;
            secondActive = true;
        }
        else
            activateLeaderCard(leaderCard);
    }

    private void activateLeaderCard(int leaderCard){
        if(secondLeaderCard == leaderCard && !firstActive){
            int temp = firstLeaderCard;
            firstLeaderCard = secondLeaderCard;
            secondLeaderCard = temp;
        }
        else if(secondLeaderCard == leaderCard)
            secondActive = true;
        firstActive = true;
    }

    public ArrayList<Integer> getDevelopmentCards() {
        ArrayList<Integer> cards = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if(firstSlot.size() > i)
                cards.add(firstSlot.get(i));
            else
                cards.add(-1);
        }
        for(int i = 0; i < 3; i++){
            if(secondSlot.size() > i)
                cards.add(secondSlot.get(i));
            else
                cards.add(-1);
        }
        for(int i = 0; i < 3; i++){
            if(thirdSlot.size() > i)
                cards.add(thirdSlot.get(i));
            else
                cards.add(-1);
        }
        return cards;
    }

    public ArrayList<Integer> getLeaderCards(){
        ArrayList<Integer> leaderCards = new ArrayList<>();
        leaderCards.add(firstLeaderCard);
        leaderCards.add(secondLeaderCard);
        return leaderCards;
    }

    public boolean isLeaderCardActive(int leaderCard){
        if(leaderCard == 1)
            return firstActive;
        else
            return secondActive;
    }

    public boolean isSlotEmpty(int slot){
        switch (slot){
            case 1:
                return firstSlot.size() == 0;
            case 2:
                return secondSlot.size() == 0;
            case 3:
                return thirdSlot.size() == 0;
            default:
                return false;
        }
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
        if(firstLeaderCard == chosenLeaderCard){
            firstLeaderCard = secondLeaderCard;
            firstActive = secondActive;
        }
        secondLeaderCard = -1;
        secondActive = false;
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
        for(int i = slot.size() - 2; i >= 0; i--) {
            System.out.println("CARD " + (i+1) + ": ");
            CardMapCLI.getCard(slot.get(i)).printSmallInfo();
        }
    }

    public void printCliLeaderCard(){
        System.out.println("\nLEADER_CARDS: ");
        if(firstLeaderCard != -1) {
            CardMapCLI.getCard(firstLeaderCard).print();
            if(firstActive)
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_GREEN.escape() + "TRUE" + ColorAnsi.RESET);
            else
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_RED.escape() + "FALSE" + ColorAnsi.RESET);
        }
        if (secondLeaderCard != -1) {
            CardMapCLI.getCard(secondLeaderCard).print();
            if(secondActive)
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_GREEN.escape() + "TRUE" + ColorAnsi.RESET);
            else
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_RED.escape() + "FALSE" + ColorAnsi.RESET);
        }
        System.out.println("\n");
    }
}