package com.bestproject.main.ObjectFragment;

import com.badlogic.gdx.math.Vector3;

import java.util.HashSet;
import java.util.Set;

public class ATKHITBOX extends HITBOX{
    float[] damage;
    float frames;
    float force=0;
    float stunframes;
    float crit_multiplier=1f;
    boolean isEnemy;
    private Set<Integer> invalidated_indexes;
    boolean isDoorOpening;
    public ATKHITBOX(float x, float y, float z, float width, float thickness, float height, float invincib_frame, float[] damage, boolean isEnemyTarget) {
        super(x, y, z, width, thickness, height);
        frames=invincib_frame;
        this.damage=damage;
        this.isEnemy=isEnemyTarget;
        invalidated_indexes = new HashSet<>();
    }
    public ATKHITBOX(float x, float y, float z, float boxres, float invincib_frame, float[] damage, boolean isEnemyTarget) {
        super(x, y, z, boxres, boxres, boxres);
        frames=invincib_frame;
        this.damage=damage;
        this.isEnemy=isEnemyTarget;
        invalidated_indexes = new HashSet<>();
    }
    public void setForce(float force){
        this.force=force;
    }
    public void setCrit_multiplier(float value){
        crit_multiplier=value;
    }
    public ATKHITBOX(Vector3 position, float boxres, float invincib_frame, float[] damage, boolean isEnemyTarget) {
        super(position.x, position.z, position.y, boxres, boxres, boxres);
        frames=invincib_frame;
        this.damage=damage;
        this.isEnemy=isEnemyTarget;
        invalidated_indexes = new HashSet<>();
    }
    public int getType(){
        return 0;
    }
    public boolean isParry(){
        return false;
    }
    public boolean getIsCol(){
        return !invalidated_indexes.isEmpty();
    }
    public boolean isDoorOpening(){
        return isDoorOpening;
    }
    public void setStunframes(float stunframes){
        this.stunframes=stunframes;
    }
    public float getStun(){
        return stunframes;
    }
    public void setDoorOpening(boolean val){
        isDoorOpening=val;
    }
    public boolean getisEnemy(){
        return isEnemy;
    }
    public boolean getisPlayer(){
        return !isEnemy;
    }
    public float getFrames(){
        return frames;
    }
    public float getSummDamage(){
        float sum=0;
        for(float i: damage){
            sum+=i;
        }
        return sum;
    }

    public boolean contains(int index){
        for(int i: invalidated_indexes){
            if(i==index){
                return false;
            }
        }
        return true;
    }
    public int invincibility_resetter(int index){
        if(!contains(index)){
            return 1;
        }
        return 0;
    }
    public float getForce(){
        return force;
    }
    public void invalidate(int index){
        if(contains(index)) {
            invalidated_indexes.add(index);
        }
    }
    public float[] getDamage(){
        return damage;
    }

}
