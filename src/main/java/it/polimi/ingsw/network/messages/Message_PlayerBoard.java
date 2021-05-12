package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.player.PlayerBoard_Read_Only;

public class Message_PlayerBoard extends Message{

    private final PlayerBoard_Read_Only playerBoard;

    public Message_PlayerBoard(MessageType messageType, int clientID, PlayerBoard_Read_Only playerBoard) {
        super(messageType, clientID);
        this.playerBoard = playerBoard;
    }
}
