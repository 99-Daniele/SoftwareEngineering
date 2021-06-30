package it.polimi.ingsw.exceptions;

/**
 * this exception is thrown when player tries to perform an illegal operation in that state of the game.
 */
public class IllegalStateException extends Exception{

    public IllegalStateException(){
        super("You can't do this operation at this moment");
    }
}
