package com.bestproject.main.Weapons;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Attacks.Attack;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticQuickMAth;

public class SlingshotBullet extends Attack  {
    ATKHITBOX[] hitboxes;
    float[] normals=new float[]{1,1,1};
    float speed=30f;
    Vector3 direction;
    public SlingshotBullet(Model model, Vector3 position, float scale, float[] flingdir) {
        super(new ModelInstance(model), position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        direction=new Vector3(flingdir[0],flingdir[1],flingdir[2]);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=2f;
        setPosition(position);

    }
    public SlingshotBullet(Model model, Vector3 position, float scale, float[] flingdir, boolean isStatic) {
        super(new ModelInstance(model), position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        direction=new Vector3(flingdir[0],flingdir[1],flingdir[2]);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=0f;

    }
    @Override
    public ATKHITBOX[] getAtkHbs(){
        return hitboxes;
    }

    @Override
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            movement.y*=normals[1];
            direction.y*=normals[1];
            movement_fragment.y*=normals[1];
            Vector3 previos_position=new Vector3(position);
            setPosition(new Vector3(normals[0]*movement.x+position.x,position.y+movement.y,normals[2]*movement.z+position.z));
            hitboxes[0].setZ(position.y);
            hitboxes[0].setY(position.z);
            hitboxes[0].setX(position.x);
            GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);
            normals[1]=Math.abs(normals[1]);
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        movement.y*=normals[1];
        direction.y*=normals[1];
        movement_fragment.y*=normals[1];
        Vector3 previos_position=new Vector3(position);
        setPosition(new Vector3(normals[0]*movement.x+position.x,position.y+movement.y,normals[2]*movement.z+position.z));
        hitboxes[0].setZ(position.y);
        hitboxes[0].setY(position.z);
        hitboxes[0].setX(position.x);
        GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);
        normals[1]=Math.abs(normals[1]);

    }
    public void reset(Vector3 position, float scale, float[] flingdir){
        setPosition(position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        direction=new Vector3(flingdir[0],flingdir[1],flingdir[2]);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=2f;
    }
    public void reset(Vector3 position, float scale, Vector3 flingdir){
        setPosition(position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x,position.z,position.y,0.3f*scale,0.3f*scale,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        normals=new float[]{1,1,1};
        direction=new Vector3(flingdir);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=2f;
        modelInstance.transform.setToScaling(scale*0.2f,scale*0.2f,scale*0.2f);
        setPosition(position);
    }
    @Override
    public void update(){
        lengh-=StaticQuickMAth.move(GameCore.deltatime);
        direction.y-=StaticQuickMAth.getGravityAcceleration();
        fractureMovement(new Vector3(StaticQuickMAth.move(speed*direction.x* GameCore.deltatime), StaticQuickMAth.move(speed*direction.y* GameCore.deltatime), StaticQuickMAth.move(speed*direction.z*GameCore.deltatime)));
        movement.set(0,0f,0);
    }
    @Override
    public void HITBOXINTERRACTION(HITBOX[] hitboxes) {
        if(hitboxes!=null){
            for(int i=0; i<hitboxes.length; i++){
                hitboxes[i].Bounce(this.hitboxes[0], normals,direction);

            }
        }
        if(this.hitboxes[0].getBottom()<GameEngine.getGameCore().getMap().GetGroundLevel(this.getPosition()) && direction.y*normals[1]<0){
            normals[1]*=-1;
        }
    }
    @Override
    public boolean expire(){
        return lengh<=0;
    }
}
