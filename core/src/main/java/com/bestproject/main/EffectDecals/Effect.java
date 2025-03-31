package com.bestproject.main.EffectDecals;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

public abstract class Effect {
    protected float length;

    public Effect(float length) {
        this.length = length;
    }

    public void update(float delta) {
        length -= delta;
        if (length <= 0) {
            expire();
        }
    }

    public boolean isExpired() {
        return length <= 0;
    }
    public void render(DecalBatch decalBatch){

    }

    protected abstract void expire();
}

