package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.resourceContainers.*;

/**
 * DiscountCard is a type of LeaderCard with the effect to discount by 1 specific resource the buying of a DevelopmentCard.
 */
public class DiscountCard extends LeaderCard {

    public DiscountCard(Resource resource, Cost resourceCost, int victoryPoints, int cardID) {
        super(resource, resourceCost, victoryPoints, cardID);
    }

    public DiscountCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints, int cardID) {
        super(resource, leaderRequirements, victoryPoints, cardID);
    }

    /**
     * @param card is the DevelopmentCard to be decreased by 1.
     * @return true if card is effectively discounted, otherwise @return false.
     */
    @Override
    public boolean discount(DevelopmentCard card) {
        if(this.isActive())
            return card.discount(this.getResource());
        return false;
    }

    /**
     * @param card is DevelopmentCard to be increased by 1.
     *
     * this method can only be called after a discount() call.
     */
    @Override
    public void recount(DevelopmentCard card) {
       card.recount(this.getResource());
    }

    @Override
    public void print() {
        System.out.println("\nDISCOUNT_CARD");
        super.print();
    }
}
