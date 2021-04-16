package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * CardRequirement is a particular requirements about one type of color of cards. It has how much of color cards are needed
 * and which levels are required.
 */
public class CardRequirement {

    private final Color color;
    private int numOfCards;
    private final ArrayList<Integer> levels = new ArrayList<>();

    /**
     * @param color identifies the color of cards.
     * @param numOfCards identifies the required number of cards.
     * @param requiredLevel identifies the required level of cards.
     */
    public CardRequirement(Color color, int numOfCards, int requiredLevel) {
        this.color = color;
        this.numOfCards = numOfCards;
        this.levels.add(requiredLevel);
    }

    /**
     * @param card is a player's DevelopmentCard.
     */
    public CardRequirement(DevelopmentCard card) {
        this.color = card.getColor();
        this.numOfCards = 1;
        this.levels.add(card.getLevel());
    }

    /**
     * @return the color of the cards.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the number of cards.
     */
    public int getNumOfCards() {
        return numOfCards;
    }

    /**
     * this method increase the number of cards by 1 unit.
     */
    public void increaseNumOfCards(){
        numOfCards++;
    }

    /**
     * @param level is the level of added DevelopmentCard.
     */
    public void addLevel(int level) {
        levels.add(level);
    }

    /**
     * @param requiredLevel is the required level.
     * @return true if this.levels contains @param requiredLevel. If @param requiredLevel == 0 always @return true.
     */
    public boolean containsLevel(int requiredLevel) {
        if(requiredLevel == 0)
            return true;
        return levels.contains(requiredLevel);
    }

    /**
     * @param cardRequirement is the CardRequirement to compare.
     * @return true if @param CardRequirement has all required levels.
     */
    public boolean enoughLevels(CardRequirement cardRequirement){
        for (Integer level: levels){
            if(!(cardRequirement.containsLevel(level)))
                return false;
        }
        return true;
    }
}
