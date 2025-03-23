package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;

public class CreativeMode implements Disposable {
    ImageButton[] imageButtons;
    String[][] assets;
    int currentx, currenty=0;
    public CreativeMode(){
        imageButtons=new ImageButton[]{};
        assets= GameEngine.getGameCore().getMap().getAssetNames();
    }
    public void draw(){
        for(int i=0; i<imageButtons.length; i++){
            imageButtons[i].draw(StaticBuffer.spriteBatch,0.5f);
        }
    }
    public void onTouch(int pointer, float touchx, float touchy){

    }
    public void onDrag(int pointer, float touchx, float touchy){

    }
    @Override
    public void dispose() {
        for(int i=0; i<imageButtons.length; i++){
            imageButtons[i].dispose();
        }
    }
}
