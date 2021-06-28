package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

/**
 * CardView is the generic interface of Market.
 */
public interface MarketView {

    void slideColumn(int selectedColumn);

    void slideRow(int selectedRow);

    void printMarketCli();

    void printRow(int row);

    void printColumn(int column);

    void printMarbles(ArrayList<Marble> marbles);

    Marble getMarble(int row, int column);

    Marble getExternalMarble();
}
