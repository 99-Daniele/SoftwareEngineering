package it.polimi.ingsw.model;

/**
 * CardRequirement is a particular requirements about one type of color of cards. It has how much of color cards are needed
 * and which is their maxLevel required
 */
public class CardRequirement {

    private final Color color;
    private int numOfCards;
    private int maxLevel;

    /**
     *
     * @param color identifies the color of the Cards
     * @param numOfCards identifies the number of cards.
     * @param maxLevel identifies the max level of cards.
     */
    public CardRequirement(Color color, int numOfCards, int maxLevel) {
        this.color = color;
        this.numOfCards = numOfCards;
        this.maxLevel = maxLevel;
    }

    /**
     *
     * @param color identifies the color of the Cards
     * @param maxLevel identifies the max level of cards.
     */
    public CardRequirement(Color color, int maxLevel) {
        this.color = color;
        this.numOfCards = 1;
        this.maxLevel = maxLevel;
    }

    /**
     * @return the color of the cards
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the number of cards
     */
    public int getNumOfCards() {
        return numOfCards;
    }

    /**
     * @return the max Level among the cards.
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * @param maxLevel identifies the max level of the card.
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * this method increase the number of cards by 1 unit.
     */
    public void increaseNumOfCards(){
        numOfCards++;
    }
}
