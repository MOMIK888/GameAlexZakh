package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

public class GradientDecalWarning implements Disposable {
    private Decal decal;
    private Texture gradientTexture;
    private Color color1 = new Color(1, 0, 0, 1);
    private Color color2 = new Color(0, 0, 1, 1);
    public GradientDecalWarning(Color col1, Color co2) {
        color1=col1;
        color2=co2;
        updateGradientTexture();
        decal = Decal.newDecal(new TextureRegion(gradientTexture), true);
        decal.setPosition(0, 0, 0);
        decal.setScale(0.001f);
    }
    public void setScale(float scalex, float scaley){
        decal.setScale(scalex,scaley);
    }
    public void setRotation(float rotation){
        decal.setRotationY(0);
        decal.setRotationZ(0);
        decal.setRotationX(90);
        decal.rotateZ(rotation);
    }
    private void updateGradientTexture() {
        int width = 256;
        int height = 256;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int y = 0; y < height; y++) {
            float t = (float) y / (height - 1);
            Color color = new Color(
                color1.r + t * (color2.r - color1.r),
                color1.g + t * (color2.g - color1.g),
                color1.b + t * (color2.b - color1.b),
                color1.a + t * (color2.a - color1.a)
            );
            pixmap.setColor(color);
            pixmap.drawLine(0, y, width - 1, y);
        }
        if (gradientTexture != null) {
            gradientTexture.dispose();
        }
        gradientTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    public void render(DecalBatch decalBatch) {
        decal.setTextureRegion(new TextureRegion(gradientTexture));
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
