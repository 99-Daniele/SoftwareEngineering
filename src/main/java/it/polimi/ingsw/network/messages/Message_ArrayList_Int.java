package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class Message_ArrayList_Int extends Message{

    private ArrayList<Integer> params;

    public Message_ArrayList_Int(MessageType messageType, int clientID, ArrayList<Integer>params) {
        super(messageType, clientID);
        this.params = params;
    }

    public ArrayList<Integer> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return super.toString() + params;
    }
}
