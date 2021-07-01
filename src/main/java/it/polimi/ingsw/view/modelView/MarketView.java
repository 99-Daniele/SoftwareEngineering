package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

/**
 * CardView is the generic interface of Market.
 */
public interface MarketView {

    void slideColumn(int selectedColumn);

    void slideRow(int selectedRow);

    Marble[][] getMarketTray();

    Marble getMarble(int row, int column);

    Marble getExternalMarble();
}
