package it.polimi.ingsw.network.messages;

public class MessageOneParameterInt extends Message {

    private final int par;

    public MessageOneParameterInt(MessageType messageType, int clientID, int par) {
        super(messageType, clientID);
        this.par = par;
    }

    @Override
    public String toString() {
        return super.toString() + "{par: " + par + "}";
    }

    public int getPar() {
        return par;
    }

}
