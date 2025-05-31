package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
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

public class Tavern extends SingleMeshMap {
    float isInZone=0;
    DialogueDecal dialogueDecal=new DialogueDecal(StaticBuffer.fonts[0],new Texture[0], "Войти?");
    public Tavern(){
        super(1);
        mapIndex=1;
    }
    @Override
    public void MapInitialization(){
        super.MapInitialization();
        movingObjects2.add(new Player(new Vector3(-3f,0.30f,2.7f)));
        singleObjectMap.scale(0.00003f,0.00003f,0.00003f);
        singleObjectMap.setPosition(new Vector3(-7,0.25f,0));
        weatherEffector=null;
        for(int i=0; i<singleObjectMap.getModelInstance().materials.size; i++) {
            if(singleObjectMap.getModelInstance().materials.get(i).id.equals("Lamp.001")){
                singleObjectMap.getModelInstance().materials.get(i).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            } else if(singleObjectMap.getModelInstance().materials.get(i).id.equals("glass")){
                singleObjectMap.getModelInstance().materials.get(i).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            } else if(singleObjectMap.getModelInstance().materials.get(i).id.equals("Material #174")){
                singleObjectMap.getModelInstance().materials.removeIndex(i);
                singleObjectMap.getModelInstance().materials.get(i).set(new Material("Material #174",new TextureAttribute(TextureAttribute.Diffuse,new Texture("Models/SingleMeshMaps/Tavern/уиу.png"))));
            }
        }
        triggerZones.add(new HeightBasedRect[]{new HeightBasedRect(new Rectangle(-3.2f,2.5f,0.4f,0.4f),4f,0f)});


    }
    @Override
    public void LoadDependencies(){
        StaticBuffer.initialize_Tavern_models();;
        StaticBuffer.LoadEffects();
    }
    @Override
    protected void initialize(){
        skybox=new ColorFulSkybox("Models/Skyboxes/skybox2.g3dj","Models/Skyboxes/SkyboxBlue.png",4f);
        modelBatch=new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(1f,1f,1f,1f).lerp(new Color(Color.YELLOW),0.1f).lerp(new Color(Color.BLACK),0.1f)));
        weatherEffector=null;
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
            dialogueDecal.render(StaticBuffer.decalBatch,modelBatch.getCamera());
            if(dialogueDecal.getClick()){
                GameEngine.getGameCore().setTemporaryMapBuffer(1);
            }
        }
    }
    @Override
    public void update(int a, int b, int c, int d){
        super.update(a,b,c,d);
        dialogueDecal.update(GameCore.deltatime);
    }

}
