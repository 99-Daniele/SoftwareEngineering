package it.polimi.ingsw.model;

/**
 * WhiteConversionCard is a type of LeaderCard with the effect to convert a WhiteMarble into ResourceMarble.
 */
public class WhiteConversionCard extends LeaderCard{

    public WhiteConversionCard(Resource resource, Cost resourceCost, int victoryPoints) {
        super(resource, resourceCost, victoryPoints);
    }

    public WhiteConversionCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints) {
        super(resource, leaderRequirements, victoryPoints);
    }

    /**
     * @return true if the cart is active so the white marble can be converted.
     */
    @Override
    public boolean whiteConversion() {
        return isActive();
    }
}
