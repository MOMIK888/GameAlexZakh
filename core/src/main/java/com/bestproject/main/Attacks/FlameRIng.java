package com.bestproject.main.Attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.EffectDecals.FireEffect;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;

public class FlameRIng extends Attack{
    ATKHITBOX[] hitboxes;
    FireEffect fireEffect;
    public FlameRIng(Model model, Vector3 position) {
        super(new ModelInstance(model), position);
        hitboxes=new ATKHITBOX[]{new ATKHITBOX(position.x, position.z,position.y,1,1,1,1f,new float[]{10,10,10},false)};
        lengh=0.2f;
        fireEffect=new FireEffect(lengh+1f,this.position,0.5f,20);
        StaticBuffer.effectBuffer.addeffect(fireEffect);
        modelInstance.transform.setTranslation(position);
        modelInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){
        StaticShaders.texcoords_shader.begin(modelBatch.getCamera(),modelBatch.getRenderContext());
        StaticShaders.texcoords_shader.setOffset(1-lengh*5,0);
        modelBatch.render(modelInstance,environment,StaticShaders.texcoords_shader);
        StaticShaders.texcoords_shader.end();
    }
    @Override
    public void render(ModelBatch modelBatch){
    }
    @Override
    public void update(){
        lengh-= StaticQuickMAth.move(GameCore.deltatime);
        hitboxes[0].width=5-lengh*25;
        hitboxes[0].thickness=5-lengh*25;
        modelInstance.transform.setToScaling(1f+10f-lengh*50f,1f+10f-lengh*50f,1f+10f-lengh*50f);
        modelInstance.transform.setTranslation(position);
        GameEngine.gameCore.getMap().ForcedHitboxInterraction(this);
        fireEffect.setEmissionArea(5-lengh*25);
        fireEffect.setCapacity((int) (20+(60-lengh*300)));
    }
    @Override
    public ATKHITBOX[] getAtkHbs(){
        return hitboxes;
    }
    @Override
    public int getRadius(){
        return 2;
    }
}
