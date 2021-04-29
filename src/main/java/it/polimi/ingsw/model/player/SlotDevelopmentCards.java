package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import it.polimi.ingsw.exceptions.NoSuchProductionPowerException;
import it.polimi.ingsw.model.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.leaderCards.LeaderRequirements;

import java.util.LinkedList;

/**
 * SlotDevelopmentCards is one od the 3 slots that each player has on his PlayerBoard.
 * it can contains up to 3 DevelopmentCards with increasing level.
 */
public class SlotDevelopmentCards {

    private final LinkedList<DevelopmentCard> developmentCards = new LinkedList<>();
    private boolean empty;

    public SlotDevelopmentCards() {
        empty = true;
    }

    private boolean isEmpty() {
        return empty;
    }

    /**
     * @param card is the DevelopmentCard to be added.
     * @return false if @param card has not required level, otherwise @return true.
     */
    public boolean haveRequiredLevel(DevelopmentCard card){
        return (getRequiredLevel() == card.getLevel());
    }

    /**
     * @param card is the DevelopmentCard to be added.
     * @return false if @param card can't be added because has not the required level.
     */
    public boolean addDevelopmentCard(DevelopmentCard card){
        if(!haveRequiredLevel(card))
            return false;
        developmentCards.add(card);
        empty = false;
        return true;
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice is player's choice.
     * @return faith points given by activate production power of last added card.
     * @throws InsufficientResourceException if player has not enough resources.
     * this method calls activateProduction() of the last added card in SlotDevelopmentCards.
     */
    public int activateProductionActiveCard(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException, NoSuchProductionPowerException {
        if(isEmpty())
            throw new NoSuchProductionPowerException();
        return developmentCards.getLast().activateProduction(w, s, choice);
    }

    /**
     *
     */
    public int increaseProductionPowerResource(Strongbox s){
        return developmentCards.getLast().increaseProductionResource(s);
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @throws InsufficientResourceException if there are not enough resource to activate production power.
     */
    public void removeProductionPowerResource(Warehouse w, Strongbox s)
            throws InsufficientResourceException {
        if(isEmpty())
            return;
        developmentCards.getLast().controlDiscardResource(w, s, 1, true);
    }

    /**
     *
     */
    public void removeProductionPowerResource(Warehouse w, Strongbox s, int choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
        if(isEmpty())
            throw new NoSuchProductionPowerException();
        developmentCards.getLast().controlDiscardResource(w, s, choice, true);
    }

    /**
     * @return 1 if slotDevelopmentCards is empty, or @return level +1 of last added DevelopmentCard.
     * this method @return the required level of a DevelopmentCard which can be added in SlotDevelopmentCards.
     */
    private int getRequiredLevel(){
        if(isEmpty())
            return 1;
        return developmentCards.getLast().getLevel() +1;
    }

    /**
     * @param leaderRequirements is current player's summary of owned cards.
     * this method calls addCardRequirement method of LeaderRequirements for each DevelopmentCard in SlotDevelopmentCards.
     */
    public void updateLeaderRequirements(LeaderRequirements leaderRequirements){
        for (DevelopmentCard card: developmentCards)
            leaderRequirements.addCardRequirement(card);
    }

    public int getNumOfCards(){
        return developmentCards.size();
    }

    /**
     * @return the sum of victory points of all cards in SlotDevelopmentCards.
     */
    public int sumTotalVictoryPointsByCards(){
        int sum = 0;
        for(DevelopmentCard card: developmentCards)
            sum += card.getVictoryPoints();
        return sum;
    }
}
