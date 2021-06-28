package it.polimi.ingsw.view.modelView;

/**
 * CardView is the generic interface of Leader and Development Cards.
 */
public interface CardView {

    void print();

    void printSmallInfo();

    int getCardID();

    int getLevel();
}
