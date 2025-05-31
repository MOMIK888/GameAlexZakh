package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CostumeClasses.SpriteSheet;
import com.bestproject.main.ObjectFragment.ATKHITBOX;

public class Explosion {
    ATKHITBOX atkhitbox;
    Decal decal;
    Vector3 position;
    Vector3 imaginatyPosition;
    float delta=0;
    float lengh=1f;
    int num;
    float radius;
    public Explosion(SpriteSheet spriteSheet){
        num=spriteSheet.getNumFrames();
    }
    public ATKHITBOX getAtkhitbox(){
        return atkhitbox;
    }
    public void update(float deltatime, SpriteSheet spriteSheet){
        delta+=deltatime;
        decal.setTextureRegion(spriteSheet.getFrame((int)(delta/(lengh/spriteSheet.getNumFrames()))));
    }
    public boolean expire(){
        return delta>(lengh+0.04f);
    }
    public void draw(DecalBatch decalBatch){

    }
}
