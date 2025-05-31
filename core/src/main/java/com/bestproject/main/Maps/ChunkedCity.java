package com.bestproject.main.Maps;

import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.MainGame;
import com.bestproject.main.MapUtils.Chunk;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.StaticBuffer;

public class ChunkedCity extends ChuckedMap{
    public ChunkedCity() {
        super("RANDOM", 0, 50, 50, 2);
    }

    @Override
    public void LoadDependencies() {
        StaticBuffer.initialize_BigCity_models();
    }

    @Override
    public void MapInitialization() {
        super.MapInitialization();
        chunks.add(new Chunk(0,0,StaticBuffer.assetManager.get("Models/Chunks/Chunk1/chunk1.g3dj"), MainGame.databaseInterface[0].getInfo(2)));
        movingObjects2.add(new Player(new Vector3(0f,0.30f,8f)));
    }
}
