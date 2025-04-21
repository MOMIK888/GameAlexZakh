package com.bestproject.main.Moveset;

import com.badlogic.gdx.utils.Array;

public class PlayerInfoBreakdown {
    String[] info=null;
    String fullmaterial;
    public PlayerInfoBreakdown(String breakdownMaterial){
        if(breakdownMaterial!=null) {
            info = breakdownMaterial.split("&");
            fullmaterial = breakdownMaterial;
        }
    }
    public String getInfo(int index){
        if(info!=null){
            if(index<info.length){
                return info[index];
            } else{
                return null;
            }
        } else{
            return null;
        }
    }
}
