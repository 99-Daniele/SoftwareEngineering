package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to do an operation giving wrong parameters.
 */
public class WrongParametersException extends Exception{

    public WrongParametersException(){
        super("You have inserted wrong parameters");
    }
}
