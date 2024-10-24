package it.polimi.ingsw.network.messages;

/**
 * MessageTwoParameterInt is a type of Message with two int params.
 */
public class MessageTwoParameterInt extends Message {

    private final int par1;
    private final int par2;

    public MessageTwoParameterInt(MessageType messageType, int clientID, int par, int par2) {
        super(messageType, clientID);
        this.par1 = par;
        this.par2 = par2;
    }

    @Override
    public String toString() {
        return super.toString() +"{par1: " + par1 + ", par2: " + par2 + "}";
    }

    public int getPar1() {
        return par1;
    }

    public int getPar2(){return par2;}
}
