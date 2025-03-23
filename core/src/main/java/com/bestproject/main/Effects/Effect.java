package com.bestproject.main.Effects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;

public class Effect extends StaticObject {
    float lenght;
    AnimationController animationController; //undisposable
    public Effect(ModelInstance model, Vector3 position, float lengh) {
        super(model, position);
        lengh=lenght;
    }

    @Override
    public void update(){
        lenght-=(StaticQuickMAth.move(GameEngine.gameCore.deltatime));
        animationController.update(StaticQuickMAth.move(GameEngine.gameCore.deltatime));

    }
    @Override
    public boolean expire(){
        return lenght<=0;
    }

}
