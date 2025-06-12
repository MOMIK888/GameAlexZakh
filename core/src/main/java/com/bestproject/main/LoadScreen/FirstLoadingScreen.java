package com.bestproject.main.LoadScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;

import java.io.FileNotFoundException;

public class FirstLoadingScreen extends LoadingScreen implements Screen {
    VideoPlayer videoPlayer;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    BitmapFont font=StaticBuffer.fonts[0];
    float w,h;
    float time=0;
    boolean errorTexture=false;
    public FirstLoadingScreen(String[] assets, AssetManager assetManager, String[][] materials) {
        super(assets, assetManager, materials);
        batch=new SpriteBatch();
        videoPlayer= VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setLooping(true);
        cdAnim=1f;
        shapeRenderer=new ShapeRenderer();
        w=700f*StaticBuffer.getScaleX();
        h=70*StaticBuffer.getScaleX();
        try {
            videoPlayer.play(Gdx.files.internal("LoadingSC/rainTown.mp4"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                Gdx.app.log("VideoPlayer", "Video playback completed: " + file.name());
            }
        });
    }

    @Override
    public void DrawAssets() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        time+=Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        videoPlayer.update();

        Texture frame = videoPlayer.getTexture();
        if (frame != null) {
            batch.begin();
            batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
            drawLoading();
        } else{
            errorTexture=true;
        }
        if(isLoaded){
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            shapeRenderer.setAutoShapeType(true);
            if(cdAnim>=0.5f){

            } else{

            }
            shapeRenderer.end();
        }
    }

    @Override
    public void show() {

    }
    private void drawLoading(){
        Color color=new Color(StaticBuffer.choice_colors[1]);
        Color secondcolor=new Color(StaticBuffer.choice_colors[0]);
        color.a=0.5f;
        secondcolor.a=0.5f;
        String loadingProgress="Загрузка";
        for(float i = 0; i<(time % 2f); i+=0.7f){
            loadingProgress+=".";
        }
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        shapeRenderer.setAutoShapeType(true);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(secondcolor);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.rect(StaticBuffer.screenWidth/2-w/2,200*StaticBuffer.getScaleY(),w,h);
        shapeRenderer.setColor(color);
        if(assets!=null && assets.length>0) {
            shapeRenderer.rect(StaticBuffer.screenWidth / 2 - w / 2, 200 * StaticBuffer.getScaleY(), w / assets.length *(currentindexingX ),h);
        }
        shapeRenderer.end();
        batch.begin();
        font.setColor(StaticBuffer.choice_colors[1]);
        font.getData().setScale(1f*StaticBuffer.getScaleX());
        char character='.';
        font.draw(batch,loadingProgress,StaticBuffer.screenWidth/2-font.getData().getGlyph(character).width*(int)(time%2/0.7f),235*StaticBuffer.getScaleY());
        if(currentindexingX<assets.length) {
            font.draw(batch, assets[currentindexingX], StaticBuffer.screenWidth / 2 - font.getData().getGlyph('h').width * assets[currentindexingX].length() / 2, 200 * StaticBuffer.getScaleY());

        } else{
            String string = "Последние штрихи...";
        }
        batch.end();


    }

    @Override
    public void recompile(String[] assets, AssetManager assetManager, String[][] materials) {
        super.recompile(assets, assetManager, materials);
        if(errorTexture){
            errorTexture=false;
            videoPlayer= VideoPlayerCreator.createVideoPlayer();
            videoPlayer.setLooping(true);
            System.out.println("RECOMPILED!");
            try {
                videoPlayer.play(Gdx.files.internal("LoadingSC/rainTown.mp4"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}
