package it.polimi.ingsw.model.cards.developmentCards;

import it.polimi.ingsw.view.ColorAnsi;

/**
 * Color is an enumeration which represents the 4 color of DevelopmentCards.
 */
public enum Color {

    GREEN,
    PURPLE,
    BLUE,
    YELLOW;

    public static String print(Color color){
        switch (color){
            case BLUE: return ColorAnsi.ANSI_CYAN.escape() + color + ColorAnsi.RESET;
            case GREEN: return ColorAnsi.ANSI_GREEN.escape()+ color + ColorAnsi.RESET;
            case PURPLE: return ColorAnsi.ANSI_PURPLE.escape() + color + ColorAnsi.RESET;
            case YELLOW: return ColorAnsi.ANSI_YELLOW.escape() + color + ColorAnsi.RESET;
        }
        return null;
    }
}
