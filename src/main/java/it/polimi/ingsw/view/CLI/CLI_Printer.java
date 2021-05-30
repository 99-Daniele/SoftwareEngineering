package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.model_view.Game_View;

import java.util.ArrayList;

public class CLI_Printer {

    public static void printMarket(Game_View game){
        game.getMarket().printMarketCli();
    }

    public static void printRowMarket(Game_View game, int row){
        game.getMarket().printRow(row);
    }

    public static void printColumnMarket(Game_View game, int column){
        game.getMarket().printColumn(column);
    }

    public static void printMarbles(Game_View game, ArrayList<Marble> marbles){
        game.getMarket().printMarbles(marbles);
    }

    public static void printDecks(Game_View game){
        game.getDecks().print();
    }

    public static void printPlayerBoard(Game_View game, int viewID){
        game.getPlayer(viewID).printCliAllPlayerBoard();
    }

    public static void printWarehouseStrongbox(Game_View game, int viewID){
        game.getWarehouseStrongbox(viewID).printCLIWarehouseStrongbox();
    }

    public static void printWarehouse(Game_View game, int viewID){
        game.getWarehouseStrongbox(viewID).printCliWarehouse();
    }

    public static void printCardSlot(Game_View game, int viewID){
        game.getSlotCards(viewID).printCliSlot();
    }

    public static void printLeaderCard(Game_View game, int viewID){
        game.getSlotCards(viewID).printCliLeaderCard();
    }

    public static void printFaithTrack(Game_View game){
        game.getFaithTrack().printCli();
    }

    public static void printCard(int cardID){
        CardMapCLI.getCard(cardID).print();
    }

}
