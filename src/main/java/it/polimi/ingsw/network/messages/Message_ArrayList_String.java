package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class Message_ArrayList_String extends Message{

    private final ArrayList<String> nickNames;

    public Message_ArrayList_String(MessageType messageType, int clientID, ArrayList<String> nickNames) {
        super(messageType, clientID);
        this.nickNames = nickNames;
    }

    public ArrayList<String> getNickNames() {
        return nickNames;
    }

    @Override
    public String toString() {
        return super.toString() + "nickNames: " + nickNames.toString();
    }
}
