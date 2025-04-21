package com.bestproject.main.MovingObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.ATKHITBOX;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class Player extends MovingObject{
    public float speed=1.4f;
    public float invinFrames=0f;
    public boolean[] blockers=new boolean[]{false,false,false,false};
    public Vector3 lastdir=new Vector3();
    public float lastangle=0;
    public HITBOX[] hitboxes;
    public int current_state=0;
    public AnimationController animationController;
    public Vector3 unnormalizedMovement=new Vector3();
    public boolean isGravityAffected=true;
    HITBOX slideHitBox;
    boolean[] climbing=new boolean[]{false,false};
    float jumped=0f;
    public float gravity_multip=1f;
    public Vector3 force=new Vector3();
    boolean isSliding=false;
    public Vector3 offset=new Vector3();


    public ShaderProgram getShaderProgram(){
        return null;
    }

    public Player(Vector3 position) {
        super(new ModelInstance(StaticBuffer.ui.getCurrentMoveset().getCharacterModel()), position);
        modelInstance.transform.setToRotation(0,1,0,90);
        modelInstance.transform.scale(0.3f,0.3f,0.3f);
        hitboxes=new HITBOX[]{new HITBOX(position.x, position.z, position.y, 0.25,0.25,0.05f)};
        slideHitBox=new HITBOX(position.x, position.z, position.y, 0.45,0.45,1.1f);
        animationController=new AnimationController(modelInstance);
        animationController.setAnimation("metarig|idle",-1);
        setPosition(position);
        islIneArt=true;
        StaticBuffer.setPlayer_coordinates(new int[]{(int) (position.z-1)/2,(int) (position.x-1)/2});


    }
    public static void disposeAll() {

    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(modelInstance,environment);
        if(isSliding){
            StaticBuffer.dust.setPosition(position);
            StaticBuffer.dust.render();
            StaticBuffer.dust.update(GameCore.deltatime);
        }
    }
    @Override
    public void render(ModelBatch modelBatch){
        modelBatch.render(modelInstance);
    }
    @Override
    public void RenderHitboxes(){
        StaticBuffer.showHitbox.render(this.hitboxes);
    }
    @Override
    public void update(){
        isSliding=false;
        StaticBuffer.ui.getCurrentMoveset().forceNullification(this);
        float glevel=GameEngine.getGameCore().getMap().GetGroundLevel(new Vector3((float) hitboxes[0].x, (float) hitboxes[0].z, (float) hitboxes[0].y));
        if(isGravityAffected && hitboxes[0].getBottom()>glevel){
            gravity_multip=1f;
        } else{
            gravity_multip=1f;
        }
        if(true){
            Move();
        } else{
             Climb();
        }
        invinFrames-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
        GameEngine.getGameCore().setCameraCoordinatesPlayer(position.x+1,position.y+1,position.z);
        StaticBuffer.setPlayer_coordinates(new int[]{(int) (position.z-1)/2,(int) (position.x-1)/2});
        blockers[0]=false;
        blockers[1]=false;
        blockers[2]=false;
        blockers[3]=false;
        climbing[0]=false;
        climbing[1]=false;
        isGravityAffected=true;
        if(jumped>0){
            jumped-=StaticQuickMAth.move(GameCore.deltatime);
        }
    }
    @Override
    public void ATKHITBOXINTERRACTIONS(ATKHITBOX[] atkhitboxes) {
        if(atkhitboxes!=null){
            for(int i=0; i<atkhitboxes.length; i++){
                if(!atkhitboxes[i].getisEnemy()) {
                    if (this.hitboxes[0].colliderectangles(atkhitboxes[i])) {
                        if (invinFrames * atkhitboxes[i].invincibility_resetter(unique_index) <= 0f) {
                            StaticBuffer.ui.setCurrentMovesetHp(StaticBuffer.ui.getCurrentMovesetHp()-atkhitboxes[i].getSummDamage());
                            invinFrames = atkhitboxes[i].getFrames();
                            atkhitboxes[i].invalidate(unique_index);

                        }
                    }
                }
            }
        }
    }
    private void Climb(){
        float deltatime=GameEngine.gameCore.getDeltatime();
        Vector2 vec=GameEngine.getGameCore().getJoystick().getDirection();
        unnormalizedMovement = new Vector3(vec.x,0,vec.y);
        if(!movement.isZero()){
            float multip=climbing[1] ? 1:0;
            setPosition(new Vector3((multip)*StaticQuickMAth.move(-movement.z*deltatime*speed)+position.x,
                StaticQuickMAth.move(-movement.x*deltatime*speed)+position.y,
                (1-multip)*StaticQuickMAth.move(-movement.z*deltatime*speed)+position.z));
        }
        movement.set(unnormalizedMovement.x,0,unnormalizedMovement.z);
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
    public void HITBOXINTERRACTION(HITBOX[] hitboxes) {
        if(hitboxes!=null){
            for(int i=0; i<hitboxes.length; i++) {
                if(StaticBuffer.ui.getState()!=5) {
                    if (hitboxes[i].geType() == HITBOX.HITBOXTYPES.FLOOR.getValue()) {
                        if (hitboxes[i].getTopBottomMargin(this.hitboxes[0])) {
                           movement.y=0;
                           force.y =0;
                            isGravityAffected = false;

                        }
                    } else if (hitboxes[i].geType() == HITBOX.HITBOXTYPES.CLIMBABLE.getValue()) {
                        if (hitboxes[i].getTopBottomMargin(this.hitboxes[0])) {
                            movement.y=0;
                            force.y =0;
                        } else {
                            boolean[] tempclimbing = hitboxes[i].blockMovementPlus(position, new Vector2(movement.x, movement.z), this.hitboxes[0]);
                            if (tempclimbing[0]) {
                                climbing = tempclimbing;
                                movement.y=0;
                                force.y =0;
                            }
                        }
                    } else {
                        if(hitboxes[i].colliderectangles(this.hitboxes[0])){
                            if(hitboxes[i].getBottomTopOverlap(this.hitboxes[0])){
                                movement.y=0;
                                if(force.y<0){
                                    force.y=0;
                                }
                                return;
                            }
                            hitboxes[i].blockMovementAfter(position,this.movement,this.hitboxes[0],force);
                        }

                    }
                } else if(StaticBuffer.ui.getState()==5){
                    if (hitboxes[i].geType() != HITBOX.HITBOXTYPES.ENEMY_OR_UNCLIMBABLE.getValue()) {
                        float glevel=GameEngine.getGameCore().getMap().GetGroundLevel(new Vector3((float) this.hitboxes[0].x, (float) this.hitboxes[0].z, (float) this.hitboxes[0].y));
                        if(jumped<=0){
                            int[] newvalues=hitboxes[i].blockMovementSlide(position,movement,this.hitboxes[0],force);
                            if(newvalues[0]!=0 || hitboxes[0].getBottom()<=glevel){
                                force.y =0.05f;
                                jumped=1f;
                            } else if(newvalues[1]!=0){
                                movement.x=movement.x+movement.z;
                                movement.z=0;
                                gravity_multip=0.1f;
                                isSliding=true;
                                if(force.y<-0.02){
                                    force.y=0.02f;
                                }
                            } else if(newvalues[2]!=0){
                                movement.z=movement.x+movement.z;
                                movement.x=0;
                                gravity_multip=0.1f;
                                isSliding=true;
                                if(force.y<-0.02){
                                    force.y=-0.02f;
                                }
                            }
                        } else{
                            if(hitboxes[i].colliderectangles(this.hitboxes[0])){
                                if(hitboxes[i].getBottomTopOverlap(this.hitboxes[0])){
                                    movement.y=0;
                                    if(force.y<0){
                                        force.y=0;
                                    }
                                    return;
                                }
                                hitboxes[i].blockMovementAfter(position,this.movement,this.hitboxes[0],force);
                            }
                        }
                    }else{
                        if(hitboxes[i].colliderectangles(this.hitboxes[0])){
                            if(hitboxes[i].getBottomTopOverlap(this.hitboxes[0])){
                                movement.y=0;
                                if(force.y<0){
                                    force.y=0;
                                }
                                return;
                            }
                            hitboxes[i].blockMovementAfter(position,this.movement,this.hitboxes[0],force);
                        }
                    }
                }
            }
        }
    }
    @Override
    public int getType(){
        return -1;
    }

    public void Move() {
        StaticBuffer.ui.getCurrentMoveset().PlayerInterrractions(this);

    }
    public void setForce(Vector3 force){
        this.force=force;
    }
    public void addForce(Vector3 force){
        this.force.add(force);
    }

    @Override
    public void setPosition(Vector3 position) {
        Vector3 laspos=new Vector3(this.position);
        this.position.set(position);
        if(modelInstance!=null){
            modelInstance.transform.setTranslation(position);
        } else{
            System.out.println("NULLMODEL");
        }
        StaticBuffer.setPlayerCooordinates(this.position);
        StaticBuffer.setPlayer_coordinates(new int[]{(int) (position.z-1)/2,(int) (position.x-1)/2});
        hitboxes[0].setX(position.x);
        hitboxes[0].setY(position.z);
        hitboxes[0].setZ(position.y-offset.y);
        slideHitBox.setCoordinatesFromHITBOX(hitboxes[0]);
        GameCore.setCameraCoordinatesPlayer(this.position.x,this.position.y,this.position.z-0.5f);
        if(GameEngine.getGameCore()!=null){
            if(GameEngine.getGameCore().getMap()!=null){
                GameEngine.getGameCore().getMap().ForcedMovingRearr(laspos);
            }
        }
        offset.setZero();
    }

    @Override
    public void dispose() {
        disposeAll();
    }
}
