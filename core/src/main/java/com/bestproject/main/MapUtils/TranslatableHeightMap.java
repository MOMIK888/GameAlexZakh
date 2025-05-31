package com.bestproject.main.MapUtils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

public class TranslatableHeightMap extends HeightMap{
    float translationX, translationY;
    public TranslatableHeightMap(float maxheight, float minheight, float minX, float maxX, float minY, float maxY, float width, float height, float mulXres, float mulYres, Pixmap pixmap) {
        super(maxheight, minheight, minX, maxX, minY, maxY, width, height, mulXres, mulYres, pixmap);
    }
    public void setTranslation(float x, float y){
        translationX=x;
        translationY=y;
    }
    public void resetTranslation(){
        translationY=0;
        translationX=0;
    }
    public float getHeight(float x, float z){
        float localX = (x - minX-translationX) * MulXres;
        float localY = (z - minY-translationY) * MulYres;
        int ix = MathUtils.clamp((int) localX, 0, pixmapX - 1);
        int iy = MathUtils.clamp((int) localY, 0, pixmapY - 1);
        int pixel = pixmap.getPixel(ix, iy);
        pixel=(pixel>>24) & 0xff;
        float normalized = (float)(pixel)/32;
        return (normalized * maxheight) ;
    }

}
