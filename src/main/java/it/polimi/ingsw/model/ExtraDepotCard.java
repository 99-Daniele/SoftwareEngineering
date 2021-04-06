package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientCardsException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;

/**
 * ExtraDepotCard is a type of LeaderCard with the effect to give to the player an extra depot which can contains only
 * one type of resource. This ExtraDepot is inserted in Warehouse during the activation of the card
 */
public class ExtraDepotCard extends LeaderCard{

    public ExtraDepotCard(Resource resource, Cost resourceCost, int victoryPoints) {
        super(resource, resourceCost, victoryPoints);
    }

    public ExtraDepotCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints) {
        super(resource, leaderRequirements, victoryPoints);
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @param l summarizes player's development cards
     * @throws InsufficientResourceException if player has not enough resources
     * @throws InsufficientCardsException if player has not enough cards
     * this method firstly calls the super method activateCard() and then, if not exceptions are thrown, create a new
     * ExtraDepot in player's warehouse
     */
    @Override
    public void activateCard(Warehouse w, Strongbox s, LeaderRequirements l) throws InsufficientResourceException, InsufficientCardsException {
        super.activateCard(w, s, l);
        w.addExtraDepot(this.getResource());
    }
}
