package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.model.resourceContainers.Resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputControllerTest {

    /**
     * test the correct check parameters of login message.
     */
    @Test
    void correctLoginCheck(){

        Message_One_Parameter_String m1 = new Message_One_Parameter_String(MessageType.LOGIN, 0, null);
        Message_One_Parameter_String m2 = new Message_One_Parameter_String(MessageType.LOGIN, 1, "Daniele");
        assertFalse(InputController.login_check(m1));
        assertTrue(InputController.login_check(m2));
    }

    /**
     * test the correct check of num players message.
     */
    @Test
    void correctNumPlayersCheck(){

        Message_One_Parameter_Int m1 = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, 0, 0);
        Message_One_Parameter_Int m2 = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, 1, 5);
        Message_One_Parameter_Int m3 = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, 2, 3);
        assertFalse(InputController.num_players_check(m1));
        assertFalse(InputController.num_players_check(m2));
        assertTrue(InputController.num_players_check(m3));
    }

    /**
     * test the correct check of taken marbles message.
     */
    @Test
    void correctTakeMarbleCheck(){

        Message_Two_Parameter_Int m1 = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, 0, 2, 3);
        Message_Two_Parameter_Int m2 = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, 1, 0, 0);
        Message_Two_Parameter_Int m3 = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, 2, 1, 5);
        Message_Two_Parameter_Int m4 = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, 3, 0, 3);
        assertFalse(InputController.taken_marbles_check(m1));
        assertFalse(InputController.taken_marbles_check(m2));
        assertFalse(InputController.taken_marbles_check(m3));
        assertTrue(InputController.taken_marbles_check(m4));
    }

    /**
     * test the correct check of switch depot message.
     */
    @Test
    void correctSwitchDepotCheck(){

        Message_Two_Parameter_Int m1 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 0, 0, 7);
        Message_Two_Parameter_Int m2 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 1, 0, 4);
        Message_Two_Parameter_Int m3 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 2, 1, 6);
        Message_Two_Parameter_Int m4 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 3, 4, 4);
        Message_Two_Parameter_Int m5 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 4, 0, 0);
        Message_Two_Parameter_Int m6 = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, 5, 3, 4);
        assertFalse(InputController.switch_depot_check(m1));
        assertFalse(InputController.switch_depot_check(m2));
        assertFalse(InputController.switch_depot_check(m3));
        assertFalse(InputController.switch_depot_check(m4));
        assertFalse(InputController.switch_depot_check(m5));
        assertTrue(InputController.switch_depot_check(m6));
    }

    /**
     * test the correct check of buy card message.
     */
    @Test
    void correctBuyCardCheck(){

        Message_Three_Parameter_Int m1 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 0, 0, 5, 2);
        Message_Three_Parameter_Int m2 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 1, 0, 4, 2);
        Message_Three_Parameter_Int m3 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 2, 1, 5, 2);
        Message_Three_Parameter_Int m4 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 3, 0, 5, 0);
        Message_Three_Parameter_Int m5 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 4, 1, 4, 2);
        Message_Three_Parameter_Int m6 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 5, 0, 3, 1);
        Message_Three_Parameter_Int m7 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 6, 1, 0, 1);
        Message_Three_Parameter_Int m8 = new Message_Three_Parameter_Int(MessageType.BUY_CARD, 7, 1, 3, 0);
        assertFalse(InputController.buy_card_check(m1));
        assertFalse(InputController.buy_card_check(m2));
        assertFalse(InputController.buy_card_check(m3));
        assertFalse(InputController.buy_card_check(m4));
        assertFalse(InputController.buy_card_check(m5));
        assertFalse(InputController.buy_card_check(m6));
        assertFalse(InputController.buy_card_check(m7));
        assertTrue(InputController.buy_card_check(m8));
    }

    /**
     * test the correct check of development card power message.
     */
    @Test
    void correctDevelopmentCardPowerCheck(){

        Message_Two_Parameter_Int m1 = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, 0, 0, 2);
        Message_Two_Parameter_Int m2 = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, 1, 0, 0);
        Message_Two_Parameter_Int m3 = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, 2, 1, 2);
        Message_Two_Parameter_Int m4 = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, 3, 3, 0);
        assertFalse(InputController.development_card_power_check(m1));
        assertFalse(InputController.development_card_power_check(m2));
        assertFalse(InputController.development_card_power_check(m3));
        assertTrue(InputController.development_card_power_check(m4));
    }

    /**
     * test the correct check of basic power message.
     */
    @Test
    void correctBasicPowerCheck(){

        Message_Three_Resource_One_Int m1 = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, 0, Resource.COIN, Resource.COIN, Resource.COIN, 2);
        Message_Three_Resource_One_Int m2 = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, 1, Resource.COIN, Resource.COIN, Resource.COIN, 1);
        assertFalse(InputController.basic_power_check(m1));
        assertTrue(InputController.basic_power_check(m2));
    }

    /**
     * test the correct check of leader card power message.
     */
    @Test
    void correctLeaderCardPowerCheck(){

        Message_One_Resource_Two_Int m1 = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, 0, Resource.COIN, 0, 2);
        Message_One_Resource_Two_Int m2 = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, 1, Resource.STONE, 0, 0);
        Message_One_Resource_Two_Int m3 = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, 2, Resource.SERVANT, 1, 2);
        Message_One_Resource_Two_Int m4 = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, 3, Resource.SHIELD, 2, 0);
        assertFalse(InputController.leader_card_power_check(m1));
        assertFalse(InputController.leader_card_power_check(m2));
        assertFalse(InputController.leader_card_power_check(m3));
        assertTrue(InputController.leader_card_power_check(m4));
    }

    /**
     * test the correct check of white conversion card message.
     */
    @Test
    void correctWhiteConversionCardCheck(){

        Message_One_Parameter_Int m1 = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, 0,  2);
        Message_One_Parameter_Int m2 = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, 1, 1);
        assertFalse(InputController.white_conversion_card_check(m1));
        assertTrue(InputController.white_conversion_card_check(m2));
    }
}
