package com.bestproject.main.Tiles;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class ConcreteTile extends Tile{
    private HITBOX[] floorHB;

    public ConcreteTile(Vector3 position) {
        super(new ModelInstance(StaticBuffer.currentModels.get(1)), position);
        floorHB=new HITBOX[]{new HITBOX(position.x, position.z,position.y,2,2,0.3f)};
        floorHB[0].setType(HITBOX.HITBOXTYPES.FLOOR.getValue());
    }
    @Override
    public HITBOX[] gethb(){
        return floorHB;
    }

    @Override
    public void dispose() {
        disposeAll();
    }
}
