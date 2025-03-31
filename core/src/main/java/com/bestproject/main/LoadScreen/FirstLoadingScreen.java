package com.bestproject.main.LoadScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
    public FirstLoadingScreen(String[] assets, AssetManager assetManager, String[][] materials) {
        super(assets, assetManager, materials);
        batch=new SpriteBatch();
        videoPlayer= VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setLooping(true);
        cdAnim=0.1f;
        shapeRenderer=new ShapeRenderer();
        try {
            videoPlayer.play(Gdx.files.internal("LoadingSC/raintown.ogv"));
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        videoPlayer.update();

        Texture frame = videoPlayer.getTexture();
        if (frame != null) {
            batch.begin();
            batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }
        if(isLoaded){
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            if(cdAnim>=0.5f){

            } else{

            }
            shapeRenderer.end();
        }
    }

    @Override
    public void show() {

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
    }
}
