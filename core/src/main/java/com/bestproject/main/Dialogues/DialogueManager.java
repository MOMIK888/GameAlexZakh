package com.bestproject.main.Dialogues;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bestproject.main.StaticBuffer;

public class DialogueManager {
    Dialogue currentDialogue=null;
    TextPrinter textPrinter=new TextPrinter("",0,0,0.05f, StaticBuffer.screenWidth/3*2.5f,StaticBuffer.fonts[0],StaticBuffer.screenWidth,StaticBuffer.screenHeight);
    public void initializeDialogue(Dialogue dialogue) {
        currentDialogue = dialogue;
    }
    public void update(){
        if(currentDialogue!=null){
            if(currentDialogue.update()){
                currentDialogue=null;
            };
        }
    }
    public void draw(SpriteBatch spriteBatch){
        if(currentDialogue!=null){
            currentDialogue.render(spriteBatch,textPrinter);
        }
    }
    public void OnTouchListener(float touchx, float touchy, int pointer){
        if(touchy<StaticBuffer.screenHeight/3){
            
        }
    }
}
