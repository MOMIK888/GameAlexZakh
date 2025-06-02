package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bestproject.main.CostumeClasses.ScreenSpaceSim;
import com.bestproject.main.Dialogues.TextPrinter;
import com.bestproject.main.Dialogues.VoiceLine;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ScreenEmulator;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;


public class DialogueScreen implements ScreenSpaceSim, ScreenEmulator {
    private static TextPrinter voiceLine=null;
    private String[] textBlocks=new String[0];
    static com.badlogic.gdx.math.Rectangle rect=new com.badlogic.gdx.math.Rectangle(0, (int)StaticBuffer.screenHeight-StaticBuffer.screenHeight/3*2,(int)StaticBuffer.screenWidth,(int)-StaticBuffer.screenHeight);
    private int currentBlockIndex = 0;

    public DialogueScreen(String[] textBlocks) {
        this.textBlocks = textBlocks;
        if(voiceLine==null) {
            this.voiceLine = new TextPrinter(textBlocks[0]+" ",rect.x+ 60,(int)((rect.getY()- StaticBuffer.getScaleX()*60)),0.1f,(int)rect.width-120,StaticBuffer.fonts[0],StaticBuffer.screenWidth,StaticBuffer.screenHeight );
        }
        voiceLine.reset(textBlocks[0]);
    }

    public void afterInit() {
        GameEngine.getGameCore().scm.add(this);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(StaticBuffer.choice_colors[0]);
        shapeRenderer.rect(rect.x,rect.y,rect.width,rect.height);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        voiceLine.draw(spriteBatch);
    }

    @Override
    public void update(float delta) {
        StaticQuickMAth.setTimeFlow(0,0.1f,true);
        voiceLine.update(delta);
        if(GameEngine.getGameCore().scm.size<1){
            GameEngine.getGameCore().scm.add(this);
        }

    }

    @Override
    public boolean expire() {
        return voiceLine.isFinished() && currentBlockIndex >= textBlocks.length;
    }

    @Override
    public void OnTouch(float screenX, float screenY, int pointer) {
        if (!voiceLine.isFinished()) {
            voiceLine.skip();
        } else {
            currentBlockIndex++;
            if (currentBlockIndex < textBlocks.length) {
                voiceLine.reset(textBlocks[currentBlockIndex]+" ");
            }
        }
    }

    @Override
    public void OnScroll(float screenX, float screenY, int pointer) {
        // No scroll interaction
    }

    @Override
    public void OnUp(float screenX, float screenY, int pointer) {
        // No release interaction
    }
}
class Rectangle{

}
