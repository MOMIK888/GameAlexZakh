package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;

public class Flame extends StaticObject{
    float animation=0f;
    public Flame(Vector3 position) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(3))), position);
        modelInstance.transform.scale(0.8f,0.8f,0.8f);
        modelInstance.transform.rotate(0f,0f,1f,180);
        modelInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
    public Flame(Vector3 position, Vector3 scale, Vector3 rotation, float deg) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(3))), position);
        modelInstance.transform.scale(0.8f*scale.x,0.8f*scale.y,0.8f*scale.z);
        modelInstance.transform.rotate(rotation.x,rotation.y,rotation.z,180+deg);
        modelInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {

        StaticShaders.texcoords_shader.begin(GameCore.camera,modelBatch.getRenderContext());
        StaticShaders.texcoords_shader.setOffset((int)(animation*8)*0.125f,-0.85f+(int)(animation*2)*0.125f);
        modelBatch.render(modelInstance,environment,StaticShaders.texcoords_shader);
        StaticShaders.texcoords_shader.end();

    }

    @Override
    public void update() {
        animation+= StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
        animation=animation%3f;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
