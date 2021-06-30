package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.model.cards.developmentCards.*;

import java.util.ArrayList;

/**
 * LeaderRequirements contains all CardRequirement requested, each one for any different color of cards.
 */
public class LeaderRequirements {

    private final ArrayList<CardRequirement> cardRequirements = new ArrayList<>();

    /**
     * @param color is the type of color required.
     * @param numOfCards is the num of cards of @param color required.
     * @param requiredLevel is the level of card of @param color required.
     */
    public void createCardRequirement(Color color, int numOfCards, int requiredLevel) {
        CardRequirement cardRequirement = new CardRequirement(color, numOfCards, requiredLevel);
        cardRequirements.add(cardRequirement);
    }

    /**
     * @param card is a player's developmentCard.
     *
     * this method evaluates if already exists CardRequirement with the same color of @param card and set new parameters,
     * otherwise create new CardRequirement.
     */
    public void addCardRequirement(DevelopmentCard card){
        for(CardRequirement cardRequirement: cardRequirements){
            if(cardRequirement.getColor() == card.getColor()){
                cardRequirement.increaseNumOfCards();
                cardRequirement.addLevel(card.getLevel());
                return;
            }
        }
        CardRequirement c = new CardRequirement(card);
        cardRequirements.add(c);
    }

    /**
     * @param leaderRequirements stands for player's set of cards.
     * @return true if player has enough required cards, otherwise @return false.
     *
     * this method verifies if @param leaderRequirements has more numOfCards than this for each color CardRequirement
     * and contains all required levels.
     */
    public boolean enoughCardRequirements(LeaderRequirements leaderRequirements){
        for(Color color: Color.values()){
            if(leaderRequirements.getNumOfCards(color) < this.getNumOfCards(color)
                || !(this.getCardRequirement(color).enoughLevels(leaderRequirements.getCardRequirement(color))))
                return false;
        }
        return true;
    }

    /**
     * @param color stands for the color of cards to count.
     * @return the count of cards of @param color.
     */
    public int getNumOfCards(Color color){
       for(CardRequirement cardRequirement: cardRequirements) {
           if (cardRequirement.getColor() == color)
               return cardRequirement.getNumOfCards();
       }
       return 0;
    }

    /**
     * @param color stands for the color of CardRequirement to find.
     * @return the CardRequirement whit @param color, or if not exist an empty CardRequirement.
     */
    public CardRequirement getCardRequirement(Color color){
        for(CardRequirement cardRequirement: cardRequirements) {
            if (cardRequirement.getColor() == color)
                return cardRequirement;
        }
        return new CardRequirement(color, 0, 0);
    }

    @Override
    public String toString() {
        return cardRequirements.toString();
    }
}
