package com.bestproject.main.Tiles;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class StoneTile extends Tile implements Disposable {
    private static Model stoneModel;
    private HITBOX[] floorHB;

    static {
        StaticBuffer.assetManager.load("Models/stone_tile.g3db", Model.class);
        StaticBuffer.assetManager.finishLoading();
        stoneModel = StaticBuffer.assetManager.get("Models/stone_tile.g3db", Model.class);
    }

    public StoneTile(Vector3 position) {
        super(new ModelInstance(stoneModel), position);
        floorHB=new HITBOX[]{new HITBOX(position.x, position.z,position.y,2,2,0.3f)};
        floorHB[0].setType(HITBOX.HITBOXTYPES.FLOOR.getValue());
    }
    @Override
    public HITBOX[] gethb(){
        return floorHB;
    }
    public static void disposeAll() {
        if (stoneModel != null) {
            stoneModel.dispose();
        }
    }

    @Override
    public void dispose() {
        disposeAll();
    }
}
