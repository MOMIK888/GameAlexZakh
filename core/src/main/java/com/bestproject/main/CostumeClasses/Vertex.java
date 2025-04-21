package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.math.Vector3;

public class Vertex {
    Vector3 position;
    public Vertex(Vector3 pos){
        this.position=new Vector3(pos.x,pos.y,pos.z);
    }
    public Vertex(float x, float y, float z){
        this.position=new Vector3(x,y,z);
    }
    public Vector3 getPosition(){
        return position;
    }
}
