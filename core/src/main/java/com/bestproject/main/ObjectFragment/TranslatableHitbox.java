package com.bestproject.main.ObjectFragment;

public class TranslatableHitbox extends HITBOX{
    float originalX, originalY, originalZ;
    public TranslatableHitbox(float x, float y, float z, float width, float thickness, float height) {
        super(x, y, z, width, thickness, height);
        originalX=x;
        originalY=y;
        originalZ=z;
    }
    public TranslatableHitbox(HITBOX hitbox) {
        super(hitbox.x,hitbox.y,hitbox.z,hitbox.width,hitbox.thickness,hitbox.height);
        originalX=hitbox.x;
        originalY=hitbox.y;
        originalZ=hitbox.z;
    }
    public void translate(float x, float y, float z){
        this.x=x+this.x;
        this.y=y+this.y;
        this.z=z+this.z;

    }
    public void resetTranslation(){
        this.x=originalX;
        this.y=originalY;
        this.z=originalZ;
    }

}
