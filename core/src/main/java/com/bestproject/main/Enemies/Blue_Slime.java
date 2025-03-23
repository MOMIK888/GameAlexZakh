package com.bestproject.main.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Ais.BlueSlimeAi;
import com.bestproject.main.CameraUtils;
import com.bestproject.main.CostumeClasses.Healthbar;
import com.bestproject.main.Effects.Warn;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Blue_Slime extends Enemy {
    HITBOX[] hitboxes;
    float hp=100;
    AnimationController animationController;
    Healthbar healthbar;
    float[] getDir;
    float rotationSpeed=250f;
    float rotation=45f;
    float maxFrames=4f;
    float cnt=0;
    Vector3 RememberLoc=new Vector3();
    boolean movementType=false;
    float atkcounter=0;
    ATKHITBOX[] atkhitboxes=null;
    Vector3 realPosition=new Vector3();
    BlueSlimeAi ai=new BlueSlimeAi(4);
    Vector3 target=new Vector3();
    Vector2 movement=new Vector2();
    public Blue_Slime(Vector3 position) {
        super(new ModelInstance(StaticBuffer.current_enemies.get(0)), position);
        setPosition(position);
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z, position.y, 0.25,0.25,1)};
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("Armature|ArmatureAction",-1);
        modelInstance.transform.scale(0.3f,0.3f,0.3f);
        Texture hb=new Texture("Images/Ui/hp.png");
        healthbar=new Healthbar(new TextureRegion(hb),new TextureRegion(hb),0.3f,0.05f,hp, Color.GREEN,Color.GREEN);
        healthbar.setPosition(position.x,position.y+0.5f,position.z);
        speed=0.5f;
        realPosition.set(position);
    }

    @Override
    public HITBOX[] gethbs() {
        return null;
    }

    @Override
    public void ATKHITBOXINTERRACTIONS(ATKHITBOX[] atkhitboxes) {
        if(atkhitboxes!=null){
            for(int i=0; i<atkhitboxes.length; i++){
                if(atkhitboxes[i].getisEnemy()) {
                    if (this.hitboxes[0].colliderectangles(atkhitboxes[i])) {
                        if(atkhitboxes[i].getType()==0) {
                            if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                                hp -= (int) atkhitboxes[i].getSummDamage();
                                invinFrames = atkhitboxes[i].getFrames();
                                healthbar.setHp(hp);
                                setStunFrames(atkhitboxes[i].getStun());
                                atkhitboxes[i].invalidate(unique_index);
                                if (hp <= 0) {
                                    int angle = this.hitboxes[0].calculateAngle(atkhitboxes[i]);
                                    getDir = new float[]{(float) Math.cos(angle), (float) Math.sin(angle)};
                                }
                            }
                        } else if(atkhitboxes[i].getType()==1){
                            if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                                hp -= (int) atkhitboxes[i].getSummDamage();
                                invinFrames = atkhitboxes[i].getFrames();
                                healthbar.setHp(hp);
                                setStunFrames(atkhitboxes[i].getStun());
                                atkhitboxes[i].invalidate(unique_index);
                                if(atkhitboxes[i].isParry() && this.atkhitboxes!=null){
                                    this.atkhitboxes=null;
                                }
                                if (hp <= 0) {
                                    int angle = this.hitboxes[0].calculateAngle(atkhitboxes[i]);
                                    getDir = new float[]{(float) Math.cos(angle), (float) Math.sin(angle)};
                                }
                            }

                        }
                    }
                }
            }
        }

    }


    @Override
    public ATKHITBOX[] getAtkHbs() {
        return atkhitboxes;
    }

    public void ai(){
        Vector3 unnormalized_movement=new Vector3();
        if(cnt>=maxFrames){
            if(ai.check(cnt)){
                speed=4f;
                maxFrames=4f+ MathUtils.random(2f);
                movementType=true;
                atkcounter=0.8f;
                cnt=-0.8f;
                atkhitboxes=new ATKHITBOX[]{new ATKHITBOX(hitboxes[0].x,hitboxes[0].y,hitboxes[0].z,hitboxes[0].width,hitboxes[0].thickness,hitboxes[0].height,1f,new float[]{5,5,5},false)};
            }
        }
        if (cnt>3.57f+(maxFrames-4f) && cnt<3.83f+(maxFrames-4f)) {

            StaticBuffer.warning.update(StaticQuickMAth.move(GameEngine.getGameCore().deltatime));
            StaticBuffer.warning.setPosition(CameraUtils.pushTowardsCamera(GameCore.camera,new Vector3(position.x+GameEngine.getGameCore().deltatime*10,position.y+0.3f,position.z),-0.8f));
            RememberLoc=new Vector3().set(StaticBuffer.getPlayerCooordinates());
            float angle_warn=StaticQuickMAth.getAngle(position,RememberLoc)-90f;
            speed=0f;
            StaticBuffer.warn.setScale(0.002f,0.008f);
            StaticBuffer.warn.setRotation(angle_warn);
            StaticBuffer.warn.setPosition(new Vector3((float) (position.x+Math.cos(Math.toRadians(angle_warn+90))),position.y, (float) (position.z+Math.sin(Math.toRadians(angle_warn+90)))));
            StaticBuffer.warn.render(StaticBuffer.decalBatch);

        }

        if(movementType){
            float[] sin_cos=StaticBuffer.getSin_Cos(position,RememberLoc);
            atkcounter-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
            unnormalized_movement=new Vector3(StaticQuickMAth.move(sin_cos[1]*speed*GameEngine.getGameCore().deltatime), 0, StaticQuickMAth.move(sin_cos[0]*speed*GameEngine.getGameCore().deltatime));
            setPosition(new Vector3(position.x+movement.x, position.y, position.z+movement.y));
            speed-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime)*5;
            float angle_warn=StaticQuickMAth.getAngle(position,RememberLoc)-90f;
            StaticBuffer.warn.setScale(0.002f,0.008f*(speed/4f));
            StaticBuffer.warn.setRotation(angle_warn);
            StaticBuffer.warn.setPosition(new Vector3((float) (position.x+(speed/4f)*Math.cos(Math.toRadians(angle_warn+90))),position.y, (float) (position.z+(speed/4f)*Math.sin(Math.toRadians(angle_warn+90)))));
            StaticBuffer.warn.render(StaticBuffer.decalBatch);
            if(atkcounter<=0){
                movementType=false;
                atkhitboxes=null;
                speed=0.5f;
            } else if (StaticQuickMAth.distance(position,RememberLoc)<0.1f) {
                speed=0;
            }
        } else{
            float[] sin_cos=StaticBuffer.getSin_Cos(position,StaticBuffer.getPlayerCooordinates());
            unnormalized_movement=new Vector3(StaticQuickMAth.move(-sin_cos[1]*speed*GameEngine.getGameCore().deltatime), 0, -StaticQuickMAth.move(sin_cos[0]*speed*GameEngine.getGameCore().deltatime));
            setPosition(new Vector3(position.x+movement.x, position.y, position.z+movement.y));
        }
        movement.set(unnormalized_movement.x,unnormalized_movement.z);

        target.set(StaticBuffer.getPlayerCooordinates().x,position.y,StaticBuffer.getPlayerCooordinates().z);
        float angle=-StaticQuickMAth.getAngle(position,target)-rotation;
        if(Math.abs(angle)<5){
            return;
        }
        if(Math.abs(angle)>180){
            if(angle<0) {
                angle = 180 - Math.abs(angle);
            } else{
                angle = -180 + angle;
            }
        }
        int multiplier= (int) (angle/(rotationSpeed*StaticQuickMAth.move(GameEngine.getGameCore().deltatime)));
        float rotationangle=angle-multiplier*(rotationSpeed*StaticQuickMAth.move(GameEngine.getGameCore().deltatime));
        modelInstance.transform.rotate(0f,1f,0f,rotationangle);
        rotation+=rotationangle;
    }
    @Override
    public void render(){
        healthbar.render(StaticBuffer.decalBatch);
        if (cnt>3.57f+(maxFrames-4f) && cnt<3.83f+(maxFrames-4f)) {
            StaticBuffer.warning.render();
        }
    }
    @Override
    public boolean expire(){
        return hp<=0;
    }
    @Override
    public void update(){
        if(hp>0) {
            invinFrames -= StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime());
            animationController.update(StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime()));
            hitboxes[0].setX(position.x);
            hitboxes[0].setZ(position.y);
            hitboxes[0].setY(position.z);
            if(atkhitboxes!=null){
                atkhitboxes[0].setX(position.x);
                atkhitboxes[0].setZ(position.y);
                atkhitboxes[0].setY(position.z);
            }
            healthbar.setPosition(position.x,position.y+0.5f,position.z);
            if(stunFrames<=0){
                cnt+=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
                ai();
            } else{
                stunFrames-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
            }

        }
    }
}
