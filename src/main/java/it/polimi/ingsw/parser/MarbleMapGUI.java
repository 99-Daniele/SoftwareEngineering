package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.market.Marble;

import java.util.HashMap;

/**
 * MarbleMapGUI is an HashMap which converts a marble to file string of chosen marble.
 */
public class MarbleMapGUI {

    private static MarbleMapGUI marbleParser;
    private static final HashMap<String, String> marbleMap = new HashMap<>();

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
