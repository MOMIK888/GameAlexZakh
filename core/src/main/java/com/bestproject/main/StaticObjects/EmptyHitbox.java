package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class EmptyHitbox extends StaticObject{
    HITBOX[] hbs;
    public EmptyHitbox(Vector3 position, float width, float height, float thickness) {
        super(null,position);
        hbs=new HITBOX[]{new HITBOX(position.x,position.z,position.y,width,thickness,height)};
    }
    @Override
    public HITBOX[] gethbs() {
        return hbs;
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(hbs);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {

    }
}
