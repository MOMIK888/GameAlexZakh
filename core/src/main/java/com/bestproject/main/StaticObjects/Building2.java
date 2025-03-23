package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.StaticBuffer;

public class Building2 extends StaticObject{
    public Building2(Vector3 position) {
        super(new ModelInstance(StaticBuffer.currentModels.get(2)), position);
    }
    @Override
    public int getRadius(){
        return 2;
    }
}
