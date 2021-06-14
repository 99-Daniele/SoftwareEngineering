package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.modelView.GameView;

import java.util.ArrayList;

public class CLIPrinter {

    public static void printMarket(GameView game){
        game.getMarket().printMarketCli();
    }

    public static void printRowMarket(GameView game, int row){
        game.getMarket().printRow(row);
    }

    public static void printColumnMarket(GameView game, int column){
        game.getMarket().printColumn(column);
    }

    public static void printMarbles(GameView game, ArrayList<Marble> marbles){
        game.getMarket().printMarbles(marbles);
    }

    public static void printDecks(GameView game){
        game.getDecks().print();
    }

    public static void printPlayerBoard(GameView game, int viewID){
        game.getPlayer(viewID).printCliAllPlayerBoard();
    }

    public static void printWarehouseStrongbox(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCLIWarehouseStrongbox();
    }

    public static void printWarehouse(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCliWarehouse();
    }

    public static void printStrongbox(GameView game, int viewID){
        game.getWarehouseStrongbox(viewID).printCliStrongbox();
    }

    public static void printCardSlot(GameView game, int viewID){
        game.getSlotCards(viewID).printCliSlot();
    }

    public static void printLeaderCard(GameView game, int viewID){
        game.getSlotCards(viewID).printCliLeaderCard();
    }

    public static void printFaithTrack(GameView game){
        game.getFaithTrack().printCli();
    }

    public static void printCard(int cardID){
        CardMapCLI.getCard(cardID).print();
    }

}
