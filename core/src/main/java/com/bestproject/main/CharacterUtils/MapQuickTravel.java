package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ScreenEmulator;
import com.bestproject.main.StaticBuffer;

import org.w3c.dom.Text;

public class MapQuickTravel implements ScreenEmulator {
    Array<Vector2> points;
    public Texture mapTexture;
    public Texture background;
    public Texture pointerTexture;
    ImageButton[] buttons;
    Array<MapNode> mapNodes;
    float mapx, mapy;
    float textureX, textureY;
    public MapQuickTravel(){
        mapy=0;
        mapTexture=new Texture(Gdx.files.internal("Images/Ui/map.png"));
        background=new Texture(Gdx.files.internal("Images/Ui/map.png"));
        textureX=mapTexture.getWidth();
        textureY=mapTexture.getHeight();
        points=new Array<>();
        points.add(new Vector2(1500,300));
        points.add(new Vector2(500,300));
        pointerTexture=new Texture(Gdx.files.internal("Images/Ui/testTexture.png"));
        buttons=new ImageButton[]{new ImageButton("Images/Ui/testTexture.png",100,100,200,200)};
    }
    @Override
    public void OnTouch(float screenX, float screenY, int pointer){
        if(buttons[0].onTouch(screenX,screenY,pointer)){
            buttons[0].setPointer(pointer);
            GameEngine.getGameCore().displayQuickTravel=false;
            GameEngine.gameCore.DecalButtonTiring=1;
        }
        for (int i=0; i<points.size; i++){
            if(points.get(i).x<screenX && screenX<points.get(i).x+100){
                if(points.get(i).y<screenY && screenY<points.get(i).y+100){
                    GameEngine.getGameCore().displayQuickTravel=false;
                    GameEngine.gameCore.DecalButtonTiring=1;
                    if(i==0){
                        GameCore.TemporaryMapBuffer=2;
                    } else{
                        GameCore.TemporaryMapBuffer=1;
                    }
                }
            }
        }
    }
    @Override
    public void OnScroll(float screenX, float screenY, int pointer){

    }
    @Override
    public void OnUp(float screenX, float screenY, int pointer){
        if(buttons[0].onTouch(screenX,screenY,pointer)){
            buttons[0].release();
            GameEngine.getGameCore().displayQuickTravel=false;
        }
    }
    public void draw(SpriteBatch spriteBatch){
        GameEngine.getGameCore().scm.add(this);
        spriteBatch.draw(background,0, 0,StaticBuffer.screenWidth,StaticBuffer.screenHeight);
        spriteBatch.draw(mapTexture,mapx, mapy,StaticBuffer.screenWidth,StaticBuffer.screenHeight);
        for(int i=0; i<points.size; i++){
            spriteBatch.draw(pointerTexture,points.get(i).x,points.get(i).y);
        }
        for(int i=0; i<buttons.length; i++){
            buttons[i].draw(spriteBatch,0.6f);
        }

    }
    public void update(float delta){

    }
}
class MapNode{
    String description;
    int index;
    String name;
    boolean isOpened;

}
