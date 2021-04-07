package it.polimi.ingsw.exceptions;

public class WrongParametersException extends Exception{

    public WrongParametersException(){
        super("Questi parametri non sono validi");
    }
}
