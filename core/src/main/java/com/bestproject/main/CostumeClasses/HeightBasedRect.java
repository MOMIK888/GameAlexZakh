package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class HeightBasedRect{
    Rectangle rectangle;
    float height;
    float y;
    public HeightBasedRect(Rectangle rectangle, float height, float y){
        this.rectangle=rectangle;
        this.height=height;
        this.y=y;
    }
    public Rectangle getRectangle(){
        return rectangle;
    }
    public boolean contains(Vector3 pos){
        if(pos.y> y+height/2 || pos.y<y-height/2){
            return  false;
        } else{
            return rectangle.contains(pos.x,pos.z);
        }
    }
}
