package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.view.modelView.CardView;

/**
 * Card is the abstract class which is inherited by DevelopmentCard or LeaderCard.
 */
public abstract class Card implements CardView {

    private final int victoryPoints;
    private final int cardID;

    public Card(int victoryPoints, int cardID) {
        this.victoryPoints = victoryPoints;
        this.cardID = cardID;
    }

    public int getVictoryPoints(){return victoryPoints;}

    @Override
    public int getCardID() {
        return cardID;
    }

    @Override
    public void print() {
        System.out.println("VICTORY_POINTS: " + victoryPoints + "\nCARD_ID: " + cardID + "\n");
    }

    @Override
    public void printSmallInfo() {
        System.out.println("VICTORY_POINTS: " + victoryPoints + "\n");
    }
}
