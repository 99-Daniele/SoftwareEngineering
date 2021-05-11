package it.polimi.ingsw.network.messages;

public class Message_Five_Parameter_Int extends Message{
    private final int par1;
    private final int par2;
    private final int par3;
    private final int par4;
    private final int par5;

    public Message_Five_Parameter_Int(MessageType messageType, int clientID,  int par1, int par2, int par3, int par4, int par5) {
        super( messageType, clientID);
        this.par1 = par1;
        this.par2 = par2;
        this.par3 = par3;
        this.par4 = par4;
        this.par5 = par5;
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

    public int getPar5() {
        return par5;
    }

    @Override
    public String toString() {
        return "Message_Five_Parameter_Int{" +
                "par1=" + par1 +
                ", par2=" + par2 +
                ", par3=" + par3 +
                ", par4=" + par4 +
                ", par5=" + par5 +
                '}';
    }
}
