package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.StaticBuffer;

public class Building2 extends StaticObject{
    public Building2(Vector3 position) {
        super(new ModelInstance(StaticBuffer.currentModels.get(2)), position);
    }
    public Building2(Vector3 position, float rotation, float scale) {
        super(new ModelInstance(StaticBuffer.currentModels.get(2)), position);
        modelInstance.transform.setToScaling(scale,scale,scale);
        modelInstance.transform.rotate(0f,1f,0f,rotation);
    }
    @Override
    public int getRadius(){
        return 2;
    }
}
