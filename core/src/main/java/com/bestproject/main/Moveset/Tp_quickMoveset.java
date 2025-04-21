package com.bestproject.main.Moveset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Attacks.Attack;
import com.bestproject.main.Attacks.Blast;
import com.bestproject.main.Attacks.Flintlock;
import com.bestproject.main.Attacks.coinFling;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Effects.Effect;
import com.bestproject.main.Effects.SnapFlash;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;

public class Tp_quickMoveset extends Moveset{
    Color[] colors; //undisposable
    coinFling coin=null; //disposed
    public boolean jump=false;
    public boolean dash;
    public boolean isUlting=false;
    Flintlock flintlock;
    Model FlintclockModel;
    Texture[] Image_variations;

    public Tp_quickMoveset(){
        super();
        charinfo=3;
        colors=new Color[]{new Color(Color.rgba8888(0.4f, 0.2f, 0.6f,0.6f)),new Color(Color.rgba8888(0f, 0.8f, 1f,0.6f))};
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1725,75,175,175));
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1900,175,225,225));
        buttons.add(new ImageButton("Images/ButtonIcons/ult1.png",1600,50,120,120));
        buttons.add(new ImageButton("Images/ButtonIcons/dash.png",2150,280,170,170));
        buttons.add(new ImageButton("Images/ButtonIcons/dash.png",2150,100,100,100));
        StaticBuffer.assetManager.load("Models/Attacks/blast.g3dj",Model.class);
        StaticBuffer.assetManager.load("Models/Char2/girl.g3dj", Model.class);
        StaticBuffer.assetManager.load("Models/Minor_models/flintlock.g3dj", Model.class);
        StaticBuffer.assetManager.finishLoading();
        FlintclockModel=StaticBuffer.assetManager.get("Models/Minor_models/flintlock.g3dj", Model.class);
        flintlock=new Flintlock(FlintclockModel,new Vector3());
        characterModel=StaticBuffer.assetManager.get("Models/Char2/girl.g3dj", Model.class);
        attacks.add(StaticBuffer.assetManager.get("Models/Attacks/blast.g3dj", Model.class));
        hp=100f;
        maxHp=100f;
        stamina=100;
        Image_variations=new Texture[]{new Texture("Images/ButtonIcons/coinToss.png"), new Texture("Images/ButtonIcons/flintlock.png") };

    }

    @Override
    public void dispose() {
        super.dispose();
        coin.dispose();
        FlintclockModel.dispose();
    }

    @Override
    public void update() {
        super.update();
        if(coin!=null){
            if(coin.expire()){
                coin=null;
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
        if(current_state==5){
            jump=buttons.get(4).getPointer()!=-1;
            if(!jump){
               current_state=0;
            } else{
                current_state=5;
            }
        }
        if(current_state==4){
            dash=buttons.get(3).getPointer()!=-1;
            if(!dash){
                current_state=0;
            } else{
                current_state=4;
            }
        }
    }
    @Override
    public void draw(SpriteBatch spriteBatch){
        for(int i=0; i<buttons.size(); i++){
            buttons.get(i).draw(spriteBatch, 0.6f);
        }
        if(isUlting) {
            StaticBuffer.fonts[0].getData().setScale(1f);
            StaticBuffer.fonts[0].draw(spriteBatch, String.valueOf(flintlock.getChance()), buttons.get(2).getCenterX(), buttons.get(2).getCenterY());
        }
    }

    @Override
    public void drawButtonPaddings(ShapeRenderer shapeRenderer) {
        buttons.get(0).drawButton(shapeRenderer,buttons.get(0).bounds.x,buttons.get(0).bounds.y,buttons.get(0).getRadius()*2,buttons.get(0).getRadius()*2,buttons.get(0).getRadius(),colors[1],colors[0], charge[0]);
        buttons.get(1).drawButton(shapeRenderer,buttons.get(1).bounds.x,buttons.get(1).bounds.y,buttons.get(1).getRadius()*2,buttons.get(1).getRadius()*2,buttons.get(1).getRadius(),colors[1],colors[0], 100f-cooldowns[0]*300f);
        buttons.get(2).drawButton(shapeRenderer,buttons.get(2).bounds.x,buttons.get(2).bounds.y,buttons.get(2).getRadius()*2,buttons.get(2).getRadius()*2,buttons.get(2).getRadius(),colors[1],colors[0], charge[1]);
        buttons.get(3).drawButton(shapeRenderer,buttons.get(3).bounds.x,buttons.get(3).bounds.y,buttons.get(3).getRadius()*2,buttons.get(3).getRadius()*2,buttons.get(3).getRadius(),colors[1],colors[0],100f-cooldowns[1]*300f);
        buttons.get(4).drawButton(shapeRenderer,buttons.get(4).bounds.x,buttons.get(4).bounds.y,buttons.get(4).getRadius()*2,buttons.get(4).getRadius()*2,buttons.get(4).getRadius(),colors[1],colors[0],100f-cooldowns[3]*300f);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(1000,80,maxHp*4,80);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(1000,80,hp*4,80);
    }

    @Override
    public boolean OnTouch(float touchX, float touchY, int pointer) {
        for(int i=0; i<buttons.size(); i++){
            if(buttons.get(i).onTouch(touchX,touchY,pointer)){
                button_suggestion(i);
                buttons.get(i).setPointer(pointer);
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
                    GameEngine.getGameCore().getMap().addStatic(new SnapFlash(StaticBuffer.getPlayerCooordinates(),0.2f));
                    StaticQuickMAth.setTimeFlow(0.5f,0.2f);
                    current_state=2;
                    if(coin!=null){
                        coin.pluslengh(0.25f);
                        cd=0.13f;
                    }
                }
            } else if (act == 2) {
                if(charge[1]>99) {
                    simoltanious_buttons.add(act);
                    cd = 0.2f;
                    current_state=3;
                    isUlting=true;
                }
                if(isUlting){
                    simoltanious_buttons.add(act);
                }
            } else if(act==1){
                simoltanious_buttons.add(1);
                isPunch=true;
                cd=0.25f;
                current_state=1;
            } else if (act == 3) {
                current_state=4;
            } else if(act==4){
                current_state=5;
                jump=true;

            }
        }
    }

    @Override
    public void PlayerInterrractions(Player player) {
        float deltatime = GameEngine.getGameCore().getDeltatime();
        Vector2 vec=GameEngine.getGameCore().getJoystick().getDirection();
        player.unnormalizedMovement.set(StaticQuickMAth.move(vec.x*deltatime*player.speed),0, StaticQuickMAth.move(-vec.y*deltatime*player.speed));
        float angle = vec.angle();
        float multiplier=1f;
        player.hitboxes[0].setHeight(1.4f);
        int current_state=player.current_state;
        if (!player.unnormalizedMovement.isZero() && StaticBuffer.ui.getState()!=4) {
            player.lastdir=player.lastdir.set(player.movement.x,0,player.movement.z);
                if (current_state != 0 && StaticBuffer.ui.getState() == 0) {
                    player.current_state = 0;
                    player.animationController.setAnimation("metarig|walk", -1);
            }
            player.modelInstance.transform.rotate(0, 1, 0, angle - player.lastangle);
            player.lastangle = angle;
        }else if(StaticBuffer.ui.getState()==0){
            player.speed=1f;
            if(current_state!=1 ){
                player.current_state=1;
                player.animationController.setAnimation("metarig|idle",-1);
            }
        }
        if(StaticBuffer.ui.getState()==2){
            if(current_state!=2){
                player.current_state=2;
                player.animationController.setAnimation("metarig|arms aross", 1);
            }
            multiplier=2f;
        }else if(StaticBuffer.ui.getState()==1){
            if(current_state!=3){
                player.current_state=3;
                player.animationController.setAnimation("metarig|Shoot",1);
            }
        }else if (StaticBuffer.ui.getState()==4) {
            player.speed=4f;
            player.hitboxes[0].setHeight(0.1f);
            player.modelInstance.transform.rotate(0, 1, 0, angle - player.lastangle);
            player.lastangle = angle;
            player.current_state=4;
            player.animationController.setAnimation("metarig|slide",1);
            StaticBuffer.dust.setPosition(new Vector3(StaticBuffer.getPlayerCooordinates()).add(new Vector3(0,0.3f,0)));
            StaticBuffer.dust.update(StaticQuickMAth.move(GameCore.deltatime));
            StaticBuffer.dust.render();
        }
        if(this.current_state==1){
            if(!player.animationController.inAction){
                this.current_state=0;
            }
        }
        player.animationController.update(StaticQuickMAth.move(deltatime)*multiplier*player.speed);
        player.fractureMovement(player.movement);
        player.movement.set(0,0,0);
        player.movement.add(player.unnormalizedMovement);
        if(player.speed>3f){
            player.speed-=StaticQuickMAth.move(deltatime/2);
        } else{
            player.speed+=StaticQuickMAth.move(deltatime);
        }
        if (simoltanious_buttons.contains(0)) {
            if (coin != null && !coin.expire()) {
                Vector3 coin_position = new Vector3(coin.getPosition());
                GameEngine.getGameCore().getMap().setPlayerImactFrame(0.08f);
                player.setPosition(coin_position);
                player.gravity_multip=0.1f;
                player.setForce(coin.getAcceleration());
                coin.setCostume(true);
                charge[0] = 0;
                current_state = 0;
                simoltanious_buttons.clear();
                return;
            }
        }
    }

    @Override
    public boolean OnRelease(float touchX, float touchY, int pointer) {
        for(int i=0; i<buttons.size(); i++){
            if(buttons.get(i).getPointer()==pointer){
                buttons.get(i).release();
                return true;
            }
        }
        return false;
    }

    @Override
    public void goThrouh(){
        if (simoltanious_buttons.contains(0) && isPunch && !isUlting) {
            float[] sin_cos;
            if(StaticBuffer.ui.getIsLocked()) {
                lock_omn_coordinates=GameEngine.getGameCore().getMap().tie_coordinates(lock_omn_coordinates,GameEngine.getGameCore().getMap().calculate_radius(2,new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]}));
                sin_cos=StaticBuffer.getSin_Cos(StaticBuffer.getPlayerCooordinates(),lock_omn_coordinates);
            } else{
                sin_cos=new float[]{-MathUtils.cosDeg(GameCore.cameraRoationm),-MathUtils.sinDeg(GameCore.cameraRoationm)};
            }
            coin=new coinFling(new Vector3(StaticBuffer.getPlayerCooordinates()),new Vector3(sin_cos[1],-MathUtils.sinDeg(GameCore.cameraRoationX),sin_cos[0]));
            GameEngine.getGameCore().getMap().addMoving(coin);
            StaticQuickMAth.setTimeFlow(0.5f,0.2f);
            isPunch=false;
            simoltanious_buttons.clear();

        } else if (simoltanious_buttons.contains(2)) {
            charge[1]=0;
            simoltanious_buttons.clear();

        } else if (isPunch && cooldowns[0]<=0){
            if(!isUlting) {
                if (!StaticBuffer.ui.getIsLocked()) {
                    lock_omn_coordinates = GameEngine.getGameCore().getMap().tie_coordinates(lock_omn_coordinates, GameEngine.getGameCore().getMap().calculate_radius(2, new int[]{StaticBuffer.getPlayer_coordinates()[1], StaticBuffer.getPlayer_coordinates()[0]}));
                }
                float[] sin_cos = new float[]{-MathUtils.cosDeg(GameCore.cameraRoationm),-MathUtils.sinDeg(GameCore.cameraRoationm)};
                GameEngine.getGameCore().getMap().addMoving(new Blast(new ModelInstance(attacks.get(0)), StaticBuffer.getPlayerCooordinates(), new float[]{sin_cos[1],-MathUtils.sinDeg(GameCore.cameraRoationX),sin_cos[0]}));
                isPunch = false;
                cooldowns[0] = 0.3f;
                if (!StaticBuffer.ui.getIsLocked()) {
                    lock_omn_coordinates = new Vector3();
                }
                simoltanious_buttons.clear();
            } else{
                isUlting=false;
                flintlock.setExplosion(true);
                flintlock.setPosition(StaticBuffer.getPlayerCooordinates());
                GameEngine.getGameCore().getMap().addMoving(flintlock);
            }
        }else if (simoltanious_buttons.contains(0)) {
            if(isUlting){
                flintlock.rollCoin();
                simoltanious_buttons.clear();
                return;
            }
            if(coin!=null){
                return;
            }
            int[] index=GameEngine.getGameCore().getMap().getNearestObject(StaticBuffer.getPlayerCooordinates(),GameEngine.getGameCore().getMap().calculate_radius(2,new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]}));
            if(index[0]!=-1){
                GameEngine.getGameCore().getMap().swap(new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]},index);
                charge[0]=0;
            }
            simoltanious_buttons.clear();
        } else if(simoltanious_buttons.contains(3) && cooldowns[1]<=0){
            cooldowns[1]=1.4f;
            cd=0.6f;
            simoltanious_buttons.clear();
        }
        simoltanious_buttons.clear();
        current_state=0;
    }
    @Override
    public void ActivateLms(){
        super.ActivateLms();
        damageMultiplier=1.5f;
    }
}
