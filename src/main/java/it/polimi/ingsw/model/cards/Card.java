package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.view.model_view.Card_View;

public class Card implements Card_View {

    private final int victoryPoints;
    private final int cardID;

    public Card(int victoryPoints, int cardID) {
        this.victoryPoints = victoryPoints;
        this.cardID = cardID;
    }

    public int getVictoryPoints(){return victoryPoints;}

    public int getCardID() {
        return cardID;
    }

    @Override
    public void print() {
        System.out.println("VICTORY_POINTS: " + victoryPoints + "\nCARD_ID: " + cardID + "\n\n");
    }
}
