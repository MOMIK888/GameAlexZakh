package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.Weapons.Rocket;
import com.bestproject.main.Weapons.SlingshotBullet;

public class RocketLauncher {
    Rocket[] rocket;
    int currentIndex=0;
    public RocketLauncher(){
        rocket=new Rocket[2];
        for(int i=0; i<2; i++){
            rocket[i]=new Rocket(StaticBuffer.Testmodel, new Vector3(), 1,new float[]{0,0,0});
        }
    }
    public void Shoot(Vector3 position, Vector3 direction){
        currentIndex+=1;
        currentIndex=currentIndex%2;
        rocket[currentIndex].setPosition(position);
        rocket[currentIndex].reset(position, 1,direction);
        GameEngine.getGameCore().getMap().addMoving(rocket[currentIndex]);
    }
}
