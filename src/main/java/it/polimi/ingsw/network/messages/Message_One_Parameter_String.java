package it.polimi.ingsw.network.messages;

public class Message_One_Parameter_String extends Message{

    private final String par;

    public Message_One_Parameter_String(int clientID,MessageType messageType, String par) {
        super(clientID,messageType);
        this.par = par;
    }

    @Override
    public String toString() {
        return super.toString() + par;
    }

    public String getPar() {
        return par;
    }
}
