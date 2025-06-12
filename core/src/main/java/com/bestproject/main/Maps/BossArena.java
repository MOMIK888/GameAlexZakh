package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CostumeClasses.HeightBasedRect;
import com.bestproject.main.Dialogues.DialogueDecal;
import com.bestproject.main.Enemies.Mech;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.EffectMap.StaticEffect;

public class BossArena extends SingleMeshMap{
    public BossArena(){
        super(3);
        mapIndex=3;
    }
    @Override
    public void MapInitialization(){
        super.MapInitialization();
        uniqueTextures=new Texture[]{new Texture(Gdx.files.internal("Images/Effect2d/light.png"))};
        if(StaticBuffer.info.isLocationFetched) {
            movingObjects2.add(new Player(new Vector3(8f,0.30f,8f)));
        } else{
            movingObjects2.add(new Player(StaticBuffer.info.getPlayerLocation()));
        }
        movingObjects2.add(new Mech(new Vector3(8f,0.30f,8f)));
        skybox=new ColorFulSkybox("Models/Skyboxes/skybox2.g3dj","Models/Skyboxes/SkyboxBlue.png",4f);
        singleObjectMap.scale(0.01f,0.01f,0.01f);
        this.weatherEffector=null;
        this.weatherArea=null;
        skybox.setCurrentTime(1);

    }
    @Override
    public void LoadDependencies(){
        StaticBuffer.initialize_BossArena_models();
        StaticBuffer.LoadEffects();
    }
    @Override
    protected void initialize(){
        modelBatch=new ModelBatch();
        gridinit();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1f));
    }
    @Override
    public void check(){
    }
    @Override
    protected void additionalRender(){
    }
    @Override
    public void update(int a, int b, int c, int d){
        super.update(a,b,c,d);
    }

}
