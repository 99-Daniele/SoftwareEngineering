package it.polimi.ingsw.model;

/**
 * CardRequirement is a particular requirements about one type of color of cards. It has how much of color cards are needed
 * and which is their maxLevel required
 */
public class CardRequirement {

    private final Color color;
    private int numOfCards;
    private int maxLevel;

    public CardRequirement(Color color, int numOfCards, int maxLevel) {
        this.color = color;
        this.numOfCards = numOfCards;
        this.maxLevel = maxLevel;
    }

    public CardRequirement(Color color, int maxLevel) {
        this.color = color;
        this.numOfCards = 1;
        this.maxLevel = maxLevel;
    }

    public Color getColor() {
        return color;
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void increaseNumOfCards(){
        numOfCards++;
    }
}
