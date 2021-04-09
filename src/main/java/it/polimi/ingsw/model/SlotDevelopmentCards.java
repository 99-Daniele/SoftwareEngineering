package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import it.polimi.ingsw.exceptions.WrongDevelopmentCardsSlotException;

import java.util.LinkedList;

public class SlotDevelopmentCards {

    private LinkedList<DevelopmentCard> developmentCards = new LinkedList<>();

    /**
     * @param card is the DevelopmentCard to be added
     * @throws WrongDevelopmentCardsSlotException if @param card has not required level
     */
    public void addDevelopmentCard(DevelopmentCard card)  throws WrongDevelopmentCardsSlotException {
        if(getRequiredLevel() != card.getLevel())
            throw new WrongDevelopmentCardsSlotException();
        developmentCards.add(card);
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @param choice is player's choice
     * @return faith points given by production power of active card
     * @throws InsufficientResourceException if player has not enough resources
     * this method calls activateProduction() of the last added card in SlotDevelopmentCards
     */
    public int activateProductionActiveCard(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException{
        return developmentCards.getLast().activateProduction(w, s, choice);
    }

    /**
     * @return 1 if slotDevelopmentCards is empty, or @return level +1 of last added DevelopmentCard
     * this method @return the required level of a DevelopmentCard which can be added in SlotDevelopmentCards
     */
    private int getRequiredLevel(){
        if(developmentCards.size() == 0)
            return 1;
        return developmentCards.getLast().getLevel() +1;
    }

    /**
     * @param leaderRequirements is current player's summary of owned cards
     * this method calls addCardRequirement method of LeaderRequirements for each DevelopmentCard in SlotDevelopmentCards
     */
    public void updateLeaderRequirements(LeaderRequirements leaderRequirements){
        for (DevelopmentCard card: developmentCards)
            leaderRequirements.addCardRequirement(card);
    }

    /**
     * @return the sum of victory points of all cards in SlotDevelopmentCards
     */
    public int sumTotalVictoryPointsByCards(){
        int sum = 0;
        for(DevelopmentCard card: developmentCards)
            sum += card.getVictoryPoints();
        return sum;
    }
}
