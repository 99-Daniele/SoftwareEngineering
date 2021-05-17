package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;

public class InputController {

    public static boolean login_check(Message_One_Parameter_String loginMessage){
        if(loginMessage.getPar() == null)
            return false;
        return loginMessage.getPar().length() != 0;
    }

    public static boolean num_players_check(Message_One_Parameter_Int numPlayersMessage) {
        int numPlayers = numPlayersMessage.getPar();
        return numPlayers >= 1 && numPlayers <= 4;
    }

    public static boolean already_chosen_leader_card_check(int viewID, ArrayList<Integer> players){
        return players.contains(viewID);
    }

    public static boolean already_chosen_resource_check(int viewID, ArrayList<Integer> players){
        return players.contains(viewID);
    }

    public static boolean leader_card_check(Message_Two_Parameter_Int leaderCardMessage, ArrayList<LeaderCard> leaderCards){
        int firstChoice = -1;
        for (LeaderCard leaderCard: leaderCards) {
            if (leaderCard.getCardID() == leaderCardMessage.getPar1())
                firstChoice = leaderCard.getCardID();
        }
        if(firstChoice == -1)
            return false;
        for (LeaderCard leaderCard: leaderCards) {
            if (leaderCard.getCardID() == leaderCardMessage.getPar2() && leaderCard.getCardID() != firstChoice)
                return true;
        }
        return false;
    }

    public static boolean taken_marbles_check(Message_Two_Parameter_Int takenMarbleMessage) {
        int row = takenMarbleMessage.getPar1();
        int column = takenMarbleMessage.getPar2();
        if (row == 0) {
            return column >= 1 && column <= 3;
        } else if (row == 1) {
            return column >= 1 && column <= 4;
        } else
            return false;
    }

    public static boolean switch_depot_check(Message_Two_Parameter_Int switchDepotMessage){
        int depot1 = switchDepotMessage.getPar1();
        int depot2 = switchDepotMessage.getPar2();
        if(depot1 == depot2)
            return false;
        else if(depot1 < 1 || depot1 > 5)
            return false;
        else return depot2 >= 1 && depot2 <= 5;
    }

    public static boolean buy_card_check(Message_Three_Parameter_Int buyCardMessage) {
        int row = buyCardMessage.getPar1();
        int column = buyCardMessage.getPar2();
        int warehouse = buyCardMessage.getPar3();
        if (row < 1 || row > 3) {
            return false;
        } else if (column < 1 || column > 4) {
            return false;
        } else return warehouse == 0 || warehouse == 1;
    }

    public static boolean chosen_slot_check(int chosenSlot, ArrayList<Integer> slots){
        return slots.contains(chosenSlot);
    }

    public static boolean development_card_power_check(Message_Two_Parameter_Int developmentCardPowerMessage) {
        int chosenSlot = developmentCardPowerMessage.getPar1();
        int warehouse = developmentCardPowerMessage.getPar2();
        if (chosenSlot < 1 || chosenSlot > 3)
            return false;
        else return warehouse == 0 || warehouse == 1;
    }

    public static boolean basic_power_check(Message_Three_Resource_One_Int basicPowerMessage){
        int warehouse = basicPowerMessage.getPar();
        return warehouse == 0 || warehouse == 1;
    }

    public static boolean leader_card_power_check(Message_One_Resource_Two_Int leaderCardPowerMessage){
        int chosenLeaderCard = leaderCardPowerMessage.getPar1();
        int warehouse = leaderCardPowerMessage.getPar2();
        if(chosenLeaderCard != 1 && chosenLeaderCard != 2)
            return false;
        return warehouse == 0 || warehouse == 1;
    }

    public static boolean white_conversion_card_check(Message_One_Parameter_Int whiteConversionCardMessage){
        int chosenLeaderCard = whiteConversionCardMessage.getPar();
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }

    public static boolean chosen_correct_marble(Marble chosenMarble, ArrayList<Marble> remainingMarbles) {
        for (Marble marble : remainingMarbles) {
            if (marble.toString().equals(chosenMarble.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean leader_card_activation(Message_One_Parameter_Int leaderCardActivation){
        int chosenLeaderCard = leaderCardActivation.getPar();
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }

    public static boolean leader_card_discard(Message_One_Parameter_Int leaderCardDiscard){
        int chosenLeaderCard = leaderCardDiscard.getPar();
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }
}
