module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    opens it.polimi.ingsw.view.GUI to javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.model.cards.developmentCards;
    exports it.polimi.ingsw.model.resourceContainers to com.google.gson;
    opens it.polimi.ingsw.model.resourceContainers to com.google.gson;
    opens it.polimi.ingsw.model.cards to com.google.gson;
    opens it.polimi.ingsw.model.cards.leaderCards to com.google.gson;
    opens it.polimi.ingsw.view.GUI.sceneController to javafx.fxml, javafx.graphics;
}