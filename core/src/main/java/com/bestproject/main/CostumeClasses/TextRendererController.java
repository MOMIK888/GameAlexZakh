package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TextRendererController implements Disposable {
    ArrayList<TextRenderer> textList=new ArrayList<>();
    public void render(SpriteBatch spriteBatch){
        for(int i=0; i<textList.size(); i++){
            textList.get(i).render(spriteBatch);
        }
    }
    public void render(ShapeRenderer sr){
        for(int i=0; i<textList.size(); i++){
            textList.get(i).renderPaddings(sr);
        }
    }
    public void renderNoshake(SpriteBatch spriteBatch, float  size){
        for(int i=0; i<textList.size(); i++){
            textList.get(i).renderNoShaking(spriteBatch, size);
        }
    }
    public void render2(ShapeRenderer sr){
        for(int i=0; i<textList.size(); i++){
            textList.get(i).renderPadding(sr);
        }
    }
    public boolean OnTouch(float touchx, float touchy){
        for(int i=0; i<textList.size(); i++){
            if(textList.get(i).OnTouch(touchx,touchy)){
                return true;
            }
        }
        return false;
    }
    public boolean checkPresence(int index){
        for(int i=0; i<textList.size(); i++){
            if(textList.get(i).getIndex()==index){
                return true;
            }
        }
        return false;
    }
    public void delete(int index){
        for(int i=0; i<textList.size(); i++){
            if(textList.get(i).getIndex()==index){
                textList.remove(i);
            }
        }
    }
    public void add(TextRenderer txt){
        textList.add(txt);
    }
    public void clear(){
        textList.clear();
    }
    @Override
    public void dispose(){
        for(int i=0; i<textList.size(); i++){
            textList.get(i).dispose();
        }
    }

}
