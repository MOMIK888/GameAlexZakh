package com.bestproject.main.MapUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;

public class Sun {
    Vector3 sunPosition=new Vector3();
    float emissionStrengh;
    float time;
    float[] milestones;
    private static Texture suntxt=null;
    public Sun(float currentTime){
        if(suntxt==null){
            suntxt=new Texture(Gdx.files.internal("Models/Skyboxes/sun.png"));
        }
        time=currentTime;
    }
    public void render(DecalBatch decalBatch){
        Decal decal=Decal.newDecal(new TextureRegion(suntxt),true);
        decal.setDimensions(30,30);
        decal.setPosition(new Vector3(sunPosition.x*100,sunPosition.y*100,sunPosition.z*100));
        decal.lookAt(GameCore.camera.position,Vector3.Y);
        decalBatch.add(decal);
    }
    public void update(float currenttime){
        time=currenttime;
        updateSun();
    }
    private void updateSun(){
        sunPosition=new Vector3(0,0,1);
        sunPosition.rotateRad(time/3.81971f,1f,0f,0f);

    }
    public Vector3 getLightDir(){
        return new Vector3(-sunPosition.x,-sunPosition.y,-sunPosition.z);
    }
    public float getEmissionStrengh(){
        return emissionStrengh;
    }
}
