package com.bestproject.main.EffectDecals;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticQuickMAth;

public class EffectBuffer {
    Array<Effect> array;
    public EffectBuffer(){
        array=new Array<>();
    }

    public void update(){
        for(int i=0; i<array.size; i++){
            array.get(i).update(StaticQuickMAth.move(GameCore.deltatime));
        }
        for(int i=0; i<array.size; i++){
            if(array.get(i).isExpired()) {
                array.removeIndex(i);
            }
        }
    }
    public void addeffect(Effect effect){
        array.add(effect);
    }
    public void getEffect(int index){

    }
    public void render(DecalBatch db){
        for(int i=0; i<array.size; i++){
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            array.get(i).render(db);
        }
    }
}
