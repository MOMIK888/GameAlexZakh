package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectItem;

public class StaticObject extends ObjectItem implements Disposable {
    public StaticObject(ModelInstance model, Vector3 position) {
        super(model, position);
    }

    @Override
    public void dispose() {

    }
    public void render(ModelBatch mb){
        if(modelInstance!=null) {
            mb.render(modelInstance);
        }
    }
}
