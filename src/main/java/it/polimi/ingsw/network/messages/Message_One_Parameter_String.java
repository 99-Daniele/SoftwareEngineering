package it.polimi.ingsw.network.messages;

public class Message_One_Parameter_String extends Message{

    private final String par;

    public Message_One_Parameter_String(MessageType messageType, int clientID, String par) {
        super(messageType, clientID);
        this.par = par;
    }

    @Override
    public String toString() {
        return super.toString() + " -> "  + par;
    }

    public String getPar() {
        return par;
    }
}
