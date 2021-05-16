package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.market.Marble;

import java.util.ArrayList;

public class Message_ArrayList_Marble extends Message{

    private ArrayList<Marble> marbles;

    public Message_ArrayList_Marble(MessageType messageType, int clientID, ArrayList<Marble> marbles) {
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
