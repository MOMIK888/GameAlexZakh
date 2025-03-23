package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class TestCollision extends StaticObject {
    private static Model stoneModel;
    private static AssetManager assetManager;
    HITBOX[] hitboxes;

    static {
        assetManager = new AssetManager();
        assetManager.load("Models/player.g3db", Model.class);
        assetManager.finishLoading();
        stoneModel = assetManager.get("Models/player.g3db", Model.class);
    }
    public TestCollision(Vector3 position) {
        super(new ModelInstance(stoneModel), position);
        modelInstance.transform.scale(0.3f,0.3f,0.3f);
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z, position.y,0.25,0.25,0.25)};
    }
    @Override
    public HITBOX[] gethbs() {
        return hitboxes;
    }

    public static void disposeAll() {
        if (stoneModel != null) {
            stoneModel.dispose();
        }
        if (assetManager != null) {
            assetManager.dispose();
        }
    }
    @Override
    public void update(){

    }

    @Override
    public void dispose() {
        disposeAll();
    }
}
