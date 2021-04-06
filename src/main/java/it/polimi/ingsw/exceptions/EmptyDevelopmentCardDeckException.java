package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to buy DevelopmentCard from an empty Deck
 */
public class EmptyDevelopmentCardDeckException extends Exception{

    public EmptyDevelopmentCardDeckException(){
        super("Questo mazzetto è vuoto");
    }
}
