package com.bestproject.main.ObjectFragment;

import java.util.HashSet;
import java.util.Set;

public class ATKHITBOX extends HITBOX{
    float[] damage;
    float frames;
    float stunframes;
    boolean isEnemy;
    private Set<Integer> invalidated_indexes;
    boolean isDoorOpening;
    public ATKHITBOX(double x, double y, double z, double width, double thickness, double height, float invincib_frame, float[] damage, boolean isEnemyTarget) {
        super(x, y, z, width, thickness, height);
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

    protected boolean contains(int index){
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
    public void invalidate(int index){
        invalidated_indexes.add(index);
    }
    public float[] getDamage(){
        return damage;
    }

}
