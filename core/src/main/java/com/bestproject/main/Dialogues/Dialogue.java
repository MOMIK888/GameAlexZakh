package com.bestproject.main.Dialogues;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

public class Dialogue {
    String[] lines;
    protected boolean isfinished=false;
    protected boolean[] choices;
    protected int index=0;
    public boolean update(){
        return false;
    }
    public void render(SpriteBatch spriteBatch, TextPrinter textPrinter){

    }
    protected void afterInit(){
        choices=new boolean[lines.length];
    }
    public boolean isfinished(){
        return isfinished;
    }
    public void reset(){
        isfinished=false;
        index=0;
    }
    public void OnClick(boolean isClicked, TextPrinter textPrinter){
        if(isClicked){
            if(textPrinter.isfinished() && !choices[index]) {
                index += 1;
                if (index < lines.length) {
                    textPrinter.reset(lines[index]+" ");
                } else {
                    isfinished = true;
                    index=index-1;
                }
            }else if(textPrinter.isfinished()){

            }else{
                textPrinter.skip();
            }
        }

    }
    public void Onagree(){

    }
    public void Ondisagree(){

    }

}
