package it.polimi.ingsw.parser;

import java.util.HashMap;

public class CardMapGUI {

    private static CardMapGUI cardParser;
    private static HashMap<Integer, String> cardMap = new HashMap<>();

    private CardMapGUI() {
        cardMap.put(1, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        cardMap.put(2, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-5-1.png");
        cardMap.put(3, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-9-1.png");
        cardMap.put(4, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-13-1.png");
        cardMap.put(5, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.png");
        cardMap.put(6, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-6-1.png");
        cardMap.put(7, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-10-1.png");
        cardMap.put(8, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-14-1.png");
        cardMap.put(9, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-3-1.png");
        cardMap.put(10, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-7-1.png");
        cardMap.put(11, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-11-1.png");
        cardMap.put(12, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-15-1.png");
        cardMap.put(13, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-4-1.png");
        cardMap.put(14, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-8-1.png");
        cardMap.put(15, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-12-1.png");
        cardMap.put(16, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-16-1.png");
        cardMap.put(17, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-17-1.png");
        cardMap.put(18, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-21-1.png");
        cardMap.put(19, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-25-1.png");
        cardMap.put(20, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-29-1.png");
        cardMap.put(21, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-18-1.png");
        cardMap.put(22, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-22-1.png");
        cardMap.put(23, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-26-1.png");
        cardMap.put(24, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-30-1.png");
        cardMap.put(25, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");
        cardMap.put(26, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-23-1.png");
        cardMap.put(27, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-27-1.png");
        cardMap.put(28, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-31-1.png");
        cardMap.put(29, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-20-1.png");
        cardMap.put(30, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-24-1.png");
        cardMap.put(31, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-28-1.png");
        cardMap.put(32, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-32-1.png");
        cardMap.put(33, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-33-1.png");
        cardMap.put(34, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-37-1.png");
        cardMap.put(35, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-41-1.png");
        cardMap.put(36, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-45-1.png");
        cardMap.put(37, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-34-1.png");
        cardMap.put(38, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-38-1.png");
        cardMap.put(39, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-42-1.png");
        cardMap.put(40, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-46-1.png");
        cardMap.put(41, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-35-1.png");
        cardMap.put(42, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-39-1.png");
        cardMap.put(43, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-43-1.png");
        cardMap.put(44, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-47-1.png");
        cardMap.put(45, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-36-1.png");
        cardMap.put(46, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-40-1.png");
        cardMap.put(47, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-44-1.png");
        cardMap.put(48, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-48-1.png");
        cardMap.put(49, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-49-1.png");
        cardMap.put(50, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-50-1.png");
        cardMap.put(51, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-51-1.png");
        cardMap.put(52, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-52-1.png");
        cardMap.put(53, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        cardMap.put(54, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        cardMap.put(55, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-55-1.png");
        cardMap.put(56, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-56-1.png");
        cardMap.put(57, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-57-1.png");
        cardMap.put(58, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-58-1.png");
        cardMap.put(59, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-59-1.png");
        cardMap.put(60, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-60-1.png");
        cardMap.put(61, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
        cardMap.put(62, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-62-1.png");
        cardMap.put(63, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-63-1.png");
        cardMap.put(64, "/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-64-1.png");
    }

    public static String getCard(int cardID){
        if(cardParser == null)
            cardParser = new CardMapGUI();
        return cardMap.get(cardID);
    }
}
