package it.polimi.ingsw.exceptions;

public class IllegalStateException extends Exception{

    public IllegalStateException(){
        super("You can't do this operation at this moment");
    }
}
