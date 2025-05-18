package com.bestproject.main.MapUtils;

import com.badlogic.gdx.utils.Array;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.ObjectFragment.HITBOX;


import com.badlogic.gdx.math.Vector3;

public class HITBOXMAP {
    public Array<HITBOX> hitboxArray;
    public Array<Array<Array<Integer>>> hitboxMap=new Array<>();
    public float tileSize = 2f;
    int xRes, yRes;
    float minX,minY,maxX,maxY;
    public HITBOXMAP(Array<HITBOX> hitboxArray) {
        minY=10000;
        minX=10000;
        maxY=-10000;
        maxX=-100000;
        this.hitboxArray = hitboxArray;
        compile();
    }

    private void compile() {
        if (hitboxArray.size == 0) return;
        for(int i=0; i<hitboxArray.size; i++){
            if(hitboxArray.get(i).x-hitboxArray.get(i).width/2<minX){
                minX= (float) (hitboxArray.get(i).x-hitboxArray.get(i).width/2);
            }
            if(hitboxArray.get(i).x+hitboxArray.get(i).width/2>maxX){
                maxX= (float) (hitboxArray.get(i).x+hitboxArray.get(i).width/2);
            }
            if(hitboxArray.get(i).y+hitboxArray.get(i).thickness/2>maxY){
                maxY=(float) (hitboxArray.get(i).y+hitboxArray.get(i).thickness/2);
            }
            if(hitboxArray.get(i).y-hitboxArray.get(i).thickness/2<minY){
                minY=(float) (hitboxArray.get(i).y-hitboxArray.get(i).thickness/2);
            }
        }
        yRes=(int) ((maxX-minX)/tileSize)+1;
        xRes=(int) ((maxY-minY)/tileSize)+1;
        for (int x = 0; x < yRes; x++) {
            Array<Array<Integer>> col = new Array<>();
            for (int y = 0; y < xRes; y++) {
                col.add(new Array<>());
            }
            hitboxMap.add(col);
        }
        for (int i = 0; i < hitboxArray.size; i++) {
            HITBOX hb = hitboxArray.get(i);
            float hbMinX = (float) (hb.x - hb.width / 2);
            float hbMaxX = (float) (hb.x + hb.width / 2);
            float hbMinY = (float) (hb.y - hb.thickness / 2);
            float hbMaxY = (float) (hb.y + hb.thickness / 2);
            int startX = (int) Math.floor((hbMinX - minX) / tileSize);
            int endX = (int) Math.floor((hbMaxX - minX) / tileSize);
            int startY = (int) Math.floor((hbMinY - minY) / tileSize);
            int endY = (int) Math.floor((hbMaxY - minY) / tileSize);
            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    if (inBounds(x, y)) {
                        hitboxMap.get(x).get(y).add(i);
                    }
                }
            }
        }

    }

    protected boolean inBounds(int x, int y) {
        return x >= 0 && x < yRes && y >= 0 && y < xRes;
    }

    public Array<Integer> analyze_position(Vector3 pos) {
        int x = (int) Math.floor((pos.x - minX) / tileSize);
        int y = (int) Math.floor((pos.z - minY) / tileSize);
        if (inBounds(x, y)) {
            return hitboxMap.get(x).get(y);

        }
        return new Array<>();
    }
    public void hitboxInterraction(MovingObject movingObject){
        Array<Integer> array=analyze_position(movingObject.getPosition());

        for(int i=0; i<array.size; i++){
            movingObject.HITBOXINTERRACTION(new HITBOX[]{hitboxArray.get(array.get(i))});
        }


    }
    public void translate(float x, float y){

    }
}
