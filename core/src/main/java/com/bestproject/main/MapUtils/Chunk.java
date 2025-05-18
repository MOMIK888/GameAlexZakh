package com.bestproject.main.MapUtils;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.SingleObjectMaps.SingleObjectMap;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.Tiles.Tile;

import javax.swing.text.Position;

public class Chunk implements Disposable {
   public  int xResolution;
    public int yResolution;
    public float width;
    public float thickness;
    public float height;
    SingleObjectMap singleObjectMap;
    TranslatableHitboxMap translatableHitboxMap;
    protected BoundingBox boundingBox;
    Vector3 min, max;
    public Chunk(int x, int y, Model model, String hitboxInfo){
        xResolution=x;
        yResolution=y;
        boundingBox=new BoundingBox();
        singleObjectMap=new SingleObjectMap(model);

        boundingBox=singleObjectMap.getModelInstance().calculateBoundingBox(boundingBox);
        min=new Vector3(boundingBox.min);
        max=new Vector3(boundingBox.max);
        width=boundingBox.getWidth();
        thickness= boundingBox.getDepth();
        height=boundingBox.getHeight();
        if(hitboxInfo!=null && hitboxInfo!="") {
            translatableHitboxMap = new TranslatableHitboxMap(StaticBuffer.decipherHitboxInfo(hitboxInfo));
        } else{
            translatableHitboxMap = new TranslatableHitboxMap(new Array<>());
        }
    }
    public BoundingBox getBounds(){
        return boundingBox;
    }
    public void initialize(){
    }
    @Override
    public void dispose() {
    }
    public void hb_interractions(MovingObject movingObject, int translationX, int translationY){
        translatableHitboxMap.translate(translationX*width,translationY*thickness);
        translatableHitboxMap.hitboxInterraction(movingObject);
    }
    public void translate(int TraslationX, int TranslationY){
        Vector3 pos=new Vector3(width*TraslationX,0,thickness*TranslationY);
        singleObjectMap.setPosition(pos);
        boundingBox.min.add(pos);
        boundingBox.max.add(pos);
    }

    public void resetTranslations(){
        singleObjectMap.setPosition(new Vector3());
        boundingBox.min.set(min);
        boundingBox.max.set(max);
    }
    public void render(ModelBatch modelBatch, Environment environment){
        if(modelBatch.getCamera().frustum.boundsInFrustum(boundingBox)) {
            singleObjectMap.render(modelBatch, environment);
        }

    }
    public void render(ModelBatch modelBatch){
        if(modelBatch.getCamera().frustum.boundsInFrustum(boundingBox)) {
            singleObjectMap.render(modelBatch);
        }

    }
    public static int[][] calculate_radius(int radius, int[] index_of_cell, int limit_x, int limit_y) {
        int x = index_of_cell[0];
        int y = index_of_cell[1];
        int min_x = Math.max(0, x - radius);
        int max_x = Math.min(limit_x - 1, x + radius);
        int min_y = Math.max(0, y - radius);
        int max_y = Math.min(limit_y - 1, y + radius);
        int num_cells_x = max_x - min_x + 1;
        int num_cells_y = max_y - min_y + 1;
        int total_cells = num_cells_x * num_cells_y;
        int[][] result = new int[total_cells][2];
        int index = 0;
        for (int i = min_x; i <= max_x; i++) {
            for (int j = min_y; j <= max_y; j++) {
                result[index][0] = i;
                result[index][1] = j;
                index++;
            }
        }
        return result;
    }
    public static int[][] calculate_radius(int radius, Vector3 pos,  float width, int limit_x, int limit_y) {
        int x = (int)Math.ceil((pos.x-width/2)/width);
        int y = (int)Math.ceil((pos.z-width/2)/width);
        int min_x = Math.max(0, x - radius);
        int max_x = Math.min(limit_x - 1, x + radius);
        int min_y = Math.max(0, y - radius);
        int max_y = Math.min(limit_y - 1, y + radius);
        int num_cells_x = max_x - min_x + 1;
        int num_cells_y = max_y - min_y + 1;
        int total_cells = num_cells_x * num_cells_y;
        int[][] result = new int[total_cells][2];
        int index = 0;
        for (int i = min_x; i <= max_x; i++) {
            for (int j = min_y; j <= max_y; j++) {
                result[index][0] = i;
                result[index][1] = j;
                index++;
            }
        }
        return result;
    }
}
