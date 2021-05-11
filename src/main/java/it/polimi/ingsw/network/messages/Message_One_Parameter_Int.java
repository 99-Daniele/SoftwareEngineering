package it.polimi.ingsw.network.messages;

public class Message_One_Parameter_Int extends Message {

    private final int par;

    public Message_One_Parameter_Int(MessageType messageType, int clientID, int par) {
        super(messageType, clientID);
        this.par = par;
    }

    @Override
    public String toString() {
        return super.toString() + "{ par = " + par + "}";
    }

    public int getPar() {
        return par;
    }
}
