package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.CLI.CLIPrinter;
import it.polimi.ingsw.view.CLI.ColorAnsi;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * CardSlotView is the View version of SlotDevelopmentCards Model class.
 * In this case all LeaderCards and DevelopmentCards are coded by Integer.
 */
public class CardsSlotsView {

    private final LinkedList<Integer> firstSlot;
    private final LinkedList<Integer> secondSlot;
    private final LinkedList<Integer> thirdSlot;
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
        firstActive = false;
        secondActive = false;
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        this.firstLeaderCard = firstLeaderCard;
        this.secondLeaderCard = secondLeaderCard;
    }

    /**
     * @param leaderCard is one LeaderCard.
     *
     * add @param leaderCard to player LeaderCards and activate the chosen one.
     */
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

    /**
     * @param leaderCard is one LeaderCard
     *
     * if the first LeaderCard was already activated, activate the second one.
     * if the first LeaderCard is not active and player has chosen to activate the second one, switch the LeaderCards
     * and activate the first one (which was the second one before).
     */
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

    /**
     * @return a list of 9 player's DevelopmentCards.
     *
     * for any missing DevelpmentCards add "-1".
     */
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

    /**
     * @return player's LeaderCards.
     */
    public ArrayList<Integer> getLeaderCards(){
        ArrayList<Integer> leaderCards = new ArrayList<>();
        leaderCards.add(firstLeaderCard);
        leaderCards.add(secondLeaderCard);
        return leaderCards;
    }

    /**
     * @param leaderCard is one player's LeaderCard (1 or 2).
     * @return if @param leaderCard is active.
     */
    public boolean isLeaderCardActive(int leaderCard){
        if(leaderCard == 1)
            return firstActive;
        else
            return secondActive;
    }

    /**
     * @param leaderCard is one player's LeaderCard (1 or 2).
     * @return if @param leaderCard is an active AdditionalProductionPowerCard.
     */
    public boolean isAdditionalPowerCard(int leaderCard){
        if(leaderCard == 1)
            return firstLeaderCard >= 61 && firstActive;
        else
            return secondLeaderCard >= 61 && secondActive;
    }

    /**
     * @param slot is one player's slot (1 or 3).
     * @return if @param slot is empty.
     */
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

    /**
     * @param cardID is DevelopmentCard's cardID.
     * @param slot is player's chosen slot to insert the card.
     */
    public void addDevelopmentCard(int cardID, int slot){
        if(slot == 1)
            firstSlot.add(cardID);
        else if(slot == 2)
            secondSlot.add(cardID);
        else
            thirdSlot.add(cardID);
    }

    /**
     * @param chosenLeaderCard is one player's LeaderCard (1 or 2).
     *
     * in case player has chosen to discard the first LeaderCard, switch the LeaderCards and set the second one as "-1".
     */
    public void discardLeaderCard(int chosenLeaderCard){
        if(firstLeaderCard == chosenLeaderCard){
            firstLeaderCard = secondLeaderCard;
            firstActive = secondActive;
        }
        secondLeaderCard = -1;
        secondActive = false;
    }

    /**
     * CLI representation of CardsSlotView.
     */
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

    /**
     * @param slot is one slot.
     *
     * print DevelopmentCards of @param slot. In case there are more than one, the last one is printed entirely while
     * the other ones only small information (color and victory points).
     */
    private void printSlot(LinkedList <Integer> slot){
        CLIPrinter.printDevelopmentCard(slot.getLast());
        for(int i = slot.size() - 2; i >= 0; i--) {
            System.out.println("CARD " + (i+1) + ": ");
            CLIPrinter.printDevelopmentCardSmallInfo(slot.getLast());
        }
    }

    /**
     * print player's LeaderCards.
     */
    public void printCliLeaderCard(){
        System.out.println("\nLEADER_CARDS: ");
        if(firstLeaderCard != -1) {
            CLIPrinter.printLeaderCard(firstLeaderCard);
            if(firstActive)
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_GREEN.escape() + "TRUE" + ColorAnsi.RESET);
            else
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_RED.escape() + "FALSE" + ColorAnsi.RESET);
        }
        if (secondLeaderCard != -1) {
            CLIPrinter.printLeaderCard(secondLeaderCard);
            if(secondActive)
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_GREEN.escape() + "TRUE" + ColorAnsi.RESET);
            else
                System.out.println("ACTIVE: " + ColorAnsi.ANSI_RED.escape() + "FALSE" + ColorAnsi.RESET);
        }
        System.out.println("\n");
    }
}
