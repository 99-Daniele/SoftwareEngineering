package it.polimi.ingsw.network.messages;

public class Message_One_Parameter_Int extends Message {

    private final int par;

    public Message_One_Parameter_Int(int clientID,MessageType messageType, int par) {
        super(clientID,messageType);
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
