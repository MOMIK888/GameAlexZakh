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

public class Torch extends StaticObject{
    boolean isFlaming=false;
    public Torch(Vector3 position) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(4))), position);
        modelInstance.transform.scale(0.3f,0.3f,0.3f);
    }
    public Torch(Vector3 position, boolean flag,Vector3 scale, Vector3 rotation, float deg) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(4))), position);
        modelInstance.transform.scale(0.3f,0.3f,0.3f);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(modelInstance,environment);

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
