package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.model.resourceContainers.*;

/**
 * WhiteConversionCard is a type of LeaderCard with the effect to convert a WhiteMarble into ResourceMarble.
 */
public class WhiteConversionCard extends LeaderCard {

    public WhiteConversionCard(Resource resource, Cost resourceCost, int victoryPoints, int cardID) {
        super(resource, resourceCost, victoryPoints, cardID);
    }

    public WhiteConversionCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints, int cardID) {
        super(resource, leaderRequirements, victoryPoints, cardID);
    }

    /**
     * @return true if the cart is active so the white marble can be converted.
     */
    @Override
    public boolean whiteConversion() {
        return isActive();
    }

    @Override
    public void print() {
        System.out.println("\nWHITE_CONVERSION_CARD");
        super.print();
    }
}
