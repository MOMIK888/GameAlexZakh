package com.bestproject.main.Maps;

import static com.bestproject.main.StaticBuffer.screenHeight;
import static com.bestproject.main.StaticBuffer.screenWidth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CostumeClasses.HeightBasedRect;
import com.bestproject.main.Dialogues.DialogueDecal;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.EffectMap.StaticEffect;
import com.bestproject.main.StaticObjects.FastTravel;

public class Village extends SingleMeshMap{
    private float isInZone;
    DialogueDecal dialogueDecal=new DialogueDecal(StaticBuffer.fonts[0],new Texture[0], "Войти?");
    public Village(){
        super(0);
        mapIndex=0;
    }
    @Override
    public void MapInitialization(){
        super.MapInitialization();
        uniqueTextures=new Texture[]{new Texture(Gdx.files.internal("Images/Effect2d/light.png"))};
        if(StaticBuffer.info.isLocationFetched) {
            movingObjects2.add(new Player(new Vector3(0f, 0.30f, 8f)));
        } else{
            movingObjects2.add(new Player(StaticBuffer.info.getPlayerLocation()));
        }
        staticObjects2.add(new StaticEffect(new Vector3(0.93f,1.4f, 6.790546f),uniqueTextures[0],1,1,0.5f));
        triggerZones.add(new HeightBasedRect[]{new HeightBasedRect(new Rectangle(0.63f,6.6f,0.6f,0.4f),4f,0)});
        staticObjects2.add(new FastTravel(StaticBuffer.Testmodel, new Vector3(0.93f,0f, 10.790546f)));
        skybox=new ColorFulSkybox("Models/Skyboxes/skybox2.g3dj","Models/Skyboxes/SkyboxBlue.png",4f);
        skybox.setCurrentTime(1);
    }
    @Override
    public void LoadDependencies(){
        StaticBuffer.initialize_Village_models();;
        StaticBuffer.LoadEffects();
    }
    @Override
    protected void initialize(){
        modelBatch=new ModelBatch();
        gridinit();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 0f));
    }
    @Override
    public void check(){
        int index=check_zones();
        if(index==0){
            isInZone=0.5f;
        }
    }
    @Override
    protected void additionalRender(){
        if(isInZone>0){
            isInZone-= GameCore.deltatime;
            dialogueDecal.render(StaticBuffer.decalBatch, GameCore.camera);
            if(dialogueDecal.getClick()){
                GameEngine.getGameCore().setTemporaryMapBuffer(0);
            }
        }
    }
    @Override
    public void update(int a, int b, int c, int d){
        super.update(a,b,c,d);
        dialogueDecal.update(GameCore.deltatime);
        dialogueDecal.setPosition(new Vector3(0.63f,0.5f,6.6f));
    }

}

