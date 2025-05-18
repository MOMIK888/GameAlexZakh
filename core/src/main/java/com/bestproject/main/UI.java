package com.bestproject.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.FPS;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Moveset.Moveset;
import com.bestproject.main.Moveset.PizzaGuy;
import com.bestproject.main.Moveset.Tp_quickMoveset;
import com.bestproject.main.UiParts.Settins;

import java.util.ArrayList;

public class UI implements Disposable {
    //disposed
    public ArrayList<Moveset> movesets; //disposed
    protected int current_moveset=0;
    protected boolean isLocked=false;
    protected ImageButton settingsButton; //disposed
    Settins settins; //disposed
    public UI(){
        settins=new Settins();
        movesets=new ArrayList<>();
        movesets.add(new PizzaGuy());
        settingsButton=new ImageButton("Images/ButtonIcons/setting_button.png",0,970,120,120);

    }
    public void AnalyzeLms(){
        ArrayList<Integer> aliveMovesets=new ArrayList<>();
        for(int i=0; i<movesets.size(); i++){
            if(movesets.get(i).getLMS()){
                aliveMovesets.add(i);
            }
        }
        if(aliveMovesets.size()==1){
            movesets.get(aliveMovesets.get(0)).ActivateLms();
            current_moveset=aliveMovesets.get(0);
        } else if(aliveMovesets.size()>1){
            Integer[] indexes=new Integer[aliveMovesets.size()];
            aliveMovesets.toArray(indexes);
            for(int i=0; i<indexes.length; i++){
                indexes[i]-=current_moveset;
                indexes[i]=Math.abs(indexes[i]);
            }
            int min=StaticQuickMAth.MinIndex(indexes);
            current_moveset=min;
        }
    }
    public void setCurrent_moveset(int index){
        current_moveset=index;
    }
    public void setMoveset(int index, Moveset moveset){
        movesets.set(index,moveset);
    }
    public boolean onTouch(float touchx, float touchy, int pointer){
        boolean ans=false;
        if (StaticBuffer.isPaused) {
            ans=ans||settins.settingdsisPressed(touchx,touchy,pointer);
        } else if(settingsButton.onTouch(touchx,touchy,pointer)){
            StaticBuffer.setIsPaused(true);
            ans=true;
        } else{
            if(!StaticBuffer.isCreative) {
                ans = ans || movesets.get(current_moveset).OnTouch(touchx, touchy, pointer);
                ans = ans || StaticBuffer.textRenderer.OnTouch(touchx, touchy);
            }
        }
        return ans;
    }
    public Moveset getCurrentMoveset(){
        return movesets.get(current_moveset);
    }
    public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer){
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        if(StaticBuffer.getIsPaused()){
            settins.draw_settings(shapeRenderer);
        } else{
            StaticBuffer.textRenderer.render2(shapeRenderer);
            movesets.get(current_moveset).drawButtonPaddings(shapeRenderer);
            for(int i=0; i<movesets.size();i++){
                movesets.get(i).getCharacterWidget().draw(shapeRenderer,i,movesets.size());
            }
        }
        shapeRenderer.end();
        spriteBatch.begin();
        if(StaticBuffer.getIsPaused()){
            settins.draw_settings(spriteBatch);
        } else{
            movesets.get(current_moveset).draw(spriteBatch);
            settingsButton.draw(spriteBatch,0.8f);
            StaticBuffer.textRenderer.render(spriteBatch);
        }
        spriteBatch.end();
    }
    public void Render(ModelBatch modelBatch){
    }
    public void update(){
        movesets.get(current_moveset).update();
    }

    @Override
    public void dispose() {
        for(int i=0; i<movesets.size(); i++){
            movesets.get(i).dispose();
        }
        settins.dispose();
        settingsButton.dispose();
    }
    public float getCurrentMovesetHp(){
        return movesets.get(current_moveset).getHp();
    }
    public void setCurrentMovesetHp(float hp){
        movesets.get(current_moveset).setHp(hp);
    }
    public float getCurrentIsroll(){
        return movesets.get(current_moveset).getIsRoll();
    }
    public void setIsLocked(boolean locked){
        isLocked=locked;
    }
    public boolean getIsLocked(){
        return isLocked;
    }

    public int getState() {
        return movesets.get(current_moveset).getCurrentState();
    }

    public boolean onUp(int touchx, int touchy, int pointer) {
        boolean ans=false;
        if (StaticBuffer.isPaused) {

        } else if(settingsButton.onTouch(touchx,touchy,pointer)){
            StaticBuffer.setIsPaused(true);
            ans=true;
        } else{
            ans=ans||movesets.get(current_moveset).OnRelease(touchx,touchy,pointer);
        }
        return ans;
    }
}
