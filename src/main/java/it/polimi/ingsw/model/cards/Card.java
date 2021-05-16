package it.polimi.ingsw.model.cards;

public class Card {

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
}
