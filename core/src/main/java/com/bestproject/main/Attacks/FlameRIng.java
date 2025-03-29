package com.bestproject.main.Attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class FlameRIng extends Attack{
    HITBOX[] hitboxes;
    public FlameRIng(Model model, Vector3 position) {
        super(new ModelInstance(model), position);
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z,position.y,5,5,1)};
        lengh=2f;
        modelInstance.transform.setTranslation(position);
        modelInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(modelInstance,environment);
    }
    @Override
    public void render(ModelBatch modelBatch){
    }
    @Override
    public void update(){
        lengh-= StaticQuickMAth.move(GameCore.deltatime);
        modelInstance.transform.setToScaling(1f+6f-lengh*3,1f+6f-lengh*3,1f+6f-lengh*3);
        modelInstance.transform.setTranslation(position);
        StaticBuffer.showHitbox.render(hitboxes);
    }
}
