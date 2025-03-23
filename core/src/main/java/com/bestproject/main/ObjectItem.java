package com.bestproject.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;

public class ObjectItem implements Disposable{
    public int shaderIndex=0;
    public ModelInstance modelInstance; //undisposable
    public Vector3 position;
    protected int unique_index;

    public ObjectItem (ModelInstance model, Vector3 position) {
        this.position = new Vector3(position);
        unique_index=StaticBuffer.getUnique_index();
        inialize(model);
    }
    public void inialize(ModelInstance model){
        this.modelInstance = model;
        if(this.modelInstance!=null){
            this.modelInstance.transform.setTranslation(position);
        } else {
            System.gc();
        }
    }
    public void update(){

    }
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(modelInstance,environment);
    }
    public int getType(){
        return 0;
    }
    public int getUnique_index(){
        return unique_index;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }
    public void render() {
    }
    public HITBOX[] gethbs(){
        return null;
    }
    public ATKHITBOX[] getAtkHbs(){
        return null;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
        modelInstance.transform.setTranslation(position);
    }
    public void RenderHitboxes(){

    }

    @Override
    public void dispose() {
    }
    public void setShaderIndex(int shaderIndex) {
        this.shaderIndex = shaderIndex;
    }
    public boolean FLOOORHITBOXINTERRACTION(HITBOX[] hitboxes){
        return true;
    }
    public void HITBOXINTERRACTION(HITBOX[] hitboxes){

    }
    public void ATKHITBOXINTERRACTIONS(ATKHITBOX[] atkhitboxes){

    }
    public boolean expire(){
        return false;
    }
    public int getRadius(){
        return 1;
    }
}
