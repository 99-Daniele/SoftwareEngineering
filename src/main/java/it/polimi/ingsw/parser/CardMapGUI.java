package it.polimi.ingsw.parser;


import java.util.HashMap;

public class CardMapGUI {
    private static CardMapGUI cardParser;
    private static HashMap<Integer, String> cardMap = new HashMap<>();

    private CardMapGUI() {
        cardMap.put(1, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        cardMap.put(2, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-5-1.png");
        cardMap.put(3, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-9-1.png");
        cardMap.put(4, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-13-1.png");
        cardMap.put(5, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.png");
        cardMap.put(6, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-6-1.png");
        cardMap.put(7, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-10-1.png");
        cardMap.put(8, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-14-1.png");
        cardMap.put(9, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-3-1.png");
        cardMap.put(10, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-7-1.png");
        cardMap.put(11, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-11-1.png");
        cardMap.put(12, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-15-1.png");
        cardMap.put(13, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-4-1.png");
        cardMap.put(14, "Masters_of_Renaissance_Cards_FRONT_3mmBleed_1-8-1.png");
        cardMap.put(15, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-12-1.png");
        cardMap.put(16, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-16-1.png");
        cardMap.put(17, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-17-1.png");
        cardMap.put(18, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-21-1.png");
        cardMap.put(19, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-25-1.png");
        cardMap.put(20, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-29-1.png");
        cardMap.put(21, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-18-1.png");
        cardMap.put(22, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-22-1.png");
        cardMap.put(23, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-26-1.png");
        cardMap.put(24, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-30-1.png");
        cardMap.put(25, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");
        cardMap.put(26, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-23-1.png");
        cardMap.put(27, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-27-1.png");
        cardMap.put(28, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-31-1.png");
        cardMap.put(29, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-20-1.png");
        cardMap.put(30, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-24-1.png");
        cardMap.put(31, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-28-1.png");
        cardMap.put(32, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-32-1.png");
        cardMap.put(33, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-33-1.png");
        cardMap.put(34, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-37-1.png");
        cardMap.put(35, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-41-1.png");
        cardMap.put(36, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-45-1.png");
        cardMap.put(37, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-34-1.png");
        cardMap.put(38, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-38-1.png");
        cardMap.put(39, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-42-1.png");
        cardMap.put(40, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-46-1.png");
        cardMap.put(41, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-35-1.png");
        cardMap.put(42, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-39-1.png");
        cardMap.put(43, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-43-1.png");
        cardMap.put(44, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-47-1.png");
        cardMap.put(45, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-36-1.png");
        cardMap.put(46, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-40-1.png");
        cardMap.put(47, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-44-1.png");
        cardMap.put(48, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-48-1.png");
        cardMap.put(49, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49-1.png");
        cardMap.put(50, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50-1.png");
        cardMap.put(51, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-51-1.png");
        cardMap.put(52, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-52-1.png");
        cardMap.put(53, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        cardMap.put(54, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        cardMap.put(55, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-55-1.png");
        cardMap.put(56, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-56-1.png");
        cardMap.put(57, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-57-1.png");
        cardMap.put(58, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-58-1.png");
        cardMap.put(59, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-59-1.png");
        cardMap.put(60, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-60-1.png");
        cardMap.put(61, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
        cardMap.put(62, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-62-1.png");
        cardMap.put(63, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-63-1.png");
        cardMap.put(64, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-64-1.png");
    }

    public static String getCard(int cardID){
        if(cardParser == null)
            cardParser = new CardMapGUI();
        return cardMap.get(cardID);
    }
}
