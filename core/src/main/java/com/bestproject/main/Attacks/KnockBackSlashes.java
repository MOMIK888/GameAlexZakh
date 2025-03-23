package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class KnockBackSlashes extends Attack{
    float speed=4f;
    Vector3 sincos;
    ATKHITBOX[] hitboxes;
    public KnockBackSlashes(Vector3 position, float[] flingdir) {
        super(null, position);
        movement=new Vector3();
        sincos=new Vector3(flingdir);
        lengh=10f;
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x, position.z, position.y, 1.5f,0.3f,1.5f,1f,new float[]{10,10,10},true)};
        StaticQuickMAth.normalizeSpeed(sincos);
        hitboxes[0].rotate(new Vector2(flingdir[0],flingdir[1]).angleDeg());
    }
    @Override
    public void setPosition(Vector3 position){
        this.position.set(position);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.renderWithRotation(hitboxes);
    }
    @Override
    public void render(ModelBatch batch, Environment environment){

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

    @Override
    public void update() {
        lengh-=StaticQuickMAth.move(GameCore.deltatime);
        fractureMovement(new Vector3(StaticQuickMAth.move(sincos.x*speed* GameCore.deltatime),StaticQuickMAth.move(sincos.y*speed* GameCore.deltatime),StaticQuickMAth.move(sincos.z*speed* GameCore.deltatime)));
    }
}
