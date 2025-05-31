package com.bestproject.main.MapRender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.MapUtils.HeightMap;
import com.bestproject.main.ObjectFragment.HITBOX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HitboxAndMappingUtils {
    public static HeightMap makeHitboxHeightMap(Array<HITBOX> hitboxArray, int res){
        float minX=10000;
        float maxX=-10000;
        float minY=10000;
        float maxY=-10000;
        float minHeight=0;
        float maxHeight=-1000;
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
            if(hitboxArray.get(i).z+hitboxArray.get(i).height/2>maxHeight){
                maxHeight=hitboxArray.get(i).z+hitboxArray.get(i).height/2;
            }
        }
        float width=maxX-minX;
        float height=maxY-minY;
        Pixmap pixmap=new Pixmap((int)((float)res*(width/height)),(int)((float)res*(height/width)), Pixmap.Format.RGBA8888);
        float pixmapW, pixmapH, resMulX, resMulY;
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        pixmapW=pixmap.getWidth();
        pixmapH=pixmap.getHeight();
        resMulX=(float) (float)pixmapW/width;
        resMulY=(float) (float)pixmapH/height;

        Map<String, Float> map = new HashMap<>();
        for(int i=0; i<hitboxArray.size; i++){
            map.put(String.valueOf(i),hitboxArray.get(i).z+hitboxArray.get(i).height/2);
        }
        Map<String, Float> sortedMap = map.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        Iterator<Map.Entry<String, Float>> iterator = sortedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Float> entry = iterator.next();
            drawHeight(hitboxArray.get(Integer.valueOf(entry.getKey())),pixmap,minX,minY,resMulX,resMulY, entry.getValue()/height);
        }
        return new HeightMap(maxHeight,minHeight,minX,maxX,minY,maxY,width,height,resMulX,resMulY,pixmap);
    }
    private static void drawHeight(HITBOX hitbox, Pixmap pixmap, float minX, float minY, float resMulX, float resMulY, float height){
        float y=hitbox.y-minY;
        float x=hitbox.x-minX;
        y=y*resMulY;
        x=x*resMulX;
        float width=hitbox.width*resMulX;
        float thickness=hitbox.thickness*resMulY;
        int left= (int) (x-width/2);
        int right= (int) (x+width/2);
        int top=(int) (y+thickness/2);
        int bottom=(int) (y-thickness/2);
        int rectWidth = right - left;
        int rectHeight = top - bottom;
        System.out.println(height);
        pixmap.setColor(height, height, height, 1f);
        pixmap.fillRectangle(left, bottom, rectWidth, rectHeight);
    }
}
