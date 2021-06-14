package it.polimi.ingsw.network.messages;

public class MessageOneParameterString extends Message{

    private final String par;

    public MessageOneParameterString(MessageType messageType, int clientID, String par) {
        super(messageType, clientID);
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
