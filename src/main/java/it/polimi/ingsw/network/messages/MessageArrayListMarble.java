package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

public class MessageArrayListMarble extends Message{

    private final ArrayList<Marble> marbles;

    public MessageArrayListMarble(MessageType messageType, int clientID, ArrayList<Marble> marbles) {
        super(messageType, clientID);
        this.marbles = marbles;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    @Override
    public String toString() {
        return super.toString() + marbles;
    }
}
