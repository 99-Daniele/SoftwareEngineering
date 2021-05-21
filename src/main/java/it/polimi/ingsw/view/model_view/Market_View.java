package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.market.Marble;

import java.util.ArrayList;

public interface Market_View {

    void slideColumn(int selectedColumn);

    void slideRow(int selectedRow);

    void printMarketCLI();

    void printRow(int row);

    void printColumn(int column);

    void printMarbles(ArrayList<Marble> marbles);
}
