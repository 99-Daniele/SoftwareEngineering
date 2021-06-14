package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to activate Leader card without having not enough Development Cards.
 */
public class InsufficientCardsException extends Exception{

    public InsufficientCardsException(){
        super("You don't have enough cards to do this operation");
    }
}