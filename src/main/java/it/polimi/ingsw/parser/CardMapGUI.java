package it.polimi.ingsw.parser;

import java.util.HashMap;

public class CardMapGUI {

    private static CardMapGUI cardParser;
    private static HashMap<Integer, String> cardMap = new HashMap<>();

    private CardMapGUI() {
        cardMap.put(1, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        cardMap.put(2, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-5-1.png");
        cardMap.put(3, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-9-1.png");
        cardMap.put(4, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-13-1.png");
        cardMap.put(5, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.png");
        cardMap.put(6, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-6-1.png");
        cardMap.put(7, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-10-1.png");
        cardMap.put(8, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-14-1.png");
        cardMap.put(9, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-3-1.png");
        cardMap.put(10, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-7-1.png");
        cardMap.put(11, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-11-1.png");
        cardMap.put(12, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-15-1.png");
        cardMap.put(13, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-4-1.png");
        cardMap.put(14, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-8-1.png");
        cardMap.put(15, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-12-1.png");
        cardMap.put(16, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-16-1.png");
        cardMap.put(17, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-17-1.png");
        cardMap.put(18, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-21-1.png");
        cardMap.put(19, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-25-1.png");
        cardMap.put(20, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-29-1.png");
        cardMap.put(21, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-18-1.png");
        cardMap.put(22, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-22-1.png");
        cardMap.put(23, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-26-1.png");
        cardMap.put(24, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-30-1.png");
        cardMap.put(25, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");
        cardMap.put(26, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-23-1.png");
        cardMap.put(27, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-27-1.png");
        cardMap.put(28, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-31-1.png");
        cardMap.put(29, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-20-1.png");
        cardMap.put(30, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-24-1.png");
        cardMap.put(31, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-28-1.png");
        cardMap.put(32, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-32-1.png");
        cardMap.put(33, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-33-1.png");
        cardMap.put(34, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-37-1.png");
        cardMap.put(35, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-41-1.png");
        cardMap.put(36, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-45-1.png");
        cardMap.put(37, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-34-1.png");
        cardMap.put(38, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-38-1.png");
        cardMap.put(39, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-42-1.png");
        cardMap.put(40, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-46-1.png");
        cardMap.put(41, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-35-1.png");
        cardMap.put(42, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-39-1.png");
        cardMap.put(43, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-43-1.png");
        cardMap.put(44, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-47-1.png");
        cardMap.put(45, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-36-1.png");
        cardMap.put(46, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-40-1.png");
        cardMap.put(47, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-44-1.png");
        cardMap.put(48, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-48-1.png");
        cardMap.put(49, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-49-1.png");
        cardMap.put(50, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-50-1.png");
        cardMap.put(51, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-51-1.png");
        cardMap.put(52, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-52-1.png");
        cardMap.put(53, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        cardMap.put(54, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        cardMap.put(55, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-55-1.png");
        cardMap.put(56, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-56-1.png");
        cardMap.put(57, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-57-1.png");
        cardMap.put(58, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-58-1.png");
        cardMap.put(59, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-59-1.png");
        cardMap.put(60, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-60-1.png");
        cardMap.put(61, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
        cardMap.put(62, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-62-1.png");
        cardMap.put(63, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-63-1.png");
        cardMap.put(64, "src/main/resources/photos/Masters of Renaissance_Cards_FRONT_3mmBleed_1-64-1.png");
    }

    public static String getCard(int cardID){
        if(cardParser == null)
            cardParser = new CardMapGUI();
        return cardMap.get(cardID);
    }
}
