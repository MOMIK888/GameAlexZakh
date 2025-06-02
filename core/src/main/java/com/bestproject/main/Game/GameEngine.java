package com.bestproject.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.Joystick;
import com.bestproject.main.Maps.BossArena;
import com.bestproject.main.Maps.CITY;
import com.bestproject.main.Maps.ChunkedCity;
import com.bestproject.main.Maps.Dungeon;
import com.bestproject.main.Maps.MapTest;
import com.bestproject.main.Maps.Tavern;
import com.bestproject.main.Maps.Village;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StartingScreen;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.Tiles.StoneTile;

public class GameEngine implements Disposable {
    public static GameCore gameCore;
    StartingScreen startingScreen;
    boolean isDisposed=false;
    public GameEngine(){
        gameCore=new GameCore();
        gameCore.setMap(new Village());
        startingScreen=new StartingScreen();
    }
    public void render(){
        if(isDisposed) {
            gameCore.render();
        } else{
            startingScreen.render();
            isDisposed=startingScreen.gameStarted;
            if(isDisposed){
                gameCore.setInput();
                startingScreen.dispose();
                startingScreen=null;
            }
        }
        StaticQuickMAth.updateTime();
    }
    @Override
    public void dispose() {
        gameCore.dispose();
    }

    public static GameCore getGameCore() {
        return gameCore;
    }
}
