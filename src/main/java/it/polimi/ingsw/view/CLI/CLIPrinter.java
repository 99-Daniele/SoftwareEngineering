package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.cards.leaderCards.CardRequirement;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.cards.leaderCards.LeaderRequirements;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.model.resourceContainers.ResourceContainer;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.modelView.CardView;
import it.polimi.ingsw.view.modelView.GameView;

import java.util.ArrayList;

/**
 * CLIPrinter has the methods to print all different playerboard's objects.
 */
public class CLIPrinter {

    /**
     * @param game is current game.
     *
     * print the market.
     */
    public static void printMarket(GameView game){
        Marble[][] marketTray = game.getMarket().getMarketTray();
        Marble externalMarble = game.getMarket().getExternalMarble();
        System.out.println("\nMARKET:");
        String[] s = new String[13];
        int i = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                Marble m = marketTray[x][y];
                s[i] = fromMarbleToString(m);
                i++;
            }
        }
        s[i] = fromMarbleToString(externalMarble);
        creationTable(s);
    }

    /**
     * @param game is current game.
     * @param row is a chosen row of the market
     *
     * print @param row of the market.
     */
    public static void printRowMarket(GameView game, int row){
        Marble[][] marketTray = game.getMarket().getMarketTray();
        row--;
        String[] s = new String[4];
        int i = 0;
        for (int y = 0; y < 4; y++) {
            Marble m = marketTray[row][y];
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationRow(s);
    }

    /**
     * @param game is current game.
     * @param column is a chosen column of the market
     *
     * print @param column of the market.
     */
    public static void printColumnMarket(GameView game, int column){
        Marble[][] marketTray = game.getMarket().getMarketTray();
        column--;
        String[] s = new String[3];
        int i = 0;
        for (int x = 0; x < 3; x++) {
            Marble m = marketTray[x][column];
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationColumn(s);
    }

    /**
     * @param marbles are player's chosen marbles.
     *
     * print @param marbles like a row of the market.
     */
    public static void printMarbles(ArrayList<Marble> marbles){
        String[] s = new String[marbles.size()];
        int i = 0;
        for (Marble m : marbles) {
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationRow(s);
    }

    /**
     * @param game is current game.
     *
     * print the 16 deck DevelopmentCards.
     */
    public static void printDecks(GameView game){
        game.getDecks().print();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID playerboard.
     */
    public static void printPlayerBoard(GameView game, int viewID){
        game.getPlayer(viewID).printCliAllPlayerBoard();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID warehouse and strongbox.
     */
    public static void printWarehouseStrongbox(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCLIWarehouseStrongbox();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID warehouse.
     */
    public static void printWarehouse(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCliWarehouse();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID strongbox.
     */
    public static void printStrongbox(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCliStrongbox();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID SlotDevelopmentCards.
     */
    public static void printCardSlot(GameView game, int viewID){
        game.getSlotCards(viewID).printCliSlot();
    }

    /**
     * @param game is current game.
     * @param viewID is one player.
     *
     * print @param viewID LeaderCards.
     */
    public static void printLeaderCard(GameView game, int viewID){
        game.getSlotCards(viewID).printCliLeaderCard();
    }

    /**
     * @param game is current game.
     *
     * print the FaithTrack.
     */

    public static void printFaithTrack(GameView game){
        game.getFaithTrack().printCli();
    }

    /**
     * @param m is one marble.
     * @return CLI representation of marble.
     */
    private static String fromMarbleToString(Marble m) {
        String s1 = m.toStringAbb();
        switch (s1) {
            case "R":
                return ColorAnsi.ANSI_RED.escape();
            case "G":
                return ColorAnsi.ANSI_WHITE.escape();
            case "Y":
                return ColorAnsi.ANSI_YELLOW.escape();
            case "B":
                return ColorAnsi.ANSI_CYAN.escape();
            case "P":
                return ColorAnsi.ANSI_PURPLE.escape();
            default:
                return "";
        }
    }

    /**
     * @param s summarize in which order print marbles.
     *
     * print market.
     */
    private static void creationTable(String[] s) {
        String[][] tabella = new String[7][17];
        for (int x = 0; x < 7; x += 2) {
            tabella[x][0] = "╠";
            for (int y = 1; y < 16; y++) {
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "╬";
            }
            tabella[x][16] = "╣";
        }
        tabella[0][0] = "╔";
        for (int i = 1; i < 4; i++) {
            tabella[0][4 * i] = "╦";
        }
        tabella[0][16] = "╗";
        tabella[6][0] = "╚";
        for (int i = 1; i < 4; i++) {
            tabella[6][4 * i] = "╩";
        }
        tabella[6][16] = "╝";
        for (int x = 1; x < 7; x += 2) {
            tabella[x][0] = "║";
            for (int y = 1; y < 15; y++) {
                tabella[x][y] = "";
                y += 2;
                tabella[x][y] = "";
                y++;
                tabella[x][y] = "║";
            }
            tabella[x][16] = "║";
        }
        tabella[0][0] = "╔";
        tabella[6][0] = "╚";
        tabella[0][16] = "╗";
        tabella[6][16] = "╝";
        int count = 0;
        for (int x = 1; x < 6; x += 2)
            for (int y = 2; y < 15; y += 4) {
                tabella[x][y] = s[count] + " ● " + ColorAnsi.RESET;
                count++;
            }
        String[][] tabella2 = new String[3][5];
        tabella2[0][0] = "╔";
        tabella2[0][1] = "═";
        tabella2[0][2] = "═";
        tabella2[0][3] = "═";
        tabella2[0][4] = "╗";
        tabella2[1][0] = "║";
        tabella2[1][1] = "";
        tabella2[1][2] = s[count] + " ● " + ColorAnsi.RESET;
        tabella2[1][3] = "";
        tabella2[1][4] = "║";
        tabella2[2][0] = "╚";
        tabella2[2][1] = "═";
        tabella2[2][2] = "═";
        tabella2[2][3] = "═";
        tabella2[2][4] = "╝";
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 17; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(tabella2[i][j]);
            System.out.println();
        }
    }

    /**
     * @param s summarize in which order print marbles.
     *
     * print market row.
     */
    private static void creationRow(String[] s) {
        String[][] tabella = new String[3][s.length * 4 + 1];
        for (int y = 0; y < s.length * 4 + 1; y += 4)
            tabella[1][y] = "║";
        tabella[0][0] = "╔";
        tabella[2][0] = "╚";
        tabella[0][s.length * 4] = "╗";
        tabella[2][s.length * 4] = "╝";
        for (int x = 0; x < 3; x += 2)
            for (int y = 1; y < s.length * 4; y++)
                tabella[x][y] = "═";
        for (int i = 1; i < s.length; i++) {
            tabella[0][4 * i] = "╦";
        }
        for (int i = 1; i < s.length; i++) {
            tabella[2][4 * i] = "╩";
        }
        int count = 0;
        for (int x = 1; x < 3; x += 2) {
            for (int y = 1; y < s.length * 4 + 1; y += 2) {
                tabella[x][y] = "";
                y++;
                tabella[x][y] = s[count] + " ● " + ColorAnsi.RESET;
                y++;
                tabella[x][y] = "";
                count++;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < s.length * 4 + 1; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
    }

    /**
     * @param s summarize in which order print marbles.
     *
     * print market column.
     */
    private static void creationColumn(String[] s) {
        String[][] tabella = new String[7][5];
        for (int x = 1; x < 7; x++)
            for (int y = 0; y < 5; y += 4)
                tabella[x][y] = "║";
        for (int x = 0; x < 7; x += 2) {
            tabella[x][0] = "╠";
            for (int y = 1; y < 4; y++) {
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "╬";
            }
            tabella[x][4] = "╣";
        }
        tabella[0][0] = "╔";
        tabella[6][0] = "╚";
        tabella[0][4] = "╗";
        tabella[6][4] = "╝";
        int count = 0;
        for (int x = 1; x < 7; x += 2) {
            tabella[x][1] = "";
            tabella[x][2] = s[count] + " ● " + ColorAnsi.RESET;
            tabella[x][3] = "";
            count++;
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
    }

    public static String printColor(Color color){
        switch (color){
            case BLUE: return ColorAnsi.ANSI_CYAN.escape() + color + ColorAnsi.RESET;
            case GREEN: return ColorAnsi.ANSI_GREEN.escape()+ color + ColorAnsi.RESET;
            case PURPLE: return ColorAnsi.ANSI_PURPLE.escape() + color + ColorAnsi.RESET;
            case YELLOW: return ColorAnsi.ANSI_YELLOW.escape() + color + ColorAnsi.RESET;
        }
        return null;
    }

    private static void printCardView(CardView card) {
        System.out.println("VICTORY_POINTS: " + card.getVictoryPoints() + "\nCARD_ID: " + card.getCardID() + "\n");
    }

    private static void printCardSmallInfo(CardView card) {
        System.out.println("VICTORY_POINTS: " + card.getVictoryPoints() + "\n");
    }

    /**
     * @param cardID is DevelopmentCard's cardID
     *
     * CLI representation of Development Card.
     */
    public static void printDevelopmentCard(int cardID) {
        CardView card = CardMapCLI.getCard(cardID);
        System.out.println("\nDEVELOPMENT_CARD:\nCOLOR: " + printColor(card.getColor()) + "\nLEVEL: " + card.getLevel() +
                "\nRESOURCE_COST: " + printCost(card.getResourceCost()) + "\nPRODUCTION_POWER_RESOURCE_REQUIRED: " +
                printCost(card.getProductionPowerResourceRequired()) + "\nPRODUCTION_POWER_RESOURCE_GIVEN: " +
                printCost(card.getProductionPowerResourceGiven()) + "\nFAITH_POINTS_GIVEN: " + card.getFaithPointsGiven());
        printCardView(card);
    }

    /**
     * @param cardID is DevelopmentCard's cardID
     *
     * CLI representation of small info of Development Card
     */
    public static void printDevelopmentCardSmallInfo(int cardID) {
        CardView card = CardMapCLI.getCard(cardID);
        System.out.println("\nCOLOR: " + printColor(card.getColor()));
        printCardSmallInfo(card);
    }

    /**
     * @param cardID is LeaderCard's cardID
     *
     * CLI representation of Leader Card.
     */
    public static void printLeaderCard(int cardID) {
        if(cardID < 53)
            System.out.println("\nDISCOUNT_CARD");
        else if(cardID < 57)
            System.out.println("\nEXTRA_DEPOT_CARD");
        else if(cardID < 61)
            System.out.println("\nWHITE_CONVERSION_CARD");
        else
            System.out.println("\nADDITIONAL_PRODUCTION_POWER_CARD");
        CardView card = CardMapCLI.getCard(cardID);
        System.out.println("RESOURCE: " + fromResourceToString(card.getResource()));
        if(card.getResourceCost() != null)
            System.out.println("RESOURCE_COST: " + printCost(card.getResourceCost()));
        else if(card.getLeaderRequirements() != null)
            System.out.println("LEADER_REQUIREMENTS: " + printLeaderRequirements(card.getLeaderRequirements()));
        printCardView(card);
    }

    /**
     * @return CLI representation of Leader Requirement.
     */
    private static String printLeaderRequirements(LeaderRequirements leaderRequirements) {
        String cardRequirements = "";
        for(Color color: Color.values())
            if(leaderRequirements.getCardRequirement(color) != null)
                cardRequirements = cardRequirements + printCardRequirement(leaderRequirements.getCardRequirement(color));
        return cardRequirements;
    }

    /**
     * @return CLI representation of Card Requirement.
     */
    private static String printCardRequirement(CardRequirement cardRequirement) {
        if(cardRequirement.getNumOfCards() == 0)
            return "";
        String s1 = cardRequirement.getNumOfCards() + "";
        String s2 = null;
        switch (cardRequirement.getColor()) {
            case BLUE:
                s2 = (ColorAnsi.ANSI_CYAN.escape() + " █ " + ColorAnsi.RESET);
                break;
            case GREEN:
                s2 = (ColorAnsi.ANSI_GREEN.escape() + " █ " + ColorAnsi.RESET);
                break;
            case PURPLE:
                s2 = (ColorAnsi.ANSI_PURPLE.escape() + " █ " + ColorAnsi.RESET);
                break;
            case YELLOW:
                s2 = (ColorAnsi.ANSI_YELLOW.escape() + " █ " + ColorAnsi.RESET);
                break;
        }
        String s3 = null;
        if(cardRequirement.getNumOfCards() > 1){
            switch (cardRequirement.getColor()) {
                case BLUE:
                    s3 = (ColorAnsi.ANSI_CYAN.escape() + "█ " + ColorAnsi.RESET);
                    break;
                case GREEN:
                    s3 = (ColorAnsi.ANSI_GREEN.escape() + "█ " + ColorAnsi.RESET);
                    break;
                case PURPLE:
                    s3 = (ColorAnsi.ANSI_PURPLE.escape() + "█ " + ColorAnsi.RESET);
                    break;
                case YELLOW:
                    s3 = (ColorAnsi.ANSI_YELLOW.escape() + "█ " + ColorAnsi.RESET);
                    break;
            }
        }
        else if(cardRequirement.getLevels().get(0) == 2)
            s3 = "lvl 2";
        else
            s3 = "";
        return s1 + s2 + s3;
    }

    /**
     * @return CLI representation of Cost.
     */
    private static String printCost(Cost cost){
        String resourceContainers = "";
        for(Resource resource: Resource.values())
            if(cost.getResourceContainer(resource) != null)
                resourceContainers = resourceContainers + printResourceContainer(cost.getResourceContainer(resource));
        return resourceContainers;
    }

    /**
     * @return CLI representation of Resource Container.
     */
    private static String printResourceContainer(ResourceContainer resourceContainer){
        return fromResourceToString(resourceContainer.getResource()) + resourceContainer.getAmount();
    }

    /**
     * @param r is one resource.
     * @return Warehouse resource CLI representation.
     */
    private static String fromResourceToString(Resource r){
        switch (r){
            case COIN:
                return ColorAnsi.ANSI_YELLOW.escape() + "● " + ColorAnsi.RESET;
            case SHIELD:
                return ColorAnsi.ANSI_CYAN.escape() + "● " + ColorAnsi.RESET;
            case SERVANT:
                return ColorAnsi.ANSI_PURPLE.escape() + "● " + ColorAnsi.RESET;
            case STONE:
                return ColorAnsi.ANSI_WHITE.escape() + "● " + ColorAnsi.RESET;
            default:
                return "";
        }
    }

    /**
     * @param resource is a resource.
     * @return a CLI representation of @param resource.
     */
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
