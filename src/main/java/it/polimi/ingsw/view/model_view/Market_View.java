package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.market.Marble;

public interface Market_View {

    void slideColumn(int selectedColumn);

    void slideRow(int selectedRow);

    void printMarketCLI();

    Marble[] getRowMarbles(int row);

    void printColumn(int column);

    void printMarbles(ArrayList<Marble> marbles);
}
