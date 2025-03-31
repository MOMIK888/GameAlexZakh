package com.bestproject.main.RenderOverride;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.CostumeClasses.SpriteSheetDecal;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticQuickMAth;

public class DomainExpansion extends RenderOverride{
    Array<Slash> decals=new Array<>();
    ColorFulSkybox skybox;
    SpriteSheetDecal spriteSheetDecal;
    int count=60;
    Vector3 slashingArea=new Vector3(40,20,40);
    public DomainExpansion(ColorFulSkybox skybox){
        this.skybox=skybox;
        spriteSheetDecal=new SpriteSheetDecal(new Texture("Images/Effect2d/fireSlashes.png"),4,4,0.04f);
        for(int i=0; i<count; i++){
            decals.add(new Slash(spriteSheetDecal.getDecal(),new Vector3(MathUtils.random(0f,slashingArea.x),MathUtils.random(0f,slashingArea.y),MathUtils.random(0f,slashingArea.z)),new Vector2(MathUtils.random(1f,3f),MathUtils.random(1f,3f)),new Vector3(MathUtils.random(0f,360f),MathUtils.random(0f,360f),MathUtils.random(0f,360f))));
        }

    }
    @Override
    public void Render(Array<Array<Array<MovingObject>>> movingObjects,Array<Array<Array<StaticObject>>> staticObjects){
        GameEngine.getGameCore().getMap().setStaticRender(false);
        skybox.render(GameCore.camera);
        for(Slash decal : decals){
            StaticBuffer.decalBatch.add(decal.decal);
        }
    }
    @Override
    public void Update(){
        float delta= StaticQuickMAth.move(GameCore.deltatime);
        for(Slash decal : decals){
            decal.update(delta,spriteSheetDecal);
        }
        for(int i=0; i<decals.size; i++){
            if(decals.get(i).Expire(spriteSheetDecal)){
                decals.removeIndex(i);
                i-=1;
                decals.add(new Slash(spriteSheetDecal.getDecal(),new Vector3(MathUtils.random(0f,slashingArea.x),MathUtils.random(0f,slashingArea.y),MathUtils.random(0f,slashingArea.z)),new Vector2(MathUtils.random(1f,3f),MathUtils.random(1f,3f)),new Vector3(MathUtils.random(0f,360f),MathUtils.random(0f,360f),MathUtils.random(0f,360f))));
            }
        }
    }
    @Override
    public void expire(){
        GameEngine.getGameCore().getMap().setStaticRender(true);
    }
}
class Slash{
    Decal decal;
    float time=0;
    Vector3 position;
    Vector2 Dimensions;
    Vector3 rotation;
    public Slash(Decal decal, Vector3 position, Vector2 Dimensions, Vector3 rotation){
        this.position=position;
        this.decal=decal;
        this.rotation=rotation;
        this.Dimensions=Dimensions;
        decal.setDimensions(Dimensions.x,Dimensions.y);
        decal.setPosition(position);
        decal.setRotationY(0);decal.setRotationX(0);decal.setRotationZ(0);
        decal.rotateY(rotation.y);
        decal.rotateX(rotation.x);
        decal.rotateZ(rotation.z);

    }
    public void update(float delta, SpriteSheetDecal spriteSheetDecal){
        time+=delta* MathUtils.random(0.5f,2f);
        spriteSheetDecal.setStateTime(time);
        decal=spriteSheetDecal.getDecal();
        decal.setPosition(position);
        decal.setDimensions(Dimensions.x,Dimensions.y);
        decal.setRotationY(0);decal.setRotationX(0);decal.setRotationZ(0);
        decal.rotateY(rotation.y);
        decal.rotateX(rotation.x);
        decal.rotateZ(rotation.z);

    }
    public boolean Expire(SpriteSheetDecal spriteSheetDecal){
        return spriteSheetDecal.getDuration()<time;
    }

}
