package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
    public void render(Array<HITBOX> hbs){
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
        hbRenderer.setPosition(0, new Vector3(x, y, z + thickness / 2));
        hbRenderer.setDimensions(0, width, height);
        hbRenderer.setRotation(0,0, 0);
        hbRenderer.render(0, decalBatch);
        // Back side
        hbRenderer.setPosition(1,new Vector3(x, y, z - thickness / 2));
        hbRenderer.setDimensions(1,width, height);
        hbRenderer.setRotation(1,0, 180);
        hbRenderer.render(1,decalBatch);
        // Left side
        hbRenderer.setPosition(2,new Vector3(x - width / 2, y, z));
        hbRenderer.setDimensions(2,thickness, height);
        hbRenderer.setRotation(2,0, 90);
        hbRenderer.setRotationY(2,90);
        hbRenderer.render(2,decalBatch);
        // Right side
        hbRenderer.setPosition(3,new Vector3(x + width / 2, y, z));
        hbRenderer.setDimensions(3,thickness, height);
        hbRenderer.setRotation(3,0, -90);
        hbRenderer.setRotationY(3,90);
        hbRenderer.render(3,decalBatch);

        // Top side
        hbRenderer.setPosition(4,new Vector3(x, y + height / 2, z));
        hbRenderer.setDimensions(4,width, thickness);
        hbRenderer.setRotation(4,90, 0);
        hbRenderer.setRotationX(4,90);
        hbRenderer.render(4,decalBatch);
        // Bottom side
        hbRenderer.setPosition(5,new Vector3(x, y - height / 2, z));
        hbRenderer.setDimensions(5,width, thickness);
        hbRenderer.setRotation(5,-90, 0);
        hbRenderer.setRotationX(5,90);
        hbRenderer.render(5, decalBatch);
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
        hbRenderer.setPosition(0, new Vector3(x, y, z + thickness / 2));
        hbRenderer.setDimensions(0, width, height);
        hbRenderer.setRotation(0,0, 0);
        hbRenderer.render(0, decalBatch);
        // Back side
        hbRenderer.setPosition(1,new Vector3(x, y, z - thickness / 2));
        hbRenderer.setDimensions(1,width, height);
        hbRenderer.setRotation(1,0, 180);
        hbRenderer.render(1,decalBatch);
        // Left side
        hbRenderer.setPosition(2,new Vector3(x - width / 2, y, z));
        hbRenderer.setDimensions(2,thickness, height);
        hbRenderer.setRotation(2,0, 90);
        hbRenderer.setRotationY(2,90);
        hbRenderer.render(2,decalBatch);
        // Right side
        hbRenderer.setPosition(3,new Vector3(x + width / 2, y, z));
        hbRenderer.setDimensions(3,thickness, height);
        hbRenderer.setRotation(3,0, -90);
        hbRenderer.setRotationY(3,90);
        hbRenderer.render(3,decalBatch);

        // Top side
        hbRenderer.setPosition(4,new Vector3(x, y + height / 2, z));
        hbRenderer.setDimensions(4,width, thickness);
        hbRenderer.setRotation(4,90, 0);
        hbRenderer.setRotationX(4,90);
        hbRenderer.render(4,decalBatch);
        // Bottom side
        hbRenderer.setPosition(5,new Vector3(x, y - height / 2, z));
        hbRenderer.setDimensions(5,width, thickness);
        hbRenderer.setRotation(5,-90, 0);
        hbRenderer.setRotationX(5,90);
        hbRenderer.render(5, decalBatch);
        decalBatch.flush();

    }

    public void dispose() {

    }
}
class HBRENDERER implements Disposable {
    private Decal[] decal=new Decal[6];
    private Texture gradientTexture;
    public HBRENDERER() {
        setGradientTexture(TextureUtils.createRedFrameTexture(64, 64));
        for(int i=0; i<6; i++) {
            decal[i] = Decal.newDecal(new TextureRegion(getGradientTexture()), true);
            decal[i].setPosition(0, 0, 0);
            decal[i].setScale(1f);
            Color col=decal[i].getColor();
            col.a=0.2f;
            decal[i].setColor(col);
        }
    }
    public void setTint(int r, int g, int b){
        for(int i=0; i<6; i++) {
            decal[i].getColor().set(r,g,b,0.2f);
        }
    }
    public void setDimensions(int index, float scalex, float scaley){
        decal[index].setDimensions(scalex,scaley);
    }
    public void setRotation(int index,float rotation, float rotation2){
        decal[index].setRotationY(0);
        decal[index].setRotationZ(0);
        decal[index].setRotationX(0);
        decal[index].rotateY(rotation);
        decal[index].rotateX(rotation2);
    }
    public void setRotationX(int index, float Rotation){
        decal[index].setRotationX(0);
        decal[index].rotateX(Rotation);
    }
    public void setRotationY(int index, float Rotation){
        decal[index].setRotationY(0);
        decal[index].rotateY(Rotation);
    }
    public void setRotationZ(int index,float Rotation){
        decal[index].setRotationZ(0);
        decal[index].rotateZ(Rotation);
    }
    public void render(int index, DecalBatch decalBatch) {
        decal[index].setTextureRegion(new TextureRegion(getGradientTexture()));
        decalBatch.add(decal[index]);
    }
    public void setPosition(int index, Vector3 pos){
        decal[index].setPosition(pos);
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

