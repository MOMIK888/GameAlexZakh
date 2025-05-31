package com.bestproject.main.MapUtils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class HeightMap {
    float maxheight;
    float minheight;
    float minX, maxX, minY, maxY, width, height, MulXres, MulYres;
    Pixmap pixmap;
    int pixmapX, pixmapY;
    public HeightMap(float maxheight, float minheight, float minX, float maxX, float minY, float maxY, float width, float height, float mulXres, float mulYres, Pixmap pixmap) {
        this.maxheight = maxheight;
        this.minheight = minheight;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
        MulXres = mulXres;
        MulYres = mulYres;
        this.pixmap = pixmap;
        pixmapX=pixmap.getWidth();
        pixmapY=pixmap.getHeight();
    }
    public Pixmap getPixmap(){
        return pixmap;
    }
    public float getHeight(float x, float z){
        float localX = (x - minX) * MulXres;
        float localY = (z - minY) * MulYres;
        int ix = MathUtils.clamp((int) localX, 0, pixmapX - 1);
        int iy = MathUtils.clamp((int) localY, 0, pixmapY - 1);
        int pixel = pixmap.getPixel(ix, iy);
        pixel=(pixel>>24) & 0xff;
        float normalized = (float)(pixel)/32;
        return (normalized * maxheight) ;
    }

}
