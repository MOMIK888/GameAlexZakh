package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class FlameRIng extends Attack{
    HITBOX[] hitboxes;
    public FlameRIng(Vector3 position) {
        super(null, position);
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z,position.y,5,5,1)};
        lengh=2f;
    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){

    }
    @Override
    public void render(ModelBatch modelBatch){

    }
    @Override
    public void update(){
        lengh-= StaticQuickMAth.move(GameCore.deltatime);
        StaticBuffer.showHitbox.render(hitboxes);
    }
}
