package com.bestproject.main.Tiles;

import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectFragment.HITBOX;

public class Tile implements Disposable{
    protected ModelInstance modelInstance;
    protected Vector3 position;

    public Tile(ModelInstance model, Vector3 position) {
        this.position = new Vector3(position);
        this.modelInstance = model;
        this.modelInstance.transform.setTranslation(position);
    }
    public float getGroundLevel(){
        return 0;
    }
    public HITBOX[] gethb(){
        return null;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
        modelInstance.transform.setTranslation(position);
    }
    public static void disposeAll(){

    }

    @Override
    public void dispose() {

    }
}
