package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;

import it.polimi.ingsw.parser.CardMapCLI;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InputControllerTest {

    /**
     * test the correct check parameters of login message.
     */
    @Test
    void correctLoginCheck(){

        Message_One_Parameter_String m1 = new Message_One_Parameter_String(MessageType.LOGIN, 0, null);
        Message_One_Parameter_String m2 = new Message_One_Parameter_String(MessageType.LOGIN, 1, "Daniele");
        assertFalse(InputController.login_check(m1.getPar()));
        assertTrue(InputController.login_check(m2.getPar()));
    }

    /**
     * test the correct check of num players message.
     */
    @Test
    void correctNumPlayersCheck(){

        assertFalse(InputController.num_players_check(0));
        assertFalse(InputController.num_players_check(5));
        assertTrue(InputController.num_players_check(3));
    }

    /**
     * test the correct check of first leader cards choices.
     */
    @Test
    void correctLeaderCardsCheck(){

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(53));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(64));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));

        assertFalse(InputController.leader_card_check(10, 74, leaderCards));
        assertFalse(InputController.leader_card_check(51, 64, leaderCards));
        assertFalse(InputController.leader_card_check(51, 64, leaderCards));
        assertFalse(InputController.leader_card_check(53, 56, leaderCards));
        assertFalse(InputController.leader_card_check(51, 51, leaderCards));
        assertFalse(InputController.leader_card_check(53, 53, leaderCards));
        assertTrue(InputController.leader_card_check(49, 57, leaderCards));
    }

    /**
     * test the correct check of taken marbles message.
     */
    @Test
    void correctTakeMarbleCheck(){

        assertFalse(InputController.taken_marbles_check(2, 3));
        assertFalse(InputController.taken_marbles_check(0, 0));
        assertFalse(InputController.taken_marbles_check(1, 5));
        assertTrue(InputController.taken_marbles_check(0, 3));
    }

    /**
     * test the correct check of switch depot message.
     */
    @Test
    void correctSwitchDepotCheck(){

        assertFalse(InputController.switch_depot_check(0, 7));
        assertFalse(InputController.switch_depot_check(0, 4));
        assertFalse(InputController.switch_depot_check(1, 6));
        assertFalse(InputController.switch_depot_check(4, 4));
        assertFalse(InputController.switch_depot_check(0, 0));
        assertTrue(InputController.switch_depot_check(3, 4));
    }

    /**
     * test the correct check of buy card message.
     */
    @Test
    void correctBuyCardCheck(){

        assertFalse(InputController.buy_card_check(0, 5, 2));
        assertFalse(InputController.buy_card_check(0, 4, 2));
        assertFalse(InputController.buy_card_check(1, 5, 2));
        assertFalse(InputController.buy_card_check(0, 5, 0));
        assertFalse(InputController.buy_card_check(1, 4, 2));
        assertFalse(InputController.buy_card_check(0, 3, 1));
        assertFalse(InputController.buy_card_check(1, 0, 1));
        assertTrue(InputController.buy_card_check(1, 3, 0));
    }

    /**
     * test the correct check of chosen slot message.
     */
    @Test
    void correctChosenSlotCheck(){

        ArrayList<Integer> slots1 = new ArrayList<>(2);
        ArrayList<Integer> slots2 = new ArrayList<>(2);
        ArrayList<Integer> slots3 = new ArrayList<>(2);
        ArrayList<Integer> slots4 = new ArrayList<>(3);

        slots1.add(1);
        slots1.add(2);
        assertFalse(InputController.chosen_slot_check(0, slots1));
        assertFalse(InputController.chosen_slot_check(5, slots1));
        assertFalse(InputController.chosen_slot_check(3, slots1));
        assertTrue(InputController.chosen_slot_check(1, slots1));
        assertTrue(InputController.chosen_slot_check(2, slots1));

        slots2.add(1);
        slots2.add(3);
        assertFalse(InputController.chosen_slot_check(0, slots2));
        assertFalse(InputController.chosen_slot_check(5, slots2));
        assertFalse(InputController.chosen_slot_check(2, slots2));
        assertTrue(InputController.chosen_slot_check(1, slots2));
        assertTrue(InputController.chosen_slot_check(3, slots2));

        slots3.add(2);
        slots3.add(3);
        assertFalse(InputController.chosen_slot_check(0, slots3));
        assertFalse(InputController.chosen_slot_check(5, slots3));
        assertFalse(InputController.chosen_slot_check(1, slots3));
        assertTrue(InputController.chosen_slot_check(3, slots3));
        assertTrue(InputController.chosen_slot_check(2, slots3));

        slots4.add(1);
        slots4.add(2);
        slots4.add(3);
        assertFalse(InputController.chosen_slot_check(0, slots4));
        assertFalse(InputController.chosen_slot_check(5, slots4));
        assertTrue(InputController.chosen_slot_check(1, slots4));
        assertTrue(InputController.chosen_slot_check(2, slots4));
        assertTrue(InputController.chosen_slot_check(3, slots4));
    }

    /**
     * test the correct check of development card power message.
     */
    @Test
    void correctDevelopmentCardPowerCheck(){

        ArrayList<Integer> chosenSlot = new ArrayList<>();
        assertFalse(InputController.development_card_power_check(0, 2, chosenSlot));
        assertFalse(InputController.development_card_power_check(0, 0, chosenSlot));
        assertFalse(InputController.development_card_power_check(1, 2, chosenSlot));
        assertTrue(InputController.development_card_power_check(3, 0, chosenSlot));
        chosenSlot.add(3);
        assertFalse(InputController.development_card_power_check(3, 1, chosenSlot));
        assertTrue(InputController.development_card_power_check(1, 0, chosenSlot));
        assertTrue(InputController.development_card_power_check(2, 0, chosenSlot));
    }

    /**
     * test the correct check of basic power message.
     */
    @Test
    void correctBasicPowerCheck(){

        boolean basicPower = false;
        assertFalse(InputController.basic_power_check(2, basicPower));
        assertTrue(InputController.basic_power_check(0, basicPower));
        assertTrue(InputController.basic_power_check(1, basicPower));
        basicPower = true;
        assertFalse(InputController.basic_power_check(0, basicPower));
        assertFalse(InputController.basic_power_check(1, basicPower));
    }

    /**
     * test the correct check of leader card power message.
     */
    @Test
    void correctLeaderCardPowerCheck(){

        ArrayList<Integer> chosenLeaderCards = new ArrayList<>();
        assertFalse(InputController.leader_card_power_check(0, 2, chosenLeaderCards));
        assertFalse(InputController.leader_card_power_check(0, 0, chosenLeaderCards));
        assertFalse(InputController.leader_card_power_check(1, 2, chosenLeaderCards));
        assertTrue(InputController.leader_card_power_check(2, 0, chosenLeaderCards));
        chosenLeaderCards.add(2);
        assertFalse(InputController.leader_card_power_check(2, 1, chosenLeaderCards));
        assertTrue(InputController.leader_card_power_check(1, 0, chosenLeaderCards));
    }

    /**
     * test the correct check of white conversion card message.
     */
    @Test
    void correctWhiteConversionCardCheck(){

        assertFalse(InputController.white_conversion_card_check(0));
        assertTrue(InputController.white_conversion_card_check(1));
        assertTrue(InputController.white_conversion_card_check(2));
    }

    /**
     * test the correct check of use marbles message.
     */
    @Test
    void correctUseMarbleCheck(){

        ArrayList<Marble> marbles = new ArrayList<>(4);
        marbles.add(new WhiteMarble());
        marbles.add(new RedMarble());
        marbles.add(new ResourceMarble(Resource.COIN));
        marbles.add(new ResourceMarble(Resource.SERVANT));

        assertFalse(InputController.chosen_correct_marble(new ResourceMarble(Resource.STONE), marbles));
        assertFalse(InputController.chosen_correct_marble(new ResourceMarble(Resource.SHIELD), marbles));
        assertTrue(InputController.chosen_correct_marble(new WhiteMarble(), marbles));
        assertTrue(InputController.chosen_correct_marble(new ResourceMarble(Resource.COIN), marbles));
        assertTrue(InputController.chosen_correct_marble(new ResourceMarble(Resource.SERVANT), marbles));
        assertTrue(InputController.chosen_correct_marble(new RedMarble(), marbles));
    }

    /**
     * test the correct check of leader card activation message.
     */
    @Test
    void correctLeaderCardActivationCheck(){

        assertFalse(InputController.leader_card_activation(0));
        assertTrue(InputController.leader_card_activation(1));
        assertTrue(InputController.leader_card_activation(2));
    }

    /**
     * test the correct check of leader card discard message.
     */
    @Test
    void correctLeaderCardDiscardCheck(){

        assertFalse(InputController.leader_card_discard(0));
        assertTrue(InputController.leader_card_discard(1));
        assertTrue(InputController.leader_card_discard(2));
    }

}
