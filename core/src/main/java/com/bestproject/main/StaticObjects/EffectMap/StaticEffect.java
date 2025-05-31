package com.bestproject.main.StaticObjects.EffectMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;

public class StaticEffect extends StaticObject {
    Decal decal;
    public StaticEffect(Vector3 position, Texture texture, float resX, float resY, float a) {
        super(null,position);
        decal=Decal.newDecal(new TextureRegion(texture),true);
        decal.setPosition(position);
        decal.setDimensions(resX,resY);
        decal.setColor(decal.getColor().r,decal.getColor().g,decal.getColor().b,a);
    }
    @Override
    public HITBOX[] gethbs() {
        return null;
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        decal.lookAt(GameCore.camera.position,Vector3.Y);
        StaticBuffer.decalBatch.add(decal);
    }
    @Override
    public void render(ModelBatch modelBatch) {
    }
    @Override
    public void render(){
        StaticBuffer.decalBatch.add(decal);
    }
}
