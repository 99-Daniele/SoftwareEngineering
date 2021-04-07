package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * LeaderRequirements contains all CardRequirement requested, each one for any different type of color of cards
 */
public class LeaderRequirements {

    private ArrayList<CardRequirement> cardRequirements = new ArrayList<>();


    /**
     * @param color is the type of color required
     * @param numOfCards is the num of cards of @param color required
     * @param maxLevel is the max level of cards of @param color required
     */
    public void createCardRequirement(Color color, int numOfCards, int maxLevel) {
        CardRequirement cardRequirement = new CardRequirement(color, numOfCards, maxLevel);
        cardRequirements.add(cardRequirement);
    }

    /**
     * @param card is a player's developmentCard
     * this method evaluates if already exists CardRequirement with the same color of @param card and set new parameters,
     * otherwise create new CardRequirement
     */
    public void addCardRequirement(DevelopmentCard card){
        for(CardRequirement cardRequirement: cardRequirements){
            if(cardRequirement.getColor() == card.getColor()){
                cardRequirement.increaseNumOfCards();
                cardRequirement.setMaxLevel(card.getLevel());
                return;
            }
        }
        CardRequirement c = new CardRequirement(card);
        cardRequirements.add(c);
    }

    /**
     * @param leaderRequirements stands for player's set of cards
     * @return true if player has enough required cards, otherwise @return false
     * this method verifies if @param leaderRequirements has more numOfCards and maxLevel than this for each color CardRequirement
     */
    public boolean enoughCardRequirements(LeaderRequirements leaderRequirements){
        for(Color color: Color.values()){
            if(this.getNumOfCards(color) > leaderRequirements.getNumOfCards(color)
                && this.getMaxLevelOfCards(color) > leaderRequirements.getMaxLevelOfCards(color))
                return false;
        }
        return true;
    }

    /**
     * @param color stands for the color of cards to count
     * @return the count of cards of @param color
     */
    public int getNumOfCards(Color color){
       for(CardRequirement cardRequirement: cardRequirements) {
           if (cardRequirement.getColor() == color)
               return cardRequirement.getNumOfCards();
       }
       return 0;
    }

    /**
     * @param color stands for the color of cards to get maxLevel
     * @return the max level among cards of @param color
     */
    public int getMaxLevelOfCards(Color color){
        for(CardRequirement cardRequirement: cardRequirements) {
            if (cardRequirement.getColor() == color)
                return cardRequirement.getMaxLevel();
        }
        return 0;
    }
}
