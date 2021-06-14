package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to do an operation without having not enough resources.
 */
public class InsufficientResourceException extends Exception{

    public InsufficientResourceException(){
        super("You don't have enough resources to do this operation.");
    }
}
