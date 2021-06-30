package it.polimi.ingsw.exceptions;

/**
 * this exception is thrown when a player tries to enter in a game already fulled of players.
 */
public class FullGameException extends Exception{

    public FullGameException(){super("This game is full of players");}
}
