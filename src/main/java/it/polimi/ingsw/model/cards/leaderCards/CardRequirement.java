package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.view.CLI.ColorAnsi;

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

    /**
     * @return CLI representation of Card Requirement
     */
    @Override
    public String toString() {
        String s1 = numOfCards + "";
        String s2 = null;
        switch (color) {
            case BLUE:
                s2 = (ColorAnsi.ANSI_CYAN.escape() + " █ " + ColorAnsi.RESET);
                break;
            case GREEN:
                s2 = (ColorAnsi.ANSI_GREEN.escape() + " █ " + ColorAnsi.RESET);
                break;
            case PURPLE:
                s2 = (ColorAnsi.ANSI_PURPLE.escape() + " █ " + ColorAnsi.RESET);
                break;
            case YELLOW:
                s2 = (ColorAnsi.ANSI_YELLOW.escape() + " █ " + ColorAnsi.RESET);
                break;
        }
        String s3 = null;
        if(numOfCards > 1){
            switch (color) {
                case BLUE:
                    s3 = (ColorAnsi.ANSI_CYAN.escape() + "█" + ColorAnsi.RESET);
                    break;
                case GREEN:
                    s3 = (ColorAnsi.ANSI_GREEN.escape() + "█" + ColorAnsi.RESET);
                    break;
                case PURPLE:
                    s3 = (ColorAnsi.ANSI_PURPLE.escape() + "█" + ColorAnsi.RESET);
                    break;
                case YELLOW:
                    s3 = (ColorAnsi.ANSI_YELLOW.escape() + "█" + ColorAnsi.RESET);
                    break;
            }
        }
        else if(levels.get(0) == 2)
            s3 = "lvl 2";
        else
            s3 = "";
        return s1 + s2 + s3;
    }
}
