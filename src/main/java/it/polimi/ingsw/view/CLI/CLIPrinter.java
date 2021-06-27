package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.parser.CardMapCLI;
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
        game.getMarket().printMarketCli();
    }

    /**
     * @param game is current game.
     * @param row is a chosen row of the market
     *
     * print @param row of the market.
     */
    public static void printRowMarket(GameView game, int row){
        game.getMarket().printRow(row);
    }

    /**
     * @param game is current game.
     * @param column is a chosen column of the market
     *
     * print @param column of the market.
     */
    public static void printColumnMarket(GameView game, int column){
        game.getMarket().printColumn(column);
    }

    /**
     * @param game is current game.
     * @param marbles are player's chosen marbles.
     *
     * print @param marbles like a row of the market.
     */
    public static void printMarbles(GameView game, ArrayList<Marble> marbles){
        game.getMarket().printMarbles(marbles);
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
     * @param cardID is card.
     *
     * convert @param cardID to Card_View and print it.
     */
    public static void printCard(int cardID){
        CardMapCLI.getCard(cardID).print();
    }

}
