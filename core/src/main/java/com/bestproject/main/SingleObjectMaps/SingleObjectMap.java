package com.bestproject.main.SingleObjectMaps;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class SingleObjectMap implements Disposable {
    private Model model;
    private ModelInstance modelInstance;
    public SingleObjectMap(Model newmodel){
        this.model=newmodel;
        this.modelInstance=new ModelInstance(model);
    }
    public void scale(float x, float y, float z){
        modelInstance.transform.scale(x,y,z);
    }
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(modelInstance,environment);
    }
    public ModelInstance getModelInstance(){
        return modelInstance;
    }
    public void render(ModelBatch modelBatch){
        modelBatch.render(modelInstance);
    }
    public void setPosition(Vector3 position){
        modelInstance.transform.setTranslation(position);
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }
}
