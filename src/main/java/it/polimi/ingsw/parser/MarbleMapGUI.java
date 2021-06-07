package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.market.Marble;

import java.util.HashMap;

public class MarbleMapGUI {

    private static MarbleMapGUI marbleParser;
    private static HashMap<String, String> marbleMap = new HashMap<>();
    private MarbleMapGUI(){
        marbleMap.put("RED","/photos/rossa.png");
        marbleMap.put("PURPLE","/photos/viola.png");
        marbleMap.put("WHITE", "/photos/bianca.png");
        marbleMap.put("BLUE","/photos/blu.png");
        marbleMap.put("GREY","/photos/grigio.png");
        marbleMap.put("YELLOW","/photos/gialla.png");
    }
    public static String getMarble(Marble marbleID){
        if(marbleParser == null)
            marbleParser = new MarbleMapGUI();
        return marbleMap.get(marbleID.toString());
    }
}
