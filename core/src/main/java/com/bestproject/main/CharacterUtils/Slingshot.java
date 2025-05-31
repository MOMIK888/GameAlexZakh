package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.Weapons.SlingshotBullet;

public class Slingshot {
    SlingshotBullet[] bullets;
    int currentIndex=0;
    public Slingshot(){
        bullets=new SlingshotBullet[20];
        for(int i=0; i<20; i++){
            bullets[i]=new SlingshotBullet(StaticBuffer.Testmodel, new Vector3(), MathUtils.random(0.9f,1.1f),new float[]{0,0,0});
        }
    }
    public void Shoot(Vector3 direction, Vector3 position){
        bullets[currentIndex].setPosition(position);
        bullets[currentIndex].reset(position,MathUtils.random(0.9f,1.1f),direction);
        GameEngine.getGameCore().getMap().addMoving(bullets[currentIndex]);
        currentIndex+=1;
        currentIndex%=bullets.length;
    }

}
