package it.polimi.ingsw.network.messages;

import java.util.Arrays;

public class Message_Three_Vector_Int extends Message{
    private final int[] switch1;
    private final int[] switch2;
    private final int[] switch3;

    public Message_Three_Vector_Int(int clientID, MessageType messageType, int[] switch1, int[] switch2, int[] switch3) {
        super(clientID, messageType);
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.switch3 = switch3;
    }

    public int[] getSwitch1() {
        return switch1;
    }

    public int[] getSwitch2() {
        return switch2;
    }

    public int[] getSwitch3() {
        return switch3;
    }

    @Override
    public String toString() {
        return "MessageAvailableSwitchReply{" +
                "switch1=" + Arrays.toString(switch1) +
                ", switch2=" + Arrays.toString(switch2) +
                ", switch3=" + Arrays.toString(switch3) +
                '}';
    }
}
