package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientCardsException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;

/**
 * LeaderCard is one of 16 cards created at the beginning by Game. It has 4 standard methods which will be @Override
 * by the specific inherited card
 */
public abstract class LeaderCard {

    private final Resource resource;
    private Cost resourceCost;
    private LeaderRequirements leaderRequirements;
    private final int victoryPoints;
    private boolean active;

    /**
     * @param resource stands for the effect resource of this card
     * @param resourceCost stands for the resources required to activate the card
     * @param victoryPoints stands for the victory given by activating this card
     * activate is initialized at false.
     * leaderRequirements remains null
     */
    public LeaderCard(Resource resource, Cost resourceCost, int victoryPoints) {
        this.resource = resource;
        this.resourceCost = resourceCost;
        this.victoryPoints = victoryPoints;
        this.active = false;
    }

    /**
     * @param resource stands for the effect resource of this card
     * @param leaderRequirements stands for the resources required to activate the card
     * @param victoryPoints stands for the victory given by activating this card
     * activate is initialized at false.
     * resourceCost remains null
     */
    public LeaderCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints) {
        this.resource = resource;
        this.leaderRequirements = leaderRequirements;
        this.victoryPoints = victoryPoints;
        this.active = false;
    }

    public Resource getResource() {
        return resource;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @param l summarizes player's development cards
     * @throws InsufficientResourceException if player has not enough resources
     * @throws InsufficientCardsException if player has not enough cards
     * this method only evaluates if player has enough resources or enough cards, based on leaderCard requirement,
     * without doing anything.
     */
    public void activateCard(Warehouse w, Strongbox s, LeaderRequirements l)
            throws InsufficientResourceException, InsufficientCardsException{
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
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased
     * @param resource is player's choice about the extra resource given
     * @return 0 in standard method
     * this is the standard method of additionalProductionPower() which will be @Override by the specific card
     */
    public int additionalProductionPower(Warehouse w, Strongbox s, int choice, Resource resource) throws InsufficientResourceException{
        return 0;
    }

    /**
     * @return Resource.WHITE in standard method
     * this is the standard method of whiteConversion() which will be @Override by the specific card
     */
    public Resource whiteConversion(){
        return Resource.WHITE;
    }

    /**
     * @param card is DevelopmentCard to be decreased by 1
     * @return false in standard method
     * this is the standard method of discount() which will be @Override by the specific card
     */
    public boolean discount(DevelopmentCard card){
        return false;
    }

    /**
     * @param card is DevelopmentCard to be increased by 1
     * this is the standard method of recount() which will be @Override by the specific card
     */
    public void recount(DevelopmentCard card){}
}
