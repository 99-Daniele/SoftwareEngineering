package it.polimi.ingsw.model.resourceContainers;

import it.polimi.ingsw.view.CLI.ColorAnsi;

/**
 * Resource is a enumeration which represents the 4 type of resources.
 */
public enum Resource {

    COIN,
    SERVANT,
    SHIELD,
    STONE;

    public static String printResource(Resource resource){
        switch (resource){
            case SERVANT:
                return ColorAnsi.ANSI_PURPLE.escape() + resource + ColorAnsi.RESET;
            case SHIELD:
                return ColorAnsi.ANSI_CYAN.escape() + resource + ColorAnsi.RESET;
            case STONE:
                return ColorAnsi.ANSI_WHITE.escape() + resource + ColorAnsi.RESET;
            case COIN:
                return ColorAnsi.ANSI_YELLOW.escape() + resource + ColorAnsi.RESET;
        }
        return null;
    }
}
