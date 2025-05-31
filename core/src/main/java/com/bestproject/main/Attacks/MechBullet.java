package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CostumeClasses.SpriteSheetDecal;
import com.bestproject.main.EffectDecals.FireEffect;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class MechBullet extends Attack {
    float speed=10f;
    Vector3 sincos;
    ATKHITBOX[] hitboxes;
    Decal deeeecal;
    Decal trail;
    float maxlengh=1f;
    public MechBullet(Vector3 position, float[] flingdir, TextureRegion txt) {
        super(null, position);
        movement=new Vector3();
        sincos=new Vector3(flingdir);
        deeeecal=Decal.newDecal(txt,true);
        lengh=-1;
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x, position.z, position.y, 0.1f,0.1f,0.1f,1f,new float[]{10,10,10},false)};
        StaticQuickMAth.normalizeSpeed(sincos);
    }
    @Override
    public int getRadius(){
        return 3;
    }
    public void setPosition(Vector3 position){
        this.position.set(position);
        this.deeeecal.setPosition(position);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.renderWithRotation(hitboxes);
    }
    @Override
    public void render(ModelBatch batch, Environment environment){
        deeeecal.setColor(new Color(1f,1f,1f,lengh/maxlengh));
        StaticBuffer.decalBatch.add(deeeecal);
    }
    @Override
    public void render(ModelBatch batch){

    }
    @Override
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
            hitboxes[0].setX(position.x);
            hitboxes[0].setY(position.z);
            hitboxes[0].setZ(position.y);
            GameEngine.getGameCore().getMap().ForcedMovingRearr(this.position);
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
        hitboxes[0].setX(position.x);
        hitboxes[0].setY(position.z);
        hitboxes[0].setZ(position.y);
        GameEngine.getGameCore().getMap().ForcedMovingRearr(this.position);

    }
    @Override
    public ATKHITBOX[] getAtkHbs() {
        return hitboxes;
    }
    public void reset(Vector3 position, float[] flingdir, TextureRegion txt){
        setPosition(position);
        sincos=sincos.set(flingdir);
        deeeecal=Decal.newDecal(txt,true);
        lengh=maxlengh;
        StaticQuickMAth.normalizeSpeed(sincos);
    }
    public void reset(Vector3 position, Vector3 flingdir, TextureRegion txt){
        setPosition(position);
        sincos=sincos.set(flingdir);
        deeeecal=Decal.newDecal(txt,true);
        deeeecal.setDimensions(0.1f,0.1f);
        lengh=maxlengh;
        StaticQuickMAth.normalizeSpeed(sincos);
    }

    @Override
    public void update() {
        deeeecal.lookAt(StaticBuffer.getPlayerCooordinates(),Vector3.Y);
        lengh-=StaticQuickMAth.move(GameCore.deltatime);
        fractureMovement(new Vector3(StaticQuickMAth.move(sincos.x*speed* GameCore.deltatime),StaticQuickMAth.move(sincos.y*speed* GameCore.deltatime),StaticQuickMAth.move(sincos.z*speed* GameCore.deltatime)));
    }
}
