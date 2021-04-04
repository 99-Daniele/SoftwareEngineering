package it.polimi.ingsw.exceptions;

public class InsufficientResourceException extends Exception{

    public InsufficientResourceException(){
        super("Non hai abbastanza risorse per effettuare questa operazione");
    }
}
