package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when there are no available slots to insert DevelopmentCard
 */
public class ImpossibleDevelopmentCardAdditionException extends Exception{

    public ImpossibleDevelopmentCardAdditionException(){
        super("You have not available slots to buy this card");
    }
}
