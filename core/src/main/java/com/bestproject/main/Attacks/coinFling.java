package com.bestproject.main.Attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class coinFling extends Attack{
    float speed=8f;
    Vector3 sincos;
    boolean swapped=false;
    boolean isCol=false;
    HITBOX hb;
    static CoinTxt coinTxt;
    float[] normals=new float[]{1,1,1};
    float[] ratatedir=new float[]{(float) (Math.random()-0.5f), (float) (Math.random()-0.5f), (float) (Math.random()-0.5f)};
    static {
        coinTxt=new CoinTxt();
    }
    public coinFling(Vector3 position, Vector3 flingdir) {
        super(StaticBuffer.getEffectfromBuffer(1), position);
        sincos=new Vector3(flingdir);
        StaticQuickMAth.normalizeSpeed(sincos);
        lengh=2f;
        hb=new HITBOX(position.x,position.z,position.y,0.1f,0.1f,0.1f);
        modelInstance.transform.scale(0.001f,0.001f,0.001f);
        movement=new Vector3();


    }

    @Override
    public boolean FLOOORHITBOXINTERRACTION(HITBOX[] hitboxes) {
        if(hitboxes!=null) {
            for (int i = 0; i < hitboxes.length; i++) {
                if(hitboxes[i].colliderect2d(this.hb)){
                    if(hitboxes[i].getBottomTopOverlap(hb)){
                        normals[1]*=-1;
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void HITBOXINTERRACTION(HITBOX[] hitboxes) {
            if(hitboxes!=null){
                for(int i=0; i<hitboxes.length; i++){
                    hitboxes[i].Bounce(hb,normals);

                }
            }
    }
    @Override
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            movement.y*=normals[1];
            sincos.y*=normals[1];
            movement_fragment.y*=normals[1];
            Vector3 previos_position=new Vector3(position);
            setPosition(new Vector3(normals[0]*movement.x+position.x,position.y+movement.y,normals[2]*movement.z+position.z));
            hb.setZ(position.y);
            hb.setY(position.z);
            hb.setX(position.x);
            GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);
            normals[1]=Math.abs(normals[1]);
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        movement.y*=normals[1];
        sincos.y*=normals[1];
        movement_fragment.y*=normals[1];
        Vector3 previos_position=new Vector3(position);
        setPosition(new Vector3(normals[0]*movement.x+position.x,position.y+movement.y,normals[2]*movement.z+position.z));
        hb.setZ(position.y);
        hb.setY(position.z);
        hb.setX(position.x);
        GameEngine.getGameCore().getMap().ForcedMovingRearr(previos_position);
        normals[1]=Math.abs(normals[1]);

    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        super.render(modelBatch, environment);
        createTxtDecal();
    }
    public Vector3 getAcceleration(){
        return new Vector3(speed*sincos.x/50f, speed*sincos.y/50f, speed*sincos.z/50f);
    }

    @Override
    public void update(){
        modelInstance.transform.rotate(ratatedir[0],ratatedir[1],ratatedir[2],1080f*StaticQuickMAth.move(GameCore.deltatime));
        lengh-=StaticQuickMAth.move(1f)*GameCore.deltatime;
        sincos.y-=StaticQuickMAth.getGravityAcceleration();
        fractureMovement(new Vector3(StaticQuickMAth.move(speed*sincos.x* GameCore.deltatime), StaticQuickMAth.move(speed*sincos.y* GameCore.deltatime), StaticQuickMAth.move(speed*sincos.z*GameCore.deltatime)));
        movement.set(0,0f,0);
    }
    @Override
    public boolean expire(){
        return swapped||(lengh<=0);
    }
    @Override
    public void setCostume(boolean val){
        swapped=true;
    }
    public void createTxtDecal(){
        coinTxt.setPosition(position);
        coinTxt.setRotation(GameCore.cameraRoationm, -GameCore.cameraRoationX);
        coinTxt.render(StaticBuffer.decalBatch);

    }

    @Override
    public void dispose() {
        super.dispose();
        coinTxt.dispose();
    }
}
class CoinTxt implements Disposable {
    private Decal decal;
    private Texture gradientTexture;
    public CoinTxt() {
        gradientTexture=new Texture("Images/Effect2d/star.png");
        decal = Decal.newDecal(new TextureRegion(gradientTexture), true);
        decal.setPosition(0, 0, 0);
        decal.setScale(0.001f);
        Color col=decal.getColor();
        col.a=0.2f;
        decal.setColor(col);
    }
    public void setScale(float scalex, float scaley){
        decal.setScale(scalex,scaley);
    }
    public void setRotation(float rotation, float rotation2){
        decal.setRotationY(0);
        decal.setRotationZ(0);
        decal.setRotationX(0);
        decal.rotateY(rotation);
        decal.rotateX(rotation2);
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
    }
}
