package it.polimi.ingsw.exceptions;

public class InsufficientCardsException extends Exception{

    public InsufficientCardsException(){
        super("You don't have enough cards to do this operation");
    }
}