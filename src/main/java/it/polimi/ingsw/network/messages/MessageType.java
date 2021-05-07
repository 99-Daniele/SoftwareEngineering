package it.polimi.ingsw.network.messages;

/**
 * MessageType is an enumeration which represents all type of different Messages.
 */
public enum MessageType {

    LOGIN,
    NUM_PLAYERS,
    START_GAME,
    TURN,
    BUY_CARD,
    SWITCH_DEPOT,
    QUIT,
    END_GAME,
    PING,
    OK,
    ERR
}
