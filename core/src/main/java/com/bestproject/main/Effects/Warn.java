package com.bestproject.main.Effects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;

public class Warn extends Effect{
    float animation=0;
    public Warn(Vector3 position, float lengh) {
        super(new ModelInstance(StaticBuffer.EffectBuffer.get(2)), position, lengh);
        lenght=0.5f;
        modelInstance.transform.rotate(0f,1f,0f, GameCore.cameraRoationm);
    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(modelInstance,environment);
        StaticShaders.texcoords_shader.end();
    }

    @Override
    public void update() {
        animation+= StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
        animation=animation%3f;
        lenght-=(StaticQuickMAth.move(GameEngine.gameCore.deltatime));
    }
}
