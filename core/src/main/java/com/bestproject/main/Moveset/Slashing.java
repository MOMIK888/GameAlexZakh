package com.bestproject.main.Moveset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Attacks.Attack;
import com.bestproject.main.Attacks.Blast;
import com.bestproject.main.Attacks.coinFling;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Effects.SnapFlash;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Slashing extends Moveset{
    Color[] colors; //undisposable
    public Slashing(){
        super();
        charinfo=2;
        colors=new Color[]{new Color(Color.rgba8888(0f, 239f/255f, 1f,0.6f)),new Color(Color.rgba8888(14f/255f, 80f/255f, 233f/255f,0.6f))};
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1725,75,175,175));
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1900,175,225,225));
        buttons.add(new ImageButton("Images/ButtonIcons/ult1.png",1600,50,120,120));
        buttons.add(new ImageButton("Images/ButtonIcons/dash.png",2150,280,170,170));
        hp=100f;


    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update() {
        super.update();
        if(isRoll>0){
            isRoll-= StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
            current_state=4;
            if(isRoll<=0){
                current_state=0;
            }
        }
        ability_cooldown=ability_cooldown-(StaticQuickMAth.move(GameEngine.getGameCore().deltatime));
        charge[0]+=(StaticQuickMAth.move(GameEngine.getGameCore().deltatime)*80);
        cd-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
        for(int i=0; i<cooldowns.length; i++){
            if(cooldowns[i]>=0){
                cooldowns[i]-=GameEngine.getGameCore().deltatime;
            }
        }
        if (cd<0){
            if (!simoltanious_buttons.isEmpty()) {
                goThrouh();
            }
        }
    }
    @Override
    public void draw(SpriteBatch spriteBatch){
        for(int i=0; i<buttons.size(); i++){
            buttons.get(i).draw(spriteBatch, 0.6f);
        }
    }

    @Override
    public void drawButtonPaddings(ShapeRenderer shapeRenderer) {
        buttons.get(0).drawButton(shapeRenderer,buttons.get(0).bounds.x,buttons.get(0).bounds.y,buttons.get(0).getRadius()*2,buttons.get(0).getRadius()*2,buttons.get(0).getRadius(),colors[1],colors[0], charge[0]);
        buttons.get(1).drawButton(shapeRenderer,buttons.get(1).bounds.x,buttons.get(1).bounds.y,buttons.get(1).getRadius()*2,buttons.get(1).getRadius()*2,buttons.get(1).getRadius(),colors[1],colors[0], 100f-cooldowns[0]*300f);
        buttons.get(2).drawButton(shapeRenderer,buttons.get(2).bounds.x,buttons.get(2).bounds.y,buttons.get(2).getRadius()*2,buttons.get(2).getRadius()*2,buttons.get(2).getRadius(),colors[1],colors[0], charge[1]);
        buttons.get(3).drawButton(shapeRenderer,buttons.get(3).bounds.x,buttons.get(3).bounds.y,buttons.get(3).getRadius()*2,buttons.get(3).getRadius()*2,buttons.get(3).getRadius(),colors[1],colors[0],100f-cooldowns[1]*300f);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(1000,80,hp*4,80);
    }

    @Override
    public boolean OnTouch(float touchX, float touchY, int pointer) {
        for(int i=0; i<buttons.size(); i++){
            if(buttons.get(i).onTouch(touchX,touchY,pointer)){
                button_suggestion(i);
                return true;
            };
        }
        return false;
    }

    @Override
    public void button_suggestion(int act) {
        if(!simoltanious_buttons.contains(act)){
            if (act==0) {
                if (charge[0]>99) {
                    simoltanious_buttons.add(act);
                    cd=0.18f;
                    ability_cooldown=0.1f;
                    current_state=2;
                }
            } else if (act == 2) {
                if(charge[1]>99) {
                    simoltanious_buttons.add(act);
                    cd = 0.2f;
                    current_state=3;
                }
            } else if(act==1){
                simoltanious_buttons.add(1);
                isPunch=true;
                cd=0.25f;
                current_state=1;
            } else if (act == 3) {
                simoltanious_buttons.add(3);
                current_state=4;
            }
        }
    }
    @Override
    public void goThrouh(){
        if (simoltanious_buttons.contains(0) && isPunch) {
            if(!StaticBuffer.ui.getIsLocked()) {
                lock_omn_coordinates=GameEngine.getGameCore().getMap().tie_coordinates(lock_omn_coordinates,GameEngine.getGameCore().getMap().calculate_radius(2,new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]}));
            }
            float[] sin_cos=StaticBuffer.getSin_Cos(StaticBuffer.getPlayerCooordinates(),lock_omn_coordinates);
            isPunch=false;
            simoltanious_buttons.clear();

        } else if (simoltanious_buttons.contains(2)) {
            charge[1]=0;
            simoltanious_buttons.clear();

        } else if (isPunch && cooldowns[0]<=0){
            if(!StaticBuffer.ui.getIsLocked()) {
                lock_omn_coordinates=GameEngine.getGameCore().getMap().tie_coordinates(lock_omn_coordinates,GameEngine.getGameCore().getMap().calculate_radius(2,new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]} ));
            }
            cooldowns[0]=0.3f;
            if(!StaticBuffer.ui.getIsLocked()) {
                lock_omn_coordinates=new Vector3();
            }
            simoltanious_buttons.clear();
        }else if (simoltanious_buttons.contains(0)) {
            charge[0]=0;
            simoltanious_buttons.clear();
        } else if(simoltanious_buttons.contains(3) && cooldowns[1]<=0){
            isRoll=0.6f;
            cooldowns[1]=1.4f;
            cd=0.6f;
            simoltanious_buttons.clear();
        }
        simoltanious_buttons.clear();
        current_state=0;
    }
    @Override
    public void PlayerInterrractions(Player player) {

    }
}

