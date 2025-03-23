package com.bestproject.main;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bestproject.main.Game.GameCore;

public class Joystick {
    private Vector2 position;
    private Vector2 touchPosition;
    private float radius;
    private boolean isTouched;
    private  int index=-1;
    private Color[] colors;
    public Joystick(float x, float y, float radius) {
        this.position = new Vector2(x, y);
        this.touchPosition = new Vector2(x, y);
        this.radius = radius;
        this.isTouched = false;
        colors=new Color[]{Color.DARK_GRAY,Color.GRAY};
        colors[0].a=0.8f;
        colors[1].a=0.8f;
    }
    public boolean contains(float val1, float val2){
        if(StaticQuickMAth.distance(new float[]{val1,val2},new float[]{position.x,position.y})<radius){
            return true;
        }
        return false;
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(colors[1]);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.setColor(colors[0]);
        shapeRenderer.circle(touchPosition.x, touchPosition.y, radius / 3);
    }

    public void update(float touchX, float touchY) {
        if (isTouched) {
            Vector2 delta = new Vector2(touchX - position.x, touchY - position.y);
            if (delta.len() > radius) {
                delta.setLength(radius);
            }
            touchPosition.set(position.x + delta.x, position.y + delta.y);
        }
    }
    public int getIndex(){
        return index;
    }
    public void setIndex(int index){
        this.index=index;
    }
    public void resetIndex(){
        index=-1;
    }

    public void setTouched(boolean isTouched) {
        this.isTouched = isTouched;
        if (!isTouched) {
            touchPosition.set(position);
        }
    }

    public Vector2 getDirection() {
        Vector2 direction = new Vector2(touchPosition.x - position.x, touchPosition.y - position.y);
        direction.nor();
        float deg= GameCore.cameraRoationm;
        float radians = (float) Math.toRadians(deg);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float newX = direction.x * cos - direction.y * sin;
        float newY = direction.x * sin + direction.y * cos;
        direction.set(newX, newY);
        return direction;
    }
    public Vector2 getDIrectionCreative(){
        Vector2 direction = new Vector2(touchPosition.x - position.x, touchPosition.y - position.y);
        direction.nor();
        return direction;
    }
    public boolean isTouched() {
        return isTouched;
    }
}
