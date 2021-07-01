package it.polimi.ingsw.model.cards.developmentCards;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

import it.polimi.ingsw.model.cards.leaderCards.LeaderRequirements;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.resourceContainers.*;

/**
 * DevelopmentCard is one of 48 cards buyable by players.
 */
public class DevelopmentCard extends Card {

    private final Color color;
    private final int level;
    private final Cost resourceCost;
    private final Cost productionPowerResourceRequired;
    private final Cost productionPowerResourceGiven;
    private final int faithPointsGiven;

    public DevelopmentCard(Color color, int level, Cost resourceCost, int victoryPoints, Cost productionPowerResourceRequired,
                               Cost productionPowerResourceGiven, int faithPointsGiven, int cardID) {
        super(victoryPoints, cardID);
        this.color = color;
        this.level = level;
        this.resourceCost = resourceCost;
        this.productionPowerResourceRequired = productionPowerResourceRequired;
        this.productionPowerResourceGiven = productionPowerResourceGiven;
        this.faithPointsGiven = faithPointsGiven;
    }

    public Color getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @return true if player has enough resources to buy this card
     */
    public boolean isBuyable(Warehouse w, Strongbox s){
        return resourceCost.enoughResource(w, s);
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     *
     * this method allows to buy a new card from the deck.
     */
    public void buyCard(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException {
        controlDiscardResource(w,s,choice,false);
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @return the faith points of the card.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     *
     * this method active the production of a this card.
     */
    public int activateProduction(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException{
        controlDiscardResource(w,s,choice,true);
        productionPowerResourceGiven.increaseResource(s);
        return faithPointsGiven;
    }

    /**
     * @param s is a strongbox which summarize all resources of production powers given to the player.
     * @return how many faith points are given by the production power.
     */
    public int increaseProductionResource(Strongbox s){
        productionPowerResourceGiven.increaseResource(s);
        return faithPointsGiven;
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @param isPowerProduction if true indicate that i want to active a card. Else, i want to buy a card.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     *
     * this method control if there are enough resources in warehouse or strongbox. If yes, discard the resources.
     */
    public void controlDiscardResource(Warehouse w, Strongbox s, int choice, boolean isPowerProduction)
            throws InsufficientResourceException {
        Cost cost;
        if(isPowerProduction)cost=productionPowerResourceRequired;
        else cost=resourceCost;
        cost.decreaseResource(w,s,choice);
    }

    /**
     * @param resource the specific resource.
     * @return true if the operation of decreasing by 1 unit a selected resource is successful, else false.
     *
     * this method allows to decrease by 1 unit the amount of a selected resource.
     */
    public boolean discount(Resource resource){
        if(resourceCost.getNumOfResource(resource) > 0) {
            resourceCost.discount(resource);
            return true;
        }
        return false;
    }

    /**
     * @param resource indicates the resource that i want to decrease the amount of.
     *
     * this method allows to increase by 1 unit the amount of a selected resource.
     */
    public void recount(Resource resource){
        resourceCost.recount(resource);
    }

    public Cost getProductionPowerResourceGiven() {
        return productionPowerResourceGiven;
    }

    public Cost getProductionPowerResourceRequired() {
        return productionPowerResourceRequired;
    }

    public Cost getResourceCost() {
        return resourceCost;
    }

    public int getFaithPointsGiven() {
        return faithPointsGiven;
    }
}
