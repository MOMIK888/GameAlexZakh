package com.bestproject.main.Wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CostumeClasses.TextRenderer;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticQuickMAth;

public class StoneWallDoor extends StaticObject {
    HITBOX[] hitboxes;
    boolean isopened;
    float openingspeed=1f;
    AnimationController animationController;
    TextRenderer txt=null;
    float progress=0;
    boolean opening=false;
    int multiplier;

    public StoneWallDoor(Vector3 position, float rotation) {
        super(new ModelInstance(StaticBuffer.currentModels.get(5)), position);
        if (rotation%180==0) {
            hitboxes = new HITBOX[]{new HITBOX(position.x-0.76f, position.z, position.y, 0.5f, 0.3f, 5f),
                new HITBOX(position.x+0.76f, position.z, position.y, 0.5f, 0.3f, 5f),
                new HITBOX(position.x, position.z, position.y, 1f, 0.3f, 5f)};
        }else {
            hitboxes = new HITBOX[]{new HITBOX(position.x, position.z-0.76f, position.y, 0.3f, 0.5f, 5f),
                new HITBOX(position.x, position.z+0.76f, position.y, 0.3f, 0.5f, 5f),
                new HITBOX(position.x, position.z, position.y, 0.3f, 1f, 5f)};
        }
        modelInstance.transform.rotate(0f,1f,0f,rotation+270);
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("Armature|ArmatureAction",-1);
        animationController.update(-0.367f);
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        hitboxes[0].setX(position.x);
        hitboxes[0].setY(position.z);
        hitboxes[0].setZ(position.y);
        hitboxes[1].setX(position.x);
        hitboxes[1].setY(position.z);
        hitboxes[1].setZ(position.y);
        hitboxes[2].setX(position.x);
        hitboxes[2].setY(position.z);
        hitboxes[2].setZ(position.y);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        super.render(modelBatch, environment);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(this.hitboxes);
    }
    @Override
    public void update(){
        if(opening){
            float openingvalue=GameEngine.getGameCore().deltatime*openingspeed;
            if(progress+openingvalue>0.36f){
                openingvalue=0.36f-progress;
                isopened=true;
                opening=false;
            } else if(progress+openingvalue<0f) {
                openingvalue=-progress;
                isopened=false;
                opening=false;
            }
            progress+=openingvalue;
            animationController.update(openingvalue);
        }

    }

    @Override
    public void ATKHITBOXINTERRACTIONS(ATKHITBOX[] atkhitboxes) {
        if(atkhitboxes!=null){
            if(atkhitboxes[0].isDoorOpening() && !isopened){
                for(int i=0; i<atkhitboxes.length; i++){
                    if( atkhitboxes[i].colliderectangles(hitboxes[2])){
                        isopened=true;
                        opening=true;
                        openingspeed=4f;
                    }
                }
            }
        }
    }

    @Override
    public HITBOX[] gethbs() {
        if(isopened){
            return new HITBOX[]{hitboxes[0], hitboxes[1]};
        }
        return hitboxes;
    }
}
