package com.bestproject.main.CostumeClasses;

import com.bestproject.main.Game.GameCore;

public class Click {
    float currentTime =0;
    float x,y;
    boolean isInvalid=false;
    public void set(float x, float y){
        this.x=x;
        this.y=y;
        currentTime=0;
        isInvalid=false;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public void update(){
        if(currentTime>0.1f){
            isInvalid=true;
        } else{
            currentTime+= GameCore.deltatime;
        }
    }
    public boolean isValid(){
        return !isInvalid;
    }
    public void invalidate(){
        currentTime=10;
        isInvalid=true;
    }
}
