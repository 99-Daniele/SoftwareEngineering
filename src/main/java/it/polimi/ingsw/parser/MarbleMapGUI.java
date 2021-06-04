package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.market.Marble;

import java.util.HashMap;

public class MarbleMapGUI {

    private static MarbleMapGUI marbleParser;
    private static HashMap<String, String> marbleMap = new HashMap<>();
    private MarbleMapGUI(){
        marbleMap.put("RED","src/main/resources/photos/rossa.png");
        marbleMap.put("PURPLE","src/main/resources/photos/viola.png");
        marbleMap.put("WHITE", "src/main/resources/photos/bianca.png");
        marbleMap.put("BLUE","src/main/resources/photos/blu.png");
        marbleMap.put("GREY","src/main/resources/photos/grigio.png");
        marbleMap.put("YELLOW","src/main/resources/photos/gialla.png");
    }
    public static String getMarble(Marble marbleID){
        if(marbleParser == null)
            marbleParser = new MarbleMapGUI();
        return marbleMap.get(marbleID.toString());
    }
}
