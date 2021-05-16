package it.polimi.ingsw.exceptions;

public class WrongParametersException extends Exception{

    public WrongParametersException(){
        super("You have inserted wrong parameters");
    }
}
