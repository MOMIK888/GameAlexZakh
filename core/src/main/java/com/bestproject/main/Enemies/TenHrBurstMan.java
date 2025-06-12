package com.bestproject.main.Enemies;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class TenHrBurstMan extends Enemy{
    HITBOX[] hitboxes=new HITBOX[]{new HITBOX(0,0,0,0.2f,0.2f,1.3f)};
    HITBOX vulnerableHitbox=new HITBOX(0,0,0,0.38f,0.38f,0.38f);
    public TenHrBurstMan(Vector3 position) {
        super(new ModelInstance(StaticBuffer.assetManager.get("Models/10hrBurst/10hrburst.g3dj", Model.class)), position);
    }

}
