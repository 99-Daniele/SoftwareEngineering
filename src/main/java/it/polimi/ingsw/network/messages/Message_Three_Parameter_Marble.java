package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.market.Marble;

import java.util.Arrays;

public class Message_Three_Parameter_Marble extends Message{
    private final Marble marble1;
    private final Marble marble2;
    private final Marble marble3;

    public Message_Three_Parameter_Marble( MessageType messageType, int clientID, Marble marble1, Marble marble2, Marble marble3) {
        super(messageType, clientID);
        this.marble1 = marble1;
        this.marble2 = marble2;
        this.marble3 = marble3;
    }

    public Marble getMarble1() {
        return marble1;
    }

    public Marble getMarble2() {
        return marble2;
    }

    public Marble getMarble3() {
        return marble3;
    }

    @Override
    public String toString() {
        return "Message_Three_Vector_Int{" +
                "marble1=" + marble1 +
                ", marble2=" + marble2 +
                ", marble3=" + marble3 +
                '}';
    }
}
