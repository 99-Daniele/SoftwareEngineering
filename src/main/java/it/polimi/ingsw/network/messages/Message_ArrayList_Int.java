package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class Message_ArrayList_Int extends Message{

    private final ArrayList<Integer> params = new ArrayList<>();

    public Message_ArrayList_Int(MessageType messageType, int clientID, int firstParam) {
        super(messageType, clientID);
        params.add(firstParam);
    }

    public void addParam(int param){
        params.add(param);
    }

    public ArrayList<Integer> getParams() {
        return params;
    }
}
