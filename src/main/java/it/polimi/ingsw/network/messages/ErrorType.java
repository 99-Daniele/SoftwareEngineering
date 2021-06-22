package it.polimi.ingsw.network.messages;

/**
 * ErrorType is an enumeration of all type of error messages sent from Server to Client.
 */
public enum ErrorType {

    ALREADY_TAKEN_NICKNAME,
    WRONG_PARAMETERS,
    NOT_YOUR_TURN,
    NOT_ENOUGH_RESOURCES,
    NOT_ENOUGH_CARDS,
    EMPTY_DECK,
    EMPTY_SLOT,
    FULL_SLOT,
    WRONG_POWER,
    ALREADY_ACTIVE_LEADER_CARD,
    ALREADY_DISCARD_LEADER_CARD,
    IMPOSSIBLE_SWITCH,
    ILLEGAL_OPERATION
}
