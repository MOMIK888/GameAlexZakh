package com.bestproject.main.ObjectFragment;

public class ParryHitbox extends ATKHITBOX {
    boolean parry=true;
    public ParryHitbox(float x, float y, float z, float width, float thickness, float height, float invincib_frame, float[] damage, boolean isEnemyTarget) {
        super(x, y, z, width, thickness, height, invincib_frame, damage, isEnemyTarget);
    }
    @Override
    public int getType(){
        return 1; //Тип парирования
    }
    @Override
    public boolean isParry(){
        return parry;
    }
    public void setParry(boolean value){
        parry=value;
    }
}
