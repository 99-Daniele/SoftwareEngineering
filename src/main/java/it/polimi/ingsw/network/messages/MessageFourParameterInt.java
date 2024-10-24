package it.polimi.ingsw.network.messages;

/**
 * MessageFourParameterInt is a type of Message with four int params.
 */
public class MessageFourParameterInt extends Message{

    private final int par1;
    private final int par2;
    private final int par3;
    private final int par4;

    public MessageFourParameterInt(MessageType messageType, int clientID, int par1, int par2, int par3, int par4) {
        super(messageType, clientID);
        this.par1 = par1;
        this.par2 = par2;
        this.par3 = par3;
        this.par4 = par4;
    }

    public int getPar1() {
        return par1;
    }

    public int getPar2() {
        return par2;
    }

    public int getPar3() {
        return par3;
    }

    public int getPar4() {
        return par4;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "par1: " + par1 +
                ", par2: " + par2 +
                ", par3: " + par3 +
                ", par4: " + par4 +
                '}';
    }
}
