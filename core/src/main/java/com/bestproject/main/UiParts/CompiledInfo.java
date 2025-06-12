package com.bestproject.main.UiParts;

import com.badlogic.gdx.math.Vector3;

public class CompiledInfo {
    public Vector3 creationLocation;
    public float time;
    public int mapIndex;
    public int[] movesets;
    public float[] movesetshp;
    public float[] movesetUltCharge;
    public boolean isLocationFetched = false;
    public CompiledInfo(){

    }
    public CompiledInfo(boolean isInstant){
        initialize();
    }

    public void initialize() {
        creationLocation = new Vector3();
        mapIndex = -1;
        time = 0f;
        movesets = new int[0];
        movesetshp = new float[0];
        movesetUltCharge = new float[0];
    }

    public Vector3 getPlayerLocation() {
        isLocationFetched = true;
        return creationLocation;
    }
    public void setDefault(){
        isLocationFetched=true;
        mapIndex=2;
        time=6f;
        movesets=new int[]{1};
        movesetshp = new float[]{10000};
        movesetUltCharge = new float[]{0};
    }
}

