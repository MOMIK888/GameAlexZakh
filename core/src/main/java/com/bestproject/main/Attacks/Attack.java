package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.StaticQuickMAth;

public class Attack extends MovingObject {
    public float lengh;

    public Attack(ModelInstance model, Vector3 position) {
        super(model, position);
    }
    @Override
    public void update(){
        lengh-= StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
    }
    @Override
    public boolean expire(){
        return lengh<=0;
    }
    public void pluslengh(float plustimer){
        lengh+=plustimer;
    }
    @Override
    public int getType(){
        return -50;
    }
}
