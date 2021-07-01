package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

/**
 * LeaderCard is one of 16 cards created at the beginning by Game. It has 4 standard methods which will be @Override
 * by the specific inherited card.
 */
public abstract class LeaderCard extends Card {

    private final Resource resource;
    private Cost resourceCost;
    private LeaderRequirements leaderRequirements;
    private boolean active;

    /**
     * @param resource stands for the effect resource of this card.
     * @param resourceCost stands for the resources required to activate the card.
     * @param victoryPoints stands for the victory given by activating this card.
     * @param cardID is the cardID.
     *
     * active is initialized at false.
     */
    public LeaderCard(Resource resource, Cost resourceCost, int victoryPoints, int cardID) {
        super(victoryPoints, cardID);
        this.resource = resource;
        this.resourceCost = resourceCost;
        this.active = false;
    }

    /**
     * @param resource stands for the effect resource of this card.
     * @param leaderRequirements stands for the resources required to activate the card.
     * @param victoryPoints stands for the victory given by activating this card.
     * @param cardID is the cardID.
     *
     * active is initialized at false.
     */
    public LeaderCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints, int cardID) {
        super(victoryPoints, cardID);this.resource = resource;
        this.leaderRequirements = leaderRequirements;
        this.active = false;
    }

    public Resource getResource() {
        return resource;
    }

    /**
     * @return 0 if LeaderCard is not active, otherwise @return its victory points.
     */
    public int getCurrentVictoryPoints() {
        if(isActive())
            return super.getVictoryPoints();
        return 0;
    }

    @Override
    public int getVictoryPoints() {
        return super.getVictoryPoints();
    }

    public boolean isActive() {
        return active;
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param l summarizes player's development cards.
     * @throws InsufficientResourceException if player has not enough resources.
     * @throws InsufficientCardsException if player has not enough cards.
     *
     * this method only evaluates if player has enough resources or enough cards, based on leaderCard requirement,
     * without doing anything.
     */
    public boolean activateCard(Warehouse w, Strongbox s, LeaderRequirements l)
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {
        if(isActive())
            throw new ActiveLeaderCardException();
        if(leaderRequirements == null) {
            if (resourceCost.enoughResource(w, s)) {
                this.active = true;
            } else {
                throw new InsufficientResourceException();
            }
        }
        else if(resourceCost == null){
            if(leaderRequirements.enoughCardRequirements(l)){
                this.active = true;
            }
            else{
                throw new InsufficientCardsException();
            }
        }
        return false;
    }

    public void cheatActivation(){
        this.active = true;
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased.
     * @param resource is player's choice about the extra resource given.
     * @return 0 in standard method.
     *
     * this is the standard method of additionalProductionPower() which will be @Override by the specific card.
     */
    public int additionalProductionPower(Warehouse w, Strongbox s, int choice, Resource resource) throws InsufficientResourceException, NoSuchProductionPowerException {
        throw new NoSuchProductionPowerException();
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources.
     *
     * this is the standard method of decreaseProductionPowerResources() which will be @Override by the specific card.
     */
    public void decreaseProductionPowerResources(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException, NoSuchProductionPowerException {
        throw new NoSuchProductionPowerException();
    }

    /**
     * @return false in standard method.
     *
     * this is the standard method of whiteConversion() which will be @Override by the specific card.
     */
    public boolean whiteConversion(){
        return false;
    }

    /**
     * @param card is DevelopmentCard to be decreased by 1.
     * @return false in standard method.
     *
     * this is the standard method of discount() which will be @Override by the specific card.
     */
    public boolean discount(DevelopmentCard card){
        return false;
    }

    /**
     * @param card is DevelopmentCard to be increased by 1.
     *
     * this is the standard method of recount() which will be @Override by the specific card.
     */
    public void recount(DevelopmentCard card){}

    @Override
    public Cost getResourceCost() {
        return resourceCost;
    }

    @Override
    public LeaderRequirements getLeaderRequirements() {
        return leaderRequirements;
    }
}
