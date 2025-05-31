package com.bestproject.main.Weapons;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Attacks.Attack;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Rocket extends Attack {
    ATKHITBOX[] hitboxes;
    boolean esExploded=false;
    float speed=4f;
    float explodeLengh=0.3f;
    boolean exploding=false;
    Vector3 direction;
    public Rocket(Model model, Vector3 position, float scale, float[] flingdir) {
        super(new ModelInstance(model), position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        direction=new Vector3(flingdir[0],flingdir[1],flingdir[2]);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=4f;
        setPosition(position);

    }
    public Rocket(Model model, Vector3 position, float scale, float[] flingdir, boolean isStatic) {
        super(new ModelInstance(model), position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position,0.3f*scale,1f,new float[]{10f*scale,10f*scale,10f*scale},true)};
        hitboxes[0].setCrit_multiplier(1.5f);
        direction=new Vector3(flingdir[0],flingdir[1],flingdir[2]);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=0f;

    }
    @Override
    public ATKHITBOX[] getAtkHbs(){
        if(exploding) {
            hitboxes[1].setZ(position.y);
            hitboxes[1].setY(position.z);
            hitboxes[1].setX(position.x);
            hitboxes[0].setZ(position.y);
            hitboxes[0].setY(position.z);
            hitboxes[0].setX(position.x);
            return hitboxes;
        } else{
            return null;
        }
    }

    @Override
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            Vector3 previos_position=new Vector3(position);
            setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
            hitboxes[0].setZ(position.y);
            hitboxes[0].setY(position.z);
            hitboxes[0].setX(position.x);
            GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        hitboxes[0].setZ(position.y);
        hitboxes[0].setY(position.z);
        hitboxes[0].setX(position.x);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        Vector3 previos_position=new Vector3(position);
        setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
        GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);

    }
    public void reset(Vector3 position, float scale, Vector3 flingdir){
        setPosition(position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x,position.z,position.y,0.4f,0.4f,0.4f,3f,new float[]{20f,20f,20f},true),new ATKHITBOX(position.x,position.z,position.y,0.3f,0.3f,0.3f,0.05f,new float[]{0f,0f,0f},false) {
            @Override
            public void invalidate(int index){
                super.invalidate(index);
                setForce(0);
            }
        }};
        hitboxes[0].setForce(10);
        hitboxes[1].setForce(20);
        hitboxes[0].setStunframes(10f);
        direction=new Vector3(flingdir);
        StaticQuickMAth.normalizeSpeed(direction);
        lengh=4f;
        exploding=false;
        speed=8f;
        esExploded=false;
        explodeLengh=0.3f;
        modelInstance.transform.setToScaling(scale*0.3f,scale*0.3f,scale*0.3f);
        setPosition(position);
    }
    @Override
    public void update(){
        lengh-=StaticQuickMAth.move(GameCore.deltatime);
        if(!exploding) {
            fractureMovement(new Vector3(StaticQuickMAth.move(speed * direction.x * GameCore.deltatime), StaticQuickMAth.move(speed * direction.y * GameCore.deltatime), StaticQuickMAth.move(speed * direction.z * GameCore.deltatime)));
        }
        movement.set(0,0f,0);
        fractureMovement(new Vector3());
        if(exploding){
            explodeLengh-=StaticQuickMAth.move(GameCore.deltatime);
            hitboxes[0].width=3-explodeLengh*8;
            hitboxes[0].thickness=3-explodeLengh*8;
            hitboxes[0].height=3-explodeLengh*8;
            hitboxes[1].width=3-explodeLengh*8;
            hitboxes[1].thickness=3-explodeLengh*8;
            hitboxes[1].height=3-explodeLengh*8;
            hitboxes[1].setZ(position.y);
            hitboxes[1].setY(position.z);
            hitboxes[1].setX(position.x);
            hitboxes[0].setZ(position.y);
            hitboxes[0].setY(position.z);
            hitboxes[0].setX(position.x);
            if(explodeLengh<=0){
                esExploded=true;
            }
        }
    }
    @Override
    public void HITBOXINTERRACTION(HITBOX[] hitboxes) {
        if(hitboxes!=null){
            for(int i=0; i<hitboxes.length; i++){
                if(hitboxes[i].colliderectangles(this.hitboxes[0])){
                    explode();
                }

            }
        }
        if(this.hitboxes[0].getBottom()<GameEngine.getGameCore().getMap().GetGroundLevel(this.getPosition())){
            explode();
        }
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        super.render(modelBatch, environment);
    }

    public void explode(){
        exploding=true;
        direction=direction.setZero();
        speed=0;
    }
    @Override
    public boolean expire(){
        boolean ans=lengh<=0;
        if(ans && !exploding){
            explode();
        }
        return esExploded;
    }
}
