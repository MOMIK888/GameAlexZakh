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
import com.bestproject.main.Maps.CITY;
import com.bestproject.main.Maps.Dungeon;
import com.bestproject.main.Maps.MapTest;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.Tiles.StoneTile;

public class GameEngine implements Disposable {
    public static GameCore gameCore;
    public GameEngine(){
        gameCore=new GameCore();
        gameCore.setMap(new CITY());
    }
    public void render(){
        gameCore.render();
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
