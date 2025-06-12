package com.bestproject.main.UiParts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.CostumeClasses.ScreenSpaceSim;
import com.bestproject.main.CostumeClasses.SpriteSheet;
import com.bestproject.main.ScreenEmulator;
import com.bestproject.main.StaticBuffer;

public class Lore implements ScreenEmulator, ScreenSpaceSim {
    SpriteSheet spriteSheet;
    int currentPage;
    ImageButton imageButton;
    boolean disposeItem=false;
    public Lore(){

    }
    @Override
    public void render(ShapeRenderer shapeRenderer) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        int width=spriteSheet.getFrame(currentPage).getRegionWidth();
        int height=spriteSheet.getFrame(currentPage).getRegionWidth();
        spriteBatch.draw(spriteSheet.getFrame(currentPage),0,StaticBuffer.screenHeight,width*StaticBuffer.getScaleX(),height*StaticBuffer.getScaleX());
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean expire() {
        return disposeItem;
    }

    @Override
    public void OnTouch(float screenX, float screenY, int pointer) {
        if(imageButton.onTouch(screenX,screenY,pointer)){
            disposeItem=true;
        } else if(screenX> StaticBuffer.screenWidth/2){
            currentPage= MathUtils.clamp(currentPage+1, 0,spriteSheet.getNumFrames()-1);

        } else{
            currentPage=MathUtils.clamp(currentPage-1, 0,spriteSheet.getNumFrames()-1);
        }
    }

    @Override
    public void OnScroll(float screenX, float screenY, int pointer) {

    }

    @Override
    public void OnUp(float screenX, float screenY, int pointer) {

    }
}
