package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class Crate extends StaticObject implements Disposable {
    HITBOX[] hitboxes;

    public Crate(Vector3 position) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(2))), position);
        modelInstance.transform.scale(0.002f,0.002f,0.002f);
    }
    public Crate(Vector3 position, float rotation) {
        super(new ModelInstance(new ModelInstance(StaticBuffer.currentModels.get(2))), position);
        modelInstance.transform.scale(0.002f,0.002f,0.002f);
        modelInstance.transform.rotate(0f,1f,0f,rotation);
    }

    @Override
    public void update() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
