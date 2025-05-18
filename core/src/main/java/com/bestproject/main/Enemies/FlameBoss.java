package com.bestproject.main.Enemies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Ais.AI;
import com.bestproject.main.Ais.BlueSlimeAi;
import com.bestproject.main.Ais.BossAi;
import com.bestproject.main.Attacks.FlameRIng;
import com.bestproject.main.Attacks.Slash;
import com.bestproject.main.CameraUtils;
import com.bestproject.main.CostumeClasses.Healthbar;
import com.bestproject.main.CostumeClasses.SpriteSheetDecal;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class FlameBoss extends Enemy{
    boolean isattacking;
    HITBOX[] hitboxes;
    float hp=1000;
    float[] WarFrames=new float[]{0.3f,0.3f,0.3f};
    float warningFrames=0;
    AnimationController animationController;
    float[] getDir;
    float Grab=0f;
    float rotationSpeed=1000f;
    float rotation=90f;
    float maxFrames=4f;
    int attackType=0;
    float cnt=0;
    ATKHITBOX[] atkhitboxes=null;
    Vector3 realPosition=new Vector3();
    float TrailTime;
    BossAi ai=new BossAi();
    Vector3 target=new Vector3();
    Vector3 movement=new Vector3();
    Model FlameRing;
    Healthbar healthbar;
    SpriteSheetDecal spriteSheetDecal;
    public FlameBoss (Vector3 position) {
        super(new ModelInstance(StaticBuffer.current_enemies.get(0)), position);
        StaticBuffer.assetManager.load("Models/Effects/fireRing.g3dj", Model.class);
        StaticBuffer.assetManager.finishLoading();
        position.y+=0.65f;
        FlameRing=StaticBuffer.assetManager.get("Models/Effects/fireRing.g3dj");
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z, position.y, 0.25f,0.25f,1.3f)};
        setPosition(position);
        setPosition(position);
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("metarig|walk",-1);
        modelInstance.transform.scale(0.06f,0.06f,0.06f);
        modelInstance.transform.rotate(0f,1f,0f,180);
        speed=2f;
        realPosition.set(position);
        Texture hb=new Texture("Images/Ui/hp.png");
        healthbar=new Healthbar(new TextureRegion(hb),new TextureRegion(hb),0.3f,0.05f,hp, Color.GREEN,Color.GREEN);
        healthbar.setPosition(position.x,position.y+1f,position.z);
        spriteSheetDecal=new SpriteSheetDecal(new Texture("Images/Effect2d/fireSlashes.png"),4,4,0.1f);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(this.hitboxes);
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
                                StaticBuffer.ui.getCurrentMoveset().chargeUlt( atkhitboxes[i].getSummDamage()/3f);
                                float lasrstun=stunFrames;
                                setStunFrames(atkhitboxes[i].getStun());
                                atkhitboxes[i].invalidate(unique_index);
                                if(stunFrames>1f && 0>= lasrstun){
                                    animationController.setAnimation("metarig|knee fall",1);
                                }
                                if (hp <= 0) {

                                }
                            }
                        } else if(atkhitboxes[i].getType()==1){
                            if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                                hp -= (int) atkhitboxes[i].getSummDamage();
                                invinFrames = atkhitboxes[i].getFrames();
                                StaticBuffer.ui.getCurrentMoveset().chargeUlt( atkhitboxes[i].getSummDamage()/3f);
                                setStunFrames(atkhitboxes[i].getStun());
                                atkhitboxes[i].invalidate(unique_index);
                                if(atkhitboxes[i].isParry() && this.atkhitboxes!=null){
                                    this.atkhitboxes=null;
                                }
                                if (hp <= 0) {

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
        if(!isattacking) {
            Move();
            rotate();
        }
        attack();
        ai.update();
        ai.analyze(this);



    }
    public void setAttack(int value){
        isattacking=true;
        attackType=value;
        StaticBuffer.warning.setStateTime(0);
        StaticBuffer.warningRed.setStateTime(0);
        if(attackType==0){
            warningFrames=0.5f;
        } else if (attackType==1){
            warningFrames=0.5f;
        } else if(attackType==-1){
            warningFrames=0.5f;
        }else{
            isattacking=false;
        }
    }
    public void Grab(){
        if(Grab<=0){
            if(StaticQuickMAth.distance(StaticBuffer.getPlayerCooordinates(),position)<=0.8f){
                float angle=-StaticQuickMAth.getAngle(position,new Vector3(StaticBuffer.getPlayerCooordinates().x,position.y,StaticBuffer.getPlayerCooordinates().z))-rotation;
                if(Math.abs(angle)>80){
                    int chance=MathUtils.random(1);
                    if(chance==0){
                        StaticQuickMAth.setTimeFlow(0.3f,0.6f);
                        Grab=20f;
                    } else{
                        Grab=5f;
                    }
                }
            }
        } else if(Grab>0){
            Grab-=StaticQuickMAth.move(GameCore.deltatime);
        }
    }
    private void Move(){
        Vector3 unnormalized_movement;
        float[] sin_cos=StaticBuffer.getSin_Cos(position,StaticBuffer.getPlayerCooordinates());
        unnormalized_movement=new Vector3(StaticQuickMAth.move(sin_cos[1]*speed*GameEngine.getGameCore().deltatime), 0, StaticQuickMAth.move(sin_cos[0]*speed*GameEngine.getGameCore().deltatime));
        movement.set(unnormalized_movement.x,unnormalized_movement.y,unnormalized_movement.z);
        fractureMovement(movement);
    }
    private void attack(){
        //Атака по зоне
        if(attackType==0 && isattacking){
            if(warningFrames>0){
                warningFrames-=StaticQuickMAth.move(GameCore.deltatime);
                StaticBuffer.warningRed.update(StaticQuickMAth.move(GameCore.deltatime));
                StaticBuffer.warningRed.setPosition(position);
                StaticBuffer.warningRed.render();
                if(warningFrames<=0){
                    TrailTime=0.8f;
                }
            } else{
                if(TrailTime>=0){
                    TrailTime-=StaticQuickMAth.move(GameCore.deltatime);
                    StaticBuffer.circularWarn.setPosition(new Vector3(position).sub(new Vector3(0,0.5f,0)));
                    StaticBuffer.circularWarn.setScale(1,1);
                    StaticBuffer.circularWarn.setRotation(0);
                    StaticBuffer.circularWarn.setDimensions(7-(TrailTime*7*1.25f),7-(TrailTime*7*1.25f));
                    StaticBuffer.circularWarn.render(StaticBuffer.decalBatch);
                } else {
                    isattacking = false;
                    GameEngine.getGameCore().getMap().addMoving(new FlameRIng(FlameRing,new Vector3(position).sub(new Vector3(0,0.5f,0))));
                }
            }
        }
        //Разрез
        if(attackType==1 && isattacking) {
            if (warningFrames > 0) {
                warningFrames -= StaticQuickMAth.move(GameCore.deltatime);
                StaticBuffer.warning.update(StaticQuickMAth.move(GameCore.deltatime));
                StaticBuffer.warning.setPosition(position);
                StaticBuffer.warning.render();
            } else {
                isattacking = false;
                float angle= (float) Math.toRadians(StaticQuickMAth.getAngle(StaticBuffer.getPlayerCooordinates(),position));
                GameEngine.getGameCore().getMap().addMoving(new Slash(position,new float[]{(float) -Math.cos(angle),0, (float) -Math.sin(angle)},spriteSheetDecal));
            }
        }
    }
    private void rotate(){
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
    }
    @Override
    public boolean expire(){
        if(hp<=0){
            dispose();
            return true;
        }
        return false;

    }
    @Override
    public void update(){
        StaticBuffer.soundManager.update("1x1x1x1x",StaticBuffer.getPlayerCooordinates(),this.position);
        if(hp>0) {
            invinFrames -= StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime());
            if(stunFrames<=0){
                animationController.update(StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime()));
                cnt+=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
                ai();
            } else{

                stunFrames-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
                if(stunFrames<0.6f) {
                    animationController.update(StaticQuickMAth.move(-GameEngine.getGameCore().getDeltatime()));
                }   else{
                    animationController.update(StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime()));
                }
                if(stunFrames<=0){
                    animationController.setAnimation("metarig|walk",-1);
                }
            }

        }
        movement.set(0,0,0);
        healthbar.setPosition(this.position.x,this.position.y+1,this.position.z);
        healthbar.setHp(hp);
    }
    private void DomainExpansion(){

    }
    @Override
    public void fractureMovement(Vector3 unnormalized_movement){
        float max_value=Math.max(Math.max(Math.abs(unnormalized_movement.x),Math.abs(unnormalized_movement.y)),Math.abs(unnormalized_movement.z));
        int iterations=(int)Math.max((max_value/0.09f),1);
        Vector3 movement_fragment=new Vector3(unnormalized_movement.x/(float) iterations, unnormalized_movement.y/(float) iterations,unnormalized_movement.z/(float) iterations);
        for(int i=1; i<iterations; i++){
            movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
            GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
            setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));
        }
        movement.set(movement_fragment.x,movement_fragment.y,movement_fragment.z);
        GameEngine.getGameCore().getMap().ForcedHitboxInterraction(this);
        setPosition(new Vector3(movement.x+position.x,position.y+movement.y,movement.z+position.z));

    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        hitboxes[0].setX(position.x);
        hitboxes[0].setZ(position.y);
        hitboxes[0].setY(position.z);
    }
    @Override
    public void dispose(){
        FlameRing.dispose();
        spriteSheetDecal.dispose();
    }
}
