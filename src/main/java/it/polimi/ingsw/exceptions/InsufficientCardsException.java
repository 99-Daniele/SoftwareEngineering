package it.polimi.ingsw.exceptions;

public class InsufficientCardsException extends Exception{

    public InsufficientCardsException(){
        super("Non hai abbastanza carte per effettuare questa operazione");
    }
}