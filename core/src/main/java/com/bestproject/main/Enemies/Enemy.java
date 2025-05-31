package com.bestproject.main.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.MovingObjects.MovingObject;

public class Enemy extends MovingObject {
    float invinFrames=0f;
    float stunFrames;
    float speed=1f;
    public static Color crit=new Color(Color.RED);
    public static Color base=new Color(Color.WHITE);
    public Enemy(ModelInstance model, Vector3 position) {
        super(model, position);
        islIneArt=true;
    }
    @Override
    public int getType(){
        return 2;
    }
    public void setStunFrames(float stun){
        if(stunFrames<stun){
            stunFrames=stun;
        }
    }
}
