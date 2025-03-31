package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.ParryHitbox;
import com.bestproject.main.StaticQuickMAth;

public class Blast extends Attack{
    AnimationController animationController;
    float speed=4f;
    float[] sincos;
    ATKHITBOX[] hitboxes;
    public Blast(ModelInstance model, Vector3 position, float[] flingdir) {
        super(model, position);
        movement=new Vector3();
        sincos=flingdir.clone();
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("Armature|fly",-1);
        modelInstance.transform.setToRotation(0f,1f,0f, (float) -Math.toDegrees(Math.atan2(flingdir[2],flingdir[0])));
        lengh=1f;
        modelInstance.transform.scale(0.002f,0.002f,0.002f);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x, position.z, position.y, 0.1f,0.1f,0.2f,1f,new float[]{10,10,10},true)};
        hitboxes[0].setDoorOpening(true);
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
        super.update();
        if(hitboxes[0].getIsCol()){
            lengh=0;
        }
        fractureMovement(new Vector3(StaticQuickMAth.move(sincos[0]*speed*GameCore.deltatime),StaticQuickMAth.move(sincos[1]*speed*GameCore.deltatime),StaticQuickMAth.move(sincos[2]*speed* GameCore.deltatime)));
        animationController.update(StaticQuickMAth.move(GameEngine.getGameCore().deltatime));
    }
}
