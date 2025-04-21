package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class ShowHitbox {
    public HBRENDERER hbRenderer=new HBRENDERER();

    public ShowHitbox() {
    }
    public void render(HITBOX[] hbs){
        if(hbs!=null){
            for(HITBOX i : hbs)
                cDec(StaticBuffer.decalBatch,i);
        }
    }
    public void renderWithRotation(HITBOX[] hbs){
        if(hbs!=null){
            for(HITBOX i: hbs){
                cDecWRotation(StaticBuffer.decalBatch,i);
            }
        }
    }
    public void cDecWRotation(DecalBatch decalBatch, HITBOX hitbox){
        float x = (float) hitbox.x;
        float y = (float) hitbox.z;
        float z = (float) hitbox.y;
        float width = (float) hitbox.width;
        float height = (float) hitbox.height;
        float thickness = (float) hitbox.thickness;

        // Front side
        hbRenderer.setPosition(new Vector3(x, y, z + thickness / 2));
        hbRenderer.setDimensions(width, height);
        hbRenderer.setRotation(0, 0);
        hbRenderer.setRotationY((float) hitbox.rotation);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Back side
        hbRenderer.setPosition(new Vector3(x, y, z - thickness / 2));
        hbRenderer.setDimensions(width, height);
        hbRenderer.setRotation(0, 180);
        hbRenderer.setRotationY((float) hitbox.rotation);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Left side
        hbRenderer.setPosition(new Vector3(x - width / 2, y, z));
        hbRenderer.setDimensions(thickness, height);
        hbRenderer.setRotation(0, 90);
        hbRenderer.setRotationY(90);
        hbRenderer.setRotationY((float) hitbox.rotation);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Right side
        hbRenderer.setPosition(new Vector3(x + width / 2, y, z));
        hbRenderer.setDimensions(thickness, height);
        hbRenderer.setRotation(0, -90);
        hbRenderer.setRotationY(90);
        hbRenderer.setRotationY((float) hitbox.rotation);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

    }
    public void cDec(DecalBatch decalBatch, HITBOX hitbox) {
        float x = (float) hitbox.x;
        float y = (float) hitbox.z;
        float z = (float) hitbox.y;
        float width = (float) hitbox.width;
        float height = (float) hitbox.height;
        float thickness = (float) hitbox.thickness;

        // Front side
        hbRenderer.setPosition(new Vector3(x, y, z + thickness / 2));
        hbRenderer.setDimensions(width, height);
        hbRenderer.setRotation(0, 0);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Back side
        hbRenderer.setPosition(new Vector3(x, y, z - thickness / 2));
        hbRenderer.setDimensions(width, height);
        hbRenderer.setRotation(0, 180);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Left side
        hbRenderer.setPosition(new Vector3(x - width / 2, y, z));
        hbRenderer.setDimensions(thickness, height);
        hbRenderer.setRotation(0, 90);
        hbRenderer.setRotationY(90);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Right side
        hbRenderer.setPosition(new Vector3(x + width / 2, y, z));
        hbRenderer.setDimensions(thickness, height);
        hbRenderer.setRotation(0, -90);
        hbRenderer.setRotationY(90);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Top side
        hbRenderer.setPosition(new Vector3(x, y + height / 2, z));
        hbRenderer.setDimensions(width, thickness);
        hbRenderer.setRotation(90, 0);
        hbRenderer.setRotationX(90);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

        // Bottom side
        hbRenderer.setPosition(new Vector3(x, y - height / 2, z));
        hbRenderer.setDimensions(width, thickness);
        hbRenderer.setRotation(-90, 0);
        hbRenderer.setRotationX(90);
        hbRenderer.render(decalBatch);
        decalBatch.flush();

    }

    public void dispose() {

    }
}
class HBRENDERER implements Disposable {
    private Decal decal;
    private Texture gradientTexture;
    public HBRENDERER() {
        setGradientTexture(TextureUtils.createRedFrameTexture(64, 64));
        decal = Decal.newDecal(new TextureRegion(getGradientTexture()), true);
        decal.setPosition(0, 0, 0);
        decal.setScale(1f);
        Color col=decal.getColor();
        col.a=0.2f;
        decal.setColor(col);
    }
    public void setTint(int r, int g, int b){
        decal.getColor().set(r,g,b,0.2f);
    }
    public void setDimensions(float scalex, float scaley){
        decal.setDimensions(scalex,scaley);
    }
    public void setRotation(float rotation, float rotation2){
        decal.setRotationY(0);
        decal.setRotationZ(0);
        decal.setRotationX(0);
        decal.rotateY(rotation);
        decal.rotateX(rotation2);
    }
    public void setRotationX(float Rotation){
        decal.setRotationX(0);
        decal.rotateX(Rotation);
    }
    public void setRotationY(float Rotation){
        decal.setRotationY(0);
        decal.rotateY(Rotation);
    }
    public void setRotationZ(float Rotation){
        decal.setRotationZ(0);
        decal.rotateZ(Rotation);
    }
    public void render(DecalBatch decalBatch) {
        decal.setTextureRegion(new TextureRegion(getGradientTexture()));
        decalBatch.add(decal);
    }
    public void setPosition(Vector3 pos){
        decal.setPosition(pos);
    }

    @Override
    public void dispose() {
        getGradientTexture().dispose();
    }
    public void setGradientColors(Color startColor, Color endColor) {
    }

    public Texture getGradientTexture() {
        return gradientTexture;
    }

    public void setGradientTexture(Texture gradientTexture) {
        this.gradientTexture = gradientTexture;
    }
}

