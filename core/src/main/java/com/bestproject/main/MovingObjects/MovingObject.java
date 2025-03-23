package com.bestproject.main.MovingObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectItem;

import java.util.ArrayList;

public class MovingObject extends ObjectItem implements Disposable {
    public boolean isImpact=false;
    ArrayList<Color> tempcolors=new ArrayList<>();
    public Vector3 movement=new Vector3();
    public MovingObject (ModelInstance model, Vector3 position) {
        super(model, position);
    }
    @Override
    public void update(){

    }
    public void setImpact(boolean value){
        isImpact=value;
    }
    public boolean isImpact(){
        return isImpact;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }
    @Override
    public void render() {
    }
    public void render(ModelBatch mb){
        mb.render(modelInstance);
    }
    public void resetColors(){
        isImpact=true;
        for(int i=0; i<tempcolors.size(); i++){
            if(i<modelInstance.materials.size) {
                modelInstance.materials.get(i).set(ColorAttribute.createDiffuse(tempcolors.get(i)));
            }
        }
        tempcolors.clear();
    }
    @Override
    public void RenderHitboxes(){

    }
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));

    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position=new Vector3(position.x,position.y,position.z);
        modelInstance.transform.setTranslation(this.position);
    }
    public void setTempAttribute(Color color){
        tempcolors.add(color);
    }

    @Override
    public void dispose() {

    }
    public void setCostume(boolean val){

    }
}
