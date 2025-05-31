package com.bestproject.main.Maps.InnerMap;

import com.badlogic.gdx.graphics.g3d.Model;
import com.bestproject.main.MapUtils.HITBOXMAP;

public class InnerMap {
    boolean isLoaded;
    String[] loadassets;
    Model model;
    HITBOXMAP hitboxmap;
    public InnerMap(String[] loadassets){
        this.loadassets=loadassets;
        isLoaded=false;
    }
    public void compile(){

    }

}
