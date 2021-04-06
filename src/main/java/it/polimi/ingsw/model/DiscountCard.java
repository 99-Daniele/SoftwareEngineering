package it.polimi.ingsw.model;

public class DiscountCard extends LeaderCard{

    public DiscountCard(Resource resource, Cost resourceCost, int victoryPoints) {
        super(resource, resourceCost, victoryPoints);
    }

    public DiscountCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints) {
        super(resource, leaderRequirements, victoryPoints);
    }

    /**
     * @param card is DevelopmentCard to be decreased by 1
     * @return true if card is correctly discounted, otherwise @return false
     */
    @Override
    public boolean discount(DevelopmentCard card) {
        return card.discount(this.getResource());
    }

    /**
     * @param card is DevelopmentCard to be increased by 1
     */
    @Override
    public void recount(DevelopmentCard card) {
       card.recount(this.getResource());
    }
}
