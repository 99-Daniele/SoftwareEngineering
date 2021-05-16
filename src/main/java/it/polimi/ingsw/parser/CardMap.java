package it.polimi.ingsw.parser;

import it.polimi.ingsw.cards.PrintedCard;
import it.polimi.ingsw.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.cards.leaderCards.LeaderCard;

import java.util.ArrayList;
import java.util.HashMap;

public class CardMap {

    private static CardMap cardParser;
    private static HashMap<Integer, PrintedCard> cardMap = new HashMap<>();

    private CardMap() {
        DevelopmentCardsParser developmentCardsParser = new DevelopmentCardsParser();
        LeaderCardsParser leaderCardsParser = new LeaderCardsParser();
        DevelopmentCard[] developmentCards = developmentCardsParser.getDevelopmentCards();
        ArrayList<LeaderCard> leaderCards = leaderCardsParser.getLeaderCards();
        cardMap.put(1, developmentCards[0]);
        cardMap.put(2, developmentCards[1]);
        cardMap.put(3, developmentCards[2]);
        cardMap.put(4, developmentCards[3]);
        cardMap.put(5, developmentCards[4]);
        cardMap.put(6, developmentCards[5]);
        cardMap.put(7, developmentCards[6]);
        cardMap.put(8, developmentCards[7]);
        cardMap.put(9, developmentCards[8]);
        cardMap.put(10, developmentCards[9]);
        cardMap.put(11, developmentCards[10]);
        cardMap.put(12, developmentCards[11]);
        cardMap.put(13, developmentCards[12]);
        cardMap.put(14, developmentCards[13]);
        cardMap.put(15, developmentCards[14]);
        cardMap.put(16, developmentCards[15]);
        cardMap.put(17, developmentCards[16]);
        cardMap.put(18, developmentCards[17]);
        cardMap.put(19, developmentCards[18]);
        cardMap.put(20, developmentCards[19]);
        cardMap.put(21, developmentCards[20]);
        cardMap.put(22, developmentCards[21]);
        cardMap.put(23, developmentCards[22]);
        cardMap.put(24, developmentCards[23]);
        cardMap.put(25, developmentCards[24]);
        cardMap.put(26, developmentCards[25]);
        cardMap.put(27, developmentCards[26]);
        cardMap.put(28, developmentCards[27]);
        cardMap.put(29, developmentCards[28]);
        cardMap.put(30, developmentCards[29]);
        cardMap.put(31, developmentCards[30]);
        cardMap.put(32, developmentCards[31]);
        cardMap.put(33, developmentCards[32]);
        cardMap.put(34, developmentCards[33]);
        cardMap.put(35, developmentCards[34]);
        cardMap.put(36, developmentCards[35]);
        cardMap.put(37, developmentCards[36]);
        cardMap.put(38, developmentCards[37]);
        cardMap.put(39, developmentCards[38]);
        cardMap.put(40, developmentCards[39]);
        cardMap.put(41, developmentCards[40]);
        cardMap.put(42, developmentCards[41]);
        cardMap.put(43, developmentCards[42]);
        cardMap.put(44, developmentCards[43]);
        cardMap.put(45, developmentCards[44]);
        cardMap.put(46, developmentCards[45]);
        cardMap.put(47, developmentCards[46]);
        cardMap.put(48, developmentCards[47]);
        cardMap.put(49, leaderCards.get(0));
        cardMap.put(50, leaderCards.get(1));
        cardMap.put(51, leaderCards.get(2));
        cardMap.put(52, leaderCards.get(3));
        cardMap.put(53, leaderCards.get(4));
        cardMap.put(54, leaderCards.get(5));
        cardMap.put(55, leaderCards.get(6));
        cardMap.put(56, leaderCards.get(7));
        cardMap.put(57, leaderCards.get(8));
        cardMap.put(58, leaderCards.get(9));
        cardMap.put(59, leaderCards.get(10));
        cardMap.put(60, leaderCards.get(11));
        cardMap.put(61, leaderCards.get(12));
        cardMap.put(62, leaderCards.get(13));
        cardMap.put(63, leaderCards.get(14));
        cardMap.put(64, leaderCards.get(15));
    }

    public static PrintedCard getCard(int cardID){
        if(cardParser == null)
            cardParser = new CardMap();
        return cardMap.get(cardID);
    }
}
