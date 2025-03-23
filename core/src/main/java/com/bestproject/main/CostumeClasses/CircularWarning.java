package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CreativeMode.TextureUtils;

public class CircularWarning implements Disposable {
    private Decal decal;
    private Texture gradientTexture;
    private Color color1 = new Color(1, 0, 0, 1);
    private Color color2 = new Color(0, 0, 1, 1);
    public CircularWarning(Color col1, Color co2) {
        color1=col1;
        color2=co2;
        gradientTexture= TextureUtils.createCirCleTexture(256,256, color1);
        decal = Decal.newDecal(new TextureRegion(gradientTexture), true);
        decal.setPosition(0, 0, 0);
        decal.setScale(0.001f);
    }
    public void setScale(float scalex, float scaley){
        decal.setScale(scalex,scaley);
    }
    public void setDimensions(float scalex, float scaley){
        decal.setDimensions(scalex,scaley);
    }
    public void setRotation(float rotation){
        decal.setRotationY(0);
        decal.setRotationZ(0);
        decal.setRotationX(90);
        decal.rotateZ(rotation);
    }
    public void render(DecalBatch decalBatch) {
        decalBatch.add(decal);
    }
    public void setPosition(Vector3 pos){
        decal.setPosition(pos);
    }

    @Override
    public void dispose() {
        gradientTexture.dispose();
    }
    public void setGradientColors(Color startColor, Color endColor) {
        this.color1.set(startColor);
        this.color2.set(endColor);
    }
}
