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
    ERR,
    LEADERCARD_ACTIVATION,
    LEADERCARD_DISCARD,
    ADDITIONAL_PRODUCTION,
    PRODUCTION_DEVCARD,
    BASIC_PRODUCTION,
    USE_MARBLE,
    TAKE_MARBLE,
    LEADERCARD_INIT,
    SWITCH_REPLY,
    AVAILABLE_SWITCH_REPLY
}
