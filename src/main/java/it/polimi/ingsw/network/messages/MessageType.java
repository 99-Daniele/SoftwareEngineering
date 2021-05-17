package it.polimi.ingsw.network.messages;

/**
 * MessageType is an enumeration which represents all type of different Messages.
 */
public enum MessageType {

    LOGIN,
    NUM_PLAYERS,
    NEW_PLAYER,
    LEADER_CARD,
    ONE_FIRST_RESOURCE,
    TWO_FIRST_RESOURCE,
    PLAYERS,
    START_GAME,
    MARKET,
    DECKBOARD,
    TURN,
    BUY_CARD,
    CHOSEN_SLOT,
    CARD_REMOVE,
    RESOURCE_AMOUNT,
    TAKE_MARBLE,
    MARKET_CHANGE,
    USE_MARBLE,
    WHITE_CONVERSION_CARD,
    FAITH_POINTS_INCREASE,
    VATICAN_REPORT,
    INCREASE_WAREHOUSE,
    DEVELOPMENT_CARD_POWER,
    BASIC_POWER,
    LEADER_CARD_POWER,
    END_PRODUCTION,
    LEADER_CARD_ACTIVATION,
    LEADER_CARD_DISCARD,
    SWITCH_DEPOT,
    END_TURN,
    QUIT,
    END_GAME,
    PING,
    PONG,
    OK,
    ERR,
}
