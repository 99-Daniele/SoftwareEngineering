package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.HashMap;

public class ResourceMapGUI {

    private static ResourceMapGUI resourceParser;
    private static final HashMap<Resource, String> resourceMap = new HashMap<>();

    private ResourceMapGUI(){
        resourceMap.put(Resource.COIN, "/photos/coin.png");
        resourceMap.put(Resource.STONE, "/photos/stone.png");
        resourceMap.put(Resource.SHIELD, "/photos/shield.png");
        resourceMap.put(Resource.SERVANT,"/photos/servant.png");
    }

    public static String getResource(Resource r){
        if(resourceParser == null)
            resourceParser = new ResourceMapGUI();
        return resourceMap.get(r);
    }
}
