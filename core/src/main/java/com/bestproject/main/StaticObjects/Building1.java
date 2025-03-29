package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.StaticBuffer;

public class Building1 extends StaticObject{
    public Building1(Vector3 position) {
        super(new ModelInstance(StaticBuffer.currentModels.get(0)), position);
    }
    public Building1(Vector3 position, float rotation, float scale) {
        super(new ModelInstance(StaticBuffer.currentModels.get(0)), position);
        modelInstance.transform.setToScaling(scale,scale,scale);
        modelInstance.transform.rotate(0f,1f,0f,rotation);
        setPosition(position);
    }
    @Override
    public int getRadius(){
        return 2;
    }
}
