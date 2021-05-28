package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

/**
 * InputController verifies if player's messages contains the correct parameters.
 */
public class InputController {

    /**
     * @param nickName is player's nickName.
     * @return if nickName is null, or with length == 0, or full of blanks.
     */
    public static boolean login_check(String nickName){
        return (nickName != null && nickName.length() != 0 && !nickName.isBlank());
    }

    /**
     * @param numPlayers is game num of players.
     * @return if @param numPlayers is inferior than 1 or more than 4.
     */
    public static boolean num_players_check(int numPlayers) {
        return numPlayers >= 1 && numPlayers <= 4;
    }

    /**
     * @param card1 is one chosen leader card.
     * @param card2 is one chosen leader card.
     * @param leaderCards are a list of leader cards
     * @return if player has chosen two different cards that are contained in @param leaderCards.
     */
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

    /**
     * @param choice is player's choice between market row or column.
     * @param index is chosen index.
     * @return if @param choice is 0 or 1 and @param index is less than 1 or more than 3 or 4 based on @param choice.
     */
    public static boolean taken_marbles_check(int choice, int index) {
        if (choice == 0) {
            return index >= 1 && index <= 3;
        } else if (choice == 1) {
            return index >= 1 && index <= 4;
        } else
            return false;
    }

    /**
     * @param depot1 is one chosen depot.
     * @param depot2 is one chosen depot.
     * @return if @param depot1 equals @param depot2 and both of them are less than 1 or more than 5.
     */
    public static boolean switch_depot_check(int depot1, int depot2){
        if(depot1 == depot2)
            return false;
        else if(depot1 < 1 || depot1 > 5)
            return false;
        else return depot2 >= 1 && depot2 <= 5;
    }

    /**
     * @param row is deck chosen row.
     * @param column is deck chosen column.
     * @param warehouse is player's choice if gave priority to warehouse.
     * @return if @param row is less than 1 or more than 3, @param column less than 1 or more than 4, @param warehouse is
     * 0 or 1.
     */
    public static boolean buy_card_check(int row, int column, int warehouse) {
        if (row < 1 || row > 3) {
            return false;
        } else if (column < 1 || column > 4) {
            return false;
        } else return warehouse == 0 || warehouse == 1;
    }

    /**
     * @param chosenSlot is player's chosen slot.
     * @param slots are player's available slots.
     * @return if @param slots contains @param chosenSlot.
     */
    public static boolean chosen_slot_check(int chosenSlot, ArrayList<Integer> slots){
        return slots.contains(chosenSlot);
    }

    /**
     * @param chosenSlot is player's chosen slot.
     * @param warehouse is player's choice if gave priority to warehouse.
     * @return if @param chosenSlot is less than 1 or more than 2 and @param warehouse is 0 or 1.
     */
    public static boolean development_card_power_check(int chosenSlot, int warehouse) {
        if (chosenSlot < 1 || chosenSlot > 3)
            return false;
        else return warehouse == 0 || warehouse == 1;
    }

    /**
     * @param warehouse is player's choice if gave priority to warehouse.
     * @return if @param warehouse is 0 or 1.
     */
    public static boolean basic_power_check(int warehouse){
        return warehouse == 0 || warehouse == 1;
    }

    /**
     * @param chosenLeaderCard is player's chosen leader card.
     * @param warehouse is player's choice if gave priority to warehouse.
     * @return if @param chosenLeaderCard is 1 or 2 and @param warehouse is 0 or 1.
     */
    public static boolean leader_card_power_check(int chosenLeaderCard, int warehouse){
        if(chosenLeaderCard != 1 && chosenLeaderCard != 2)
            return false;
        return warehouse == 0 || warehouse == 1;
    }

    /**
     * @param chosenLeaderCard is player's chosen leader card.
     * @return if @param chosenLeaderCard is 1 or 2.
     */
    public static boolean white_conversion_card_check(int chosenLeaderCard){
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }

    /**
     * @param chosenMarble is player's chosen marble.
     * @param remainingMarbles are a list of available marbles.
     * @return if @param remainingMarbles contains @param chosenMarble.
     */
    public static boolean chosen_correct_marble(Marble chosenMarble, ArrayList<Marble> remainingMarbles) {
        for (Marble marble : remainingMarbles) {
            if (marble.toString().equals(chosenMarble.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param chosenLeaderCard is player's chosen leader card.
     * @return if @param chosenLeaderCard is 1 or 2.
     */
    public static boolean leader_card_activation(int chosenLeaderCard){
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }

    /**
     * @param chosenLeaderCard is player's chosen leader card.
     * @return if @param chosenLeaderCard is 1 or 2.
     */
    public static boolean leader_card_discard(int chosenLeaderCard){
        return chosenLeaderCard == 1 || chosenLeaderCard == 2;
    }
}
