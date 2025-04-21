package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bestproject.main.StaticBuffer;

public class CharacterWidget {
    public static Color[] colors;
    public Texture characterSprite;
    public CharacterWidget(Texture texture){
        this.characterSprite=texture;
    }
    public void draw(ShapeRenderer shapeRenderer, int num, int colornum ){
        float w,x,y;
        w=100* StaticBuffer.getScaleX();
        x=StaticBuffer.screenWidth-200*StaticBuffer.getScaleX();
        y=StaticBuffer.screenHeight+StaticBuffer.getScaleY()*100*num;
        shapeRenderer.setColor(colors[colornum]);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x,y,300*StaticBuffer.getScaleX(),w);
    }
    public void draw(SpriteBatch spriteBatch, int num){
        float w,x,y;
        w=100* StaticBuffer.getScaleX();
        x=StaticBuffer.screenWidth-250*StaticBuffer.getScaleX();
        y=StaticBuffer.screenHeight+StaticBuffer.getScaleY()*100*num;
        spriteBatch.draw(characterSprite,x,y,w,w);
    }
}
