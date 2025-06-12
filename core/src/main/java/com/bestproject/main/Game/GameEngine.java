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
import com.bestproject.main.MainGame;
import com.bestproject.main.Maps.BossArena;
import com.bestproject.main.Maps.CITY;
import com.bestproject.main.Maps.ChunkedCity;
import com.bestproject.main.Maps.Dungeon;
import com.bestproject.main.Maps.MapTest;
import com.bestproject.main.Maps.Tavern;
import com.bestproject.main.Maps.Village;
import com.bestproject.main.Moveset.Moveset;
import com.bestproject.main.Moveset.PizzaGuy;
import com.bestproject.main.Moveset.Tp_quickMoveset;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StartingScreen;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.Tiles.StoneTile;

public class GameEngine implements Disposable {
    public static GameCore gameCore;
    StartingScreen startingScreen;
    boolean isDisposed=false;
    public GameEngine(){
        if(MainGame.databaseInterface[2].getInfo(0)==null) {
            MainGame.databaseInterface[2].setInfo(0, "");
        }
        StaticBuffer.loadAllValues(MainGame.databaseInterface[2].getInfo(0));
        StaticQuickMAth.time=StaticBuffer.info.time;
        StaticBuffer.inventory.decipher(MainGame.databaseInterface[2].getInfo(2));
        StaticBuffer.questManager.decipherData(MainGame.databaseInterface[2].getInfo(1));
        gameCore=new GameCore();
        if(StaticBuffer.info.mapIndex==3){
            gameCore.setMap(new BossArena());
        } else {
            gameCore.setMap(new Village());
        }
        for(int i=0; i<StaticBuffer.info.movesets.length; i++){
            if(StaticBuffer.info.movesets[i]==1){
                Moveset moveset=new PizzaGuy();
                moveset.setHp(StaticBuffer.info.movesetshp[i]);
                moveset.chargeUlt(StaticBuffer.info.movesetUltCharge[i]);
                StaticBuffer.ui.movesets.add(moveset);
            } else if (StaticBuffer.info.movesets[i] == 3) {
                Moveset moveset=new Tp_quickMoveset();
                moveset.setHp(StaticBuffer.info.movesetshp[i]);
                moveset.chargeUlt(StaticBuffer.info.movesetUltCharge[i]);
                StaticBuffer.ui.movesets.add(moveset);
            }
        }
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
