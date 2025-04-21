package com.bestproject.main.Databases;

public class MapInfoDecimation {
    String[] info=null;
    String fullmaterial;
    public MapInfoDecimation(String breakdownMaterial){
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
    public String[] getInfo(){
        return info;
    }
}
