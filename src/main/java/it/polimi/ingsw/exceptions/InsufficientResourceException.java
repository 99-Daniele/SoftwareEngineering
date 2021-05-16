package it.polimi.ingsw.exceptions;

public class InsufficientResourceException extends Exception{

    public InsufficientResourceException(){
        super("You don't have enough resources to do this operation.");
    }
}
