package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.network.messages.*;

public class InputController {

    public static boolean login_check(Message_One_Parameter_String loginMessage){
        try{
            return loginMessage.getPar() != null;
        } catch (ClassCastException e){
            return false;
        }
    }

    public static boolean num_players_check(Message_One_Parameter_Int numPlayersMessage) {
        int numPlayers = numPlayersMessage.getPar();
        return numPlayers >= 1 && numPlayers <= 4;
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
        return chosenLeaderCard == 0 || chosenLeaderCard == 1;
    }
}
