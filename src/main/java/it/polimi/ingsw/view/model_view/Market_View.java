package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.market.Marble;

public interface Market_View {

    void slideColumn(int selectedColumn);

    void slideRow(int selectedRow);

    void printMarket();

    void printMarketCli();

    Marble[] getRowMarbles(int row);

    Marble[] getColumnMarbles(int column);
}
