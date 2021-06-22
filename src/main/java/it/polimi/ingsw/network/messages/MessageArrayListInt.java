package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

/**
 * MessageArrayListInt is a type of Message with an Integer ArrayList param.
 */
public class MessageArrayListInt extends Message{

    private final ArrayList<Integer> params;

    public MessageArrayListInt(MessageType messageType, int clientID, ArrayList<Integer>params) {
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
