package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bestproject.main.StaticBuffer;

import java.awt.Rectangle;

public class CharacterWidget {
    public static Color color;
    public Texture characterSprite;
    public CharacterWidget(Texture texture){
        this.characterSprite=texture;
        color=new Color(StaticBuffer.choice_colors[0]);
        color.a=0.5f;
    }
    public void draw(ShapeRenderer shapeRenderer, int num, int totalMovesets){
        float w,x,y;
        w=100* StaticBuffer.getScaleX();
        x=StaticBuffer.screenWidth-200*StaticBuffer.getScaleX();
        y=StaticBuffer.screenHeight/2+StaticBuffer.getScaleY()*200+StaticBuffer.getScaleY()*100*num-StaticBuffer.getScaleY()*((float) (totalMovesets-1)/2)*100;
        shapeRenderer.setColor(color);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x,y,300*StaticBuffer.getScaleX(),w);
    }
    public void draw(SpriteBatch spriteBatch, int num){
        if(characterSprite!=null) {
            float w, x, y;
            w = 100 * StaticBuffer.getScaleX();
            x = StaticBuffer.screenWidth - 250 * StaticBuffer.getScaleX();
            y = StaticBuffer.screenHeight + StaticBuffer.getScaleY() * 100 * num;
            spriteBatch.draw(characterSprite, x, y, w, w);
        }
    }
    public boolean OnTouch(int num, int totalMovesets, int pointer, float touchX, float touchY){
        float w,x,y,h;
        h=300*StaticBuffer.getScaleX();
        w=100* StaticBuffer.getScaleX();
        x=StaticBuffer.screenWidth-200*StaticBuffer.getScaleX();
        y=StaticBuffer.screenHeight/2+StaticBuffer.getScaleY()*200+StaticBuffer.getScaleY()*100*num-StaticBuffer.getScaleY()*((float) (totalMovesets-1)/2)*100;
        if(x<touchX && touchX<x+h && y<touchY && touchY<y+w){
            return true;
        }
        return false;
    }
}
