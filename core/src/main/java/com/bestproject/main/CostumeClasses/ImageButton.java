package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.StaticBuffer;

public class ImageButton implements Disposable {
    public Texture image;
    public Rectangle bounds;
    public boolean isPressed;

    private int pointer=-1;
    public ImageButton(String imagePath, float x, float y, float width, float height) {
        this.image = new Texture(Gdx.files.internal(imagePath));
        this.bounds = StaticBuffer.getBounds(1f,true, width, height,(int)x,(int) y);
        this.isPressed = false;
    }
    public ImageButton(String imagePath, float x, float y, float width, float height, boolean flag) {
        this.image = new Texture(Gdx.files.internal(imagePath));
        this.bounds = StaticBuffer.getBounds(1f,true, image.getWidth(), image.getHeight(),(int)x,(int) y);
        this.isPressed = false;
    }
    public void draw(SpriteBatch batch, float alpha) {
        batch.setColor(1, 1, 1,alpha);
        batch.draw(image, bounds.x, bounds.y, bounds.width, bounds.height);
    }
    public float getRadius(){
        return bounds.width/2;
    }
    public float getCenterX(){
        return bounds.x+bounds.width/2;
    }
    public float getCenterY(){
        return bounds.y+bounds.height/2;
    }
    public void drawButton(ShapeRenderer shapeRenderer, float x, float y, float width, float height, float radius, Color color1, Color color2, float charge) {
        charge = Math.min(100, Math.max(0, charge));
        float t = charge / 100f;
        Color startColor = new Color(color2).lerp(color1, t);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color2);
        shapeRenderer.circle(x + width - radius, y + height - radius, radius);
        shapeRenderer.setColor(startColor);
        shapeRenderer.arc(x + width - radius, y + height - radius, radius,270-charge*1.8f,charge*3.6f);
    }
    public void update() {
        isPressed = false;
    }
    public boolean isPressed() {
        return isPressed;
    }
    public void release(){
        pointer=-1;
    }
    public boolean onTouch(float touchX, float touchY, int pointer){
        if (bounds.contains(touchX, touchY)) {
            isPressed = true;
            return true;
        }
        return false;
    }
    public void setBounds(float x,float y){
        bounds.x=x;
        bounds.y=y;
    }
    @Override
    public void dispose() {
        image.dispose();
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }
}
