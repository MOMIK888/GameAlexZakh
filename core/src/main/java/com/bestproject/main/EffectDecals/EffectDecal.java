package com.bestproject.main.EffectDecals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.bestproject.main.CostumeClasses.SpriteSheet;

public class EffectDecal extends Effect{
    Decal decal;
    SpriteSheet spriteSheet;
    public EffectDecal(float length, SpriteSheet spriteSheet) {
        super(length);
        decal=Decal.newDecal(spriteSheet.getFrame(0),true);
        this.spriteSheet=spriteSheet;
    }
    @Override
    public void update(float delta){
        super.update(delta);

    }

    @Override
    public void render(DecalBatch decalBatch) {

    }

    @Override
    protected void expire() {

    }
}
