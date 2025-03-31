package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class EmptyAttackHitbox extends Attack{
    ATKHITBOX[] hbs;
    public EmptyAttackHitbox(Vector3 position, float width, float height, float thickness, float pro, float invinFrames, float[] damage, boolean isenemy, float stunaframes) {
        super(null,position);
        lengh=pro;
        hbs=new ATKHITBOX[]{new ATKHITBOX(position.x,position.z,position.y,width,thickness,height,invinFrames,damage,isenemy)};
        hbs[0].setStunframes(stunaframes);
    }
    @Override
    public ATKHITBOX[] getAtkHbs() {
        return hbs;
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(hbs);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {

    }
    @Override
    public void render(ModelBatch modelBatch){

    }
    @Override
    public void update(){
        lengh-= StaticQuickMAth.move(GameCore.deltatime);
    }
    @Override
    public boolean expire(){
        return lengh<=0;
    }
}
