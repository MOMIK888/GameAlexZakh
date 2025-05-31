package com.bestproject.main.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Ais.AI;
import com.bestproject.main.Ais.MechAI;
import com.bestproject.main.Attacks.FlameRIng;
import com.bestproject.main.Attacks.MechBullet;
import com.bestproject.main.Attacks.Slash;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MapRender.HitboxAndMappingUtils;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Mech extends Enemy{
    MechBullet[] bulletBatch;
    Texture bulletTexture;
    AnimationController[]  controllers;
    HITBOX[] hitboxes=new HITBOX[]{new HITBOX(0,0,0,3,3,3)};
    Vector3 target=new Vector3();
    boolean membran=false;
    float currentmembranTime=0;
    float rotation;
    float cnt;
    boolean vulnerable;
    HITBOX vulnerableHitbox=new HITBOX(0,0,0,1,1,4);
    float rotationSpeed=240f;
    boolean isShooting=false;
    float shootingLen=0;
    float hp=1f;
    boolean isattacking=false;
    int attackType;
    float warningFrames;
    MechAI ai=new MechAI();
    float atkLengh=1f;
    float ShootLen=1f;

    public Mech(Vector3 position) {
        super(new ModelInstance(StaticBuffer.current_enemies.get(0)), position.add(0f,0.8f,0f));
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        controllers=new AnimationController[]{new AnimationController(modelInstance),new AnimationController(modelInstance)};
        bulletTexture=new Texture(pixmap);
        modelInstance.transform.scale(0.5f,0.5f,0.5f);
        modelInstance.transform.rotate(0f,1f,0f,-90);
        controllers[0].setAnimation("Armature.001|walk",-1);
        controllers[1].setAnimation("Armature.001|gunSpinning",-1);

        pixmap.dispose();
        bulletBatch=new MechBullet[20];
        for(int i=0; i<bulletBatch.length; i++){
            bulletBatch[i]=new MechBullet(new Vector3(), new float[]{0,0,0},new TextureRegion(bulletTexture));
        }
    }
    private void Shoot(){
        int iterations= (int) MathUtils.clamp((1-shootingLen)*20,0,19);
        for(int i=0; i<iterations; i++){
            if(bulletBatch[i].lengh<0){
                int randomMul=MathUtils.random(0,1);
                if(randomMul==0){
                    randomMul=-1;
                }
                Vector3 pos=new Vector3(this.position).add((float) (Math.cos(rotation)*2)*randomMul,0, (float) (Math.sin(rotation)*2)*randomMul);
                bulletBatch[i].reset(pos,new Vector3(StaticBuffer.getPlayerCooordinates()).sub(pos).nor(),new TextureRegion(bulletTexture));
                GameEngine.getGameCore().getMap().addMoving(bulletBatch[i]);
            }
        }

    }
    private void RocketBlast(){

    }
    @Override
    public void ATKHITBOXINTERRACTIONS(ATKHITBOX[] atkhitboxes) {
        if (atkhitboxes != null) {
            for (int i = 0; i < atkhitboxes.length; i++) {
                if (atkhitboxes[i].getisEnemy()) {
                    if(vulnerable && vulnerableHitbox.colliderectangles(atkhitboxes[i])){
                        if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                            float damage = atkhitboxes[i].getSummDamage() * 1.5f;
                            hp -= (int) damage;

                            StaticBuffer.damageRenderer.showDamage(new Vector3(this.position).lerp(StaticBuffer.getPlayerCooordinates(), 0.6f), damage, crit);
                            invinFrames = atkhitboxes[i].getFrames();
                            StaticBuffer.ui.getCurrentMoveset().chargeUlt(damage / 3f);
                            float lasrstun = stunFrames;
                            setStunFrames(atkhitboxes[i].getStun());
                            atkhitboxes[i].invalidate(unique_index);
                            if (hp <= 0) {

                            }
                        }
                    } else if (!vulnerable && this.hitboxes[0].colliderectangles(atkhitboxes[i])) {
                        if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                            hp -= (int) atkhitboxes[i].getSummDamage();
                           StaticBuffer.damageRenderer.showDamage(new Vector3(this.position).lerp(StaticBuffer.getPlayerCooordinates(),0.6f),atkhitboxes[i].getSummDamage(),base);
                            invinFrames = atkhitboxes[i].getFrames();
                            StaticBuffer.ui.getCurrentMoveset().chargeUlt(atkhitboxes[i].getSummDamage() / 3f);
                            float lasrstun = stunFrames;
                            if(!isattacking) {
                                setStunFrames(atkhitboxes[i].getStun());
                            }
                            atkhitboxes[i].invalidate(unique_index);
                            if (hp <= 0) {

                            }

                        }
                    }
                }
            }

        }
    }

    @Override
    public HITBOX[] gethbs() {
        if(!vulnerable) {
            return hitboxes;
        }
        return null;
    }

    public void ai(){
        rotate();
        if(!isattacking) {
            Move();
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
            atkLengh=6f;
            shootingLen=1f;
        } else if (attackType==1){
            warningFrames=0.5f;
        } else if(attackType==-1){
            warningFrames=0.5f;
        }else{
            isattacking=false;
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
        if(isattacking){
            if(attackType==0){
                Shoot();
            }
        }
        if(atkLengh<0){
            isattacking=false;
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

    }
    @Override
    public boolean expire(){
        if(hp<=0){
            dispose();
            GameEngine.gameCore.setTemporaryMapBuffer(1);
            return true;
        }
        return false;

    }
    @Override
    public void update(){
        float delta=StaticQuickMAth.move(GameEngine.getGameCore().getDeltatime());
        shootingLen-=delta;
        if(hp>0) {
            invinFrames -= delta;
            atkLengh-=delta;
            if(stunFrames<=0){
                cnt+=delta;
                controllers[0].update(delta);
                vulnerable=false;
                controllers[1].update(delta);
                ai();
            } else{
                vulnerable=true;
                if(!membran) {
                    controllers[1].setAnimation("Armature.001|MembranOut", 1);
                    membran=true;
                }
                if(currentmembranTime+delta<0.336f) {
                    controllers[1].update(delta);
                    currentmembranTime += delta;
                }
                stunFrames-=delta;
                if(stunFrames<=0){
                    membran=false;
                    controllers[1].update(0.08f);
                    currentmembranTime=0;
                    controllers[1].setAnimation("Armature.001|gunSpinning",-1);
                }

            }

        }
        movement.set(0,0,0);

    }
    private void DomainExpansion(){

    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment) {
        super.render(modelBatch, environment);
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
        vulnerableHitbox.setX(position.x);
        vulnerableHitbox.setZ(position.y+1);
        vulnerableHitbox.setY(position.z);

    }

}
