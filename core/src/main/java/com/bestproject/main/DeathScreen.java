package com.bestproject.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bestproject.main.CostumeClasses.ScreenSpaceSim;
import com.bestproject.main.CostumeClasses.SpriteSheet;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.SoundManagement.CostumeSound;

public class DeathScreen implements ScreenSpaceSim, ScreenEmulator {
    public SpriteSheet dead;
    boolean invalid=false;
    static CostumeSound cs;
    BitmapFont bitmapFont;
    public float currentTime=0;
    String message = "ВЫ УМЕРЛИ ЛОЛ";
    int[] bounce=new int[message.length()];
    public DeathScreen(BitmapFont bitmapFont){
        this.bitmapFont=bitmapFont;
        if(cs==null){
            StaticBuffer.constantAssets.load("Sounds/Music/linganguli.mp3", Music.class);
            StaticBuffer.constantAssets.finishLoading();
            cs=new CostumeSound(StaticBuffer.constantAssets.get("Sounds/Music/linganguli.mp3",Music.class),false,1f);
        }
        currentTime=0;
        dead=new SpriteSheet(new Texture(Gdx.files.internal("Images/Effect2d/sprite_sheet-min.png")),4,2);

    }
    public void start(){
        StaticBuffer.soundManager.StopAllCons();
        invalid=false;
        cs.play();
        currentTime=0;
        cs.sound.setLooping(true);
        cs.sound.setVolume(0f);

    }
    @Override
    public void render(ShapeRenderer shapeRenderer) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        Gdx.gl20.glClearColor(0f,0f,0f,0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        int width= (int) (StaticBuffer.getScaleX()*500);
        Gdx.gl20.glClearColor(1f,1f,1f,1f);
        for (int i = 0; i < bounce.length; i++) {
            bounce[i] = (int) (Math.sin(currentTime * 5 + i * 0.4f) * 10);
        }
        int baseX = (int) (StaticBuffer.screenWidth / 2 - (int)(message.length() * 10)); // Adjust spacing as needed
        int baseY = (int) (StaticBuffer.screenHeight / 4);
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            bitmapFont.draw(spriteBatch, String.valueOf(c), baseX + i * 20, baseY + bounce[i]);
        }
        spriteBatch.draw(dead.getFrame((int) (currentTime*2)),StaticBuffer.screenWidth/2-width/2,StaticBuffer.screenHeight/2-width/2,width,width);

    }

    @Override
    public void update(float delta) {
        GameEngine.gameCore.scm.add(this);
        currentTime+=GameCore.deltatime;
        if(currentTime<2.5f){
            cs.sound.setVolume(currentTime/2.5f);
        } else{
            cs.sound.setVolume(1f);
        }
        StaticQuickMAth.setTimeFlow(0,1,true);
    }

    @Override
    public boolean expire() {
        return invalid;
    }

    @Override
    public void OnTouch(float screenX, float screenY, int pointer) {
        if(currentTime>1.5f) {
            invalid = true;
            cs.stop();
            for (int i = 0; i < StaticBuffer.ui.movesets.size(); i++) {
                StaticBuffer.ui.movesets.get(i).reset();
            }
            GameEngine.gameCore.setTemporaryMapBuffer(GameEngine.getGameCore().getMap().mapIndex);
        }
    }

    @Override
    public void OnScroll(float screenX, float screenY, int pointer) {

    }

    @Override
    public void OnUp(float screenX, float screenY, int pointer) {

    }
}
