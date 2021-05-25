package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

public class InputController {

    public static boolean login_check(String nickName){
        return (nickName != null && nickName.length() != 0 && !nickName.isBlank());
    }

    public static boolean num_players_check(int numPlayers) {
        return numPlayers >= 1 && numPlayers <= 4;
    }

    public static boolean leader_card_check(int card1, int card2, ArrayList<LeaderCard> leaderCards){
        int count = 0;
        if(card1 == card2)
            return false;
        for (LeaderCard leaderCard: leaderCards) {
            if (leaderCard.getCardID() == card1 || leaderCard.getCardID() == card2)
                count++;
        }
        return count == 2;
    }

    public static boolean taken_marbles_check(int row, int column) {
        if (row == 0) {
            return column >= 1 && column <= 3;
        } else if (row == 1) {
            return column >= 1 && column <= 4;
        } else
            return false;
    }

    public static boolean switch_depot_check(int depot1, int depot2){
        if(depot1 == depot2)
            return false;
        else if(depot1 < 1 || depot1 > 5)
            return false;
        else return depot2 >= 1 && depot2 <= 5;
    }

    public static boolean buy_card_check(int row, int column, int warehouse) {
        if (row < 1 || row > 3) {
            return false;
        } else if (column < 1 || column > 4) {
            return false;
        } else return warehouse == 0 || warehouse == 1;
    }

    public static boolean chosen_slot_check(int chosenSlot, ArrayList<Integer> slots){
        return slots.contains(chosenSlot);
    }

    public static boolean development_card_power_check(int chosenSlot, int warehouse) {
        if (chosenSlot < 1 || chosenSlot > 3)
            return false;
        else return warehouse == 0 || warehouse == 1;
    }

    public static boolean basic_power_check(int warehouse){
        return warehouse == 0 || warehouse == 1;
    }

    public static boolean leader_card_power_check(int chosenLeaderCard, int warehouse){
        if(chosenLeaderCard != 1 && chosenLeaderCard != 2)
            return false;
        return warehouse == 0 || warehouse == 1;
    }

    public static boolean white_conversion_card_check(int chosenLeaderCard){
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

    public static boolean leader_card_activation(int chosenLeaderCard){
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }

    public static boolean leader_card_discard(int chosenLeaderCard){
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }
}
