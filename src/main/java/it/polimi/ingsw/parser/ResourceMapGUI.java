package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.HashMap;

public class ResourceMapGUI {

    private static ResourceMapGUI resourceParser;
    private static HashMap<Resource, String> resourceMap = new HashMap<>();

    private ResourceMapGUI(){
        resourceMap.put(Resource.COIN, "src/main/resources/photos/coin.png");
        resourceMap.put(Resource.STONE, "src/main/resources/photos/stone.png");
        resourceMap.put(Resource.SHIELD, "src/main/resources/photos/shield.png");
        resourceMap.put(Resource.SERVANT,"src/main/resources/photos/servant.png");
    }

    public static String getResource(Resource r){
        if(resourceParser == null)
            resourceParser = new ResourceMapGUI();
        return resourceMap.get(r);
    }
}
