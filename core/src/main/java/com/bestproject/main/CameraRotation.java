package com.bestproject.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bestproject.main.Game.GameCore;

import java.awt.Point;

public class CameraRotation {
    int current_pointer=-1;
    private Vector2 touchStart = new Vector2();
    private Vector2 touchEnd = new Vector2();
    private float SCHEIGHT, SCWIDTH;
    private float rotationSpeed = 360f;
    private float sensitivity=1f;
    private float touchLen=0;
    public CameraRotation(){
        SCHEIGHT=Gdx.graphics.getHeight();
        SCWIDTH=Gdx.graphics.getWidth();
    }
    public void setCurrent_pointer(int index){
        current_pointer=index;
    }
    public int getCurrent_pointer(){
        return current_pointer;
    }
    public void startTouch(int touchx, int touchy){
        touchStart.set(touchx, touchy);
        touchEnd.set(touchx, touchy);
        touchLen=0;
    }
    public void drag(int screenX, int screenY){
        touchEnd.set(screenX, screenY);
    }
    public void update(){
        touchLen+= GameCore.deltatime;
    }
    public void up(){
        current_pointer=-1;
        if(touchLen<0.05f){
            GameCore.lastclick.set(touchEnd.x,touchEnd.y);
        }
    }
    public float[] getVectors(){
        float deltaX = touchEnd.x - touchStart.x;
        float deltaY = touchEnd.y - touchStart.y;
        touchStart.set(touchEnd);
        return new float[]{deltaX,deltaY};
    }
    public float[] getAngles(float[] values){
        return new float[]{values[0]/SCWIDTH*rotationSpeed*sensitivity,values[1]/SCHEIGHT*rotationSpeed*sensitivity};
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
}
