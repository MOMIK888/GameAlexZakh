package com.bestproject.main.Effects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;

public class SnapFlash extends Effect{
    public SnapFlash(Vector3 position, float lengh) {
        super(StaticBuffer.getEffectfromBuffer(0), position, lengh);
        modelInstance.transform.rotate(0f,0f,0f,90);
        modelInstance.transform.rotate(1f,0f,0f,-90);
        modelInstance.transform.scale(0.2f,0.2f,0.2f);
        modelInstance.transform.rotate(0f,0f,1f, GameCore.cameraRoationm);
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("Circle|Action",1);
        lenght=0.3f;
    }
    @Override
    public boolean expire(){
        return lenght<=0;
    }

}
