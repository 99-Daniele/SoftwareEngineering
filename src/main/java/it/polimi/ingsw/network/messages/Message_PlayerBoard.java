package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.PlayerBoard;

public class Message_PlayerBoard extends Message{

    private final PlayerBoard playerBoard;

    public Message_PlayerBoard(MessageType messageType, int clientID, PlayerBoard playerBoard) {
        super(messageType, clientID);
        this.playerBoard = playerBoard;
    }
}
