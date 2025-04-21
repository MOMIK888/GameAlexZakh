package com.bestproject.main.MapUtils;

import com.badlogic.gdx.math.Vector3;

public class Sun {
    Vector3 sunPosition=new Vector3();
    float emissionStrengh;
    float time;
    float[] milestones;
    public Sun(float currentTime){
        time=currentTime;
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
