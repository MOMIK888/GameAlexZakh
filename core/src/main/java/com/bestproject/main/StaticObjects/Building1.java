package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.StaticBuffer;

public class Building1 extends StaticObject{
    public Building1(Vector3 position) {
        super(new ModelInstance(StaticBuffer.currentModels.get(0)), position);
    }
    @Override
    public int getRadius(){
        return 2;
    }
}
