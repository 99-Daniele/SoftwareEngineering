package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when try to insert DevelopmentCard in SlotDevelopmentCards which already contains
 * DevelopmentCard with equals or higher level or try to insert DevelopmentCard in SlotDevelopmentCards with higher
 * level than required level
 */
public class WrongDevelopmentCardsSlotException extends Exception{

    public WrongDevelopmentCardsSlotException(){
        super("Questo slot non può inserire questa carta");
    }
}
