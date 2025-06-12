package com.bestproject.main.UiParts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;


public class Settins implements Disposable {
    protected int width, height;
    Color[] colors;
    protected float frames, maxframes;
    protected Rectangle menurect;
    QuestingUi questingUi;
    private ImageButton[] Buttons;
    private Texture[] sprites;
    boolean isQuesting=false;
    public Settins(){
        height=Gdx.graphics.getHeight();
        width= Gdx.graphics.getWidth() /12;
        Buttons=new ImageButton[]{new ImageButton("Images/Ui/returnbutton.png",1600,1060-120,120,120),
            new ImageButton("Images/Ui/quests.png",1600,920-120,120,120),
            new ImageButton("Images/Ui/inventory.png",1600,780-120,120,120),
            new ImageButton("Images/Ui/leave.png",1600,640-120,120,120),
            new ImageButton("Images/Ui/leave.png",2000,400,120,120)};
        sprites=new Texture[]{new Texture("Images/Ui/coin.png")};
        colors=new Color[]{StaticBuffer.choice_colors[0],StaticBuffer.choice_colors[1]};
        frames=0;
        questingUi=new QuestingUi(StaticBuffer.fonts[0],StaticBuffer.questManager);
        maxframes=0.2f;
        menurect=new Rectangle(-(width),Gdx.graphics.getHeight(),width,height);

    }
    public void draw_settings(ShapeRenderer shapeRenderer){
        if(frames<maxframes){
            frames+= GameEngine.getGameCore().deltatime;
            if(frames>=maxframes){
                frames=maxframes;
            }
        }
        int x = (int) (width-(width*(1f/maxframes)*frames));
        for(int i=0; i<Buttons.length; i++){
            Buttons[i].setBounds(x+20f*StaticBuffer.getScaleX(),Buttons[i].bounds.y);
        }
        menurect.set(-x, 0, width, height);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(colors[0]);
        shapeRenderer.rect(menurect.x,menurect.y,menurect.width,menurect.height);
        for(int i=0; i<Buttons.length; i++){
            Buttons[i].drawButton(shapeRenderer,Buttons[i].bounds.x,Buttons[i].bounds.y,Buttons[i].getRadius()*2,Buttons[i].getRadius()*2,Buttons[i].getRadius(),colors[1],colors[0], 0);
        }
    }
    public void draw_settings(SpriteBatch spriteBatch){
        for(int i=0; i<Buttons.length; i++){
            Buttons[i].draw(spriteBatch, 0.6f);
        }
        if(isQuesting){
            StaticBuffer.TestShapeRenderer.begin();
            questingUi.render(spriteBatch,StaticBuffer.TestShapeRenderer,StaticBuffer.questManager);
            StaticBuffer.TestShapeRenderer.end();
        }
    }
    public void reset(){
        frames=0;
        StaticBuffer.setIsPaused(false);
    }
    public boolean settingdsisPressed(float touchx, float touchy, int pointer){
        for(int i=0; i<Buttons.length; i++){
            if(Buttons[i].onTouch(touchx,touchy, pointer)){
                if((i)==0){
                    reset();
                } else if(i==1) {
                    isQuesting=true;
                }else if((i==3)){
                        StaticBuffer.saveAllValues();
                        System.exit(0);
                } else if(i==4){
                    StaticBuffer.EnableCREATIVE();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        for(int i=0; i<Buttons.length; i++){
            Buttons[i].dispose();
        }
        for (int i=0; i<sprites.length; i++) {
            sprites[i].dispose();
        }

    }
}
