package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Flintlock extends Attack{
    public boolean isExplosion=false;
    public boolean expired=true;
    float flintlock=0.5f;
    float iterations=1;
    int chance=1;
    int result=-1;
    public Flintlock(ModelInstance model, Vector3 position) {
        super(model, position);
    }
    @Override
    public void update(){
        if(isExplosion){
            if(result==-1){
                Activate();
            }
            flintlock-=StaticQuickMAth.move(GameCore.deltatime);
            if(flintlock<=0){
                expired=true;
                reset();
            }
        }
    }

    public void setExplosion(boolean explosion) {
        isExplosion = explosion;
    }

    @Override
    public boolean expire(){
        return expired;
    }

    public void rollCoin() {
        if(iterations>3){
            return;
        }
        int mult= MathUtils.random(1);
        if(mult==0){
            chance+=1;
        } else{
            chance*=2;
        }
    }
    public void reset(){
        chance=1;
        isExplosion=false;
        flintlock=0.5f;
        result=-1;
    }
    public int getChance(){
        return chance;
    }
    public void Activate(){
        int chancing=MathUtils.random(1,chance);
        if(chancing==1){
            result=0;
            shoot();
        } else{
            boolean get_out=MathUtils.randomBoolean();
            if(get_out){
                result=1;
                explode();
            } else{
                result=2;
                didntWork();
            }
        }
    }
    private void shoot(){
        Vector3 cords= GameEngine.getGameCore().getMap().getNearestObjectCoordinates(StaticBuffer.getPlayerCooordinates(),GameEngine.getGameCore().getMap().calculate_radius(5,new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]}));
        GameEngine.getGameCore().getMap().addMoving(new EmptyAttackHitbox(cords,1,1,1,0.3f,1f,new float[]{20*(iterations*iterations),20*(iterations*iterations),20*(iterations*iterations)},true));
    }
    private void explode(){
        Vector3 cords= StaticBuffer.getPlayerCooordinates();
        GameEngine.getGameCore().getMap().addMoving(new EmptyAttackHitbox(cords,1,1,1,0.3f,1f,new float[]{5*(iterations*iterations),5*(iterations*iterations),5*(iterations*iterations)},false));
    }
    private void didntWork(){

    }
}
