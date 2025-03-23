package com.bestproject.main.Wall;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;

public class StoneWall extends StaticObject {
    HITBOX[] hitboxes;
    public StoneWall(Vector3 position, float rotation) {
        super(new ModelInstance(StaticBuffer.currentModels.get(0)), position);
        if (rotation%180==0) {
            hitboxes = new HITBOX[]{new HITBOX(position.x, position.z, position.y, 2.2f, 0.2f, 2f)};
        }else {
            hitboxes=new HITBOX[]{new HITBOX(position.x, position.z, position.y, 0.2f, 2.2f, 2f)};
        }
        modelInstance.transform.rotate(0f,1f,0f,rotation);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(this.hitboxes);
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        hitboxes[0].setX(position.x);
        hitboxes[0].setY(position.z);
        hitboxes[0].setZ(position.y);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        super.render(modelBatch, environment);
        modelInstance.transform.rotate(0f,1f,0f,180);
        modelBatch.render(modelInstance,environment);
    }

    @Override
    public HITBOX[] gethbs() {
        return hitboxes;
    }
}
