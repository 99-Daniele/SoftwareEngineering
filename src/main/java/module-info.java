module it.polimi.ingsw.view.GUI
        {

    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
            opens it.polimi.ingsw.view.GUI to javafx.fxml, javafx.graphics;
            exports it.polimi.ingsw.view.GUI;

        }