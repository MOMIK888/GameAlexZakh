package com.bestproject.main.MapUtils;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.Tiles.Tile;

import javax.swing.text.Position;

public class Chunk implements Disposable {
   public  int xResolution;
    public int yResolution;
    public Array<Array<Array<MovingObject>>> movingObjects; //disposed
    public Array<Array<Array<StaticObject>>> staticObjects; //disposed
    protected Array<Array<Tile>> Tiles; //do_not_dispose
    protected BoundingBox boundingBox;
    public Chunk(int x, int y){
        xResolution=x;
        yResolution=y;
        boundingBox=new BoundingBox();
        Vector3 min = new Vector3(0, 0, 0);
        Vector3 max = new Vector3(xResolution * 2f, 100f, yResolution * 2f);
        boundingBox.set(min, max);
    }
    public BoundingBox getBounds(){
        return boundingBox;
    }
    public void initialize(){
        movingObjects=new Array<>();
        staticObjects=new Array<>();
        Tiles=new Array<>(yResolution);
        for (int row = 0; row < yResolution; row++) {
            staticObjects.add(new Array<>(xResolution));
            movingObjects.add(new Array<>(xResolution));
            Tiles.add(new Array<>(xResolution));
            for (int col = 0; col < xResolution; col++) {
                staticObjects.get(row).add(new Array<StaticObject>());
                movingObjects.get(row).add(new Array<MovingObject>());
                Tiles.get(row).add(null);
            }
        }
    }
    @Override
    public void dispose() {
        for(int i=0; i<movingObjects.size; i++){
            for(int j=0; i<movingObjects.get(i).size;j++){
                for(int  m=0; i<movingObjects.get(i).get(j).size;m++){
                    movingObjects.get(i).get(j).get(m).dispose();
                }
            }
        }
        for(int i=0; i<staticObjects.size; i++){
            for(int j=0; i<staticObjects.get(i).size;j++){
                for(int  m=0; i<staticObjects.get(i).get(j).size;m++){
                    staticObjects.get(i).get(j).get(m).dispose();
                }
            }
        }
    }
    public void hb_interractions(MovingObject movingObject, int exclude){
        int rows=yResolution;
        int columns=xResolution;
        int[] norm=normalizeCoordinates(movingObject.getPosition(),rows, columns );
        int[][] indexes=calculate_radius(movingObject.getRadius(),new int[]{rows,columns},rows,columns);
        for(int[] i : indexes){
            if(Tiles.get(i[0]).get(i[1])!=null){
                movingObject.FLOOORHITBOXINTERRACTION(Tiles.get(i[0]).get(i[1]).gethb());
            }
            for(int j=0; j<movingObjects.get(i[0]).get(i[1]).size; j++){
                if(movingObjects.get(i[0]).get(i[1]).get(j).getUnique_index()!=exclude){
                    movingObject.HITBOXINTERRACTION(movingObjects.get(i[0]).get(i[1]).get(j).gethbs());
                    movingObjects.get(i[0]).get(i[1]).get(j).ATKHITBOXINTERRACTIONS(movingObject.getAtkHbs());


                }
            }
            for(int j=0; j<staticObjects.get(i[0]).get(i[1]).size; j++){
                movingObject.HITBOXINTERRACTION(staticObjects.get(i[0]).get(i[1]).get(j).gethbs());
                staticObjects.get(i[0]).get(i[1]).get(j).ATKHITBOXINTERRACTIONS(movingObject.getAtkHbs());
            }
        }
    }
    public void translate(int TraslationX, int TranslationY){
        float dx = 2 * xResolution * TraslationX;
        float dz = 2 * yResolution * TranslationY;
        for(int i=0; i<staticObjects.size; i++){
            for(int j=0; i<staticObjects.get(i).size;j++){
                for(int  m=0; i<staticObjects.get(i).get(j).size;m++){
                    Vector3 pos=staticObjects.get(i).get(j).get(m).getPosition();
                    staticObjects.get(i).get(j).get(m).setPosition(new Vector3(2f*xResolution*TraslationX+pos.x,pos.y,2f*yResolution*TranslationY+pos.z));
                }
            }
        }
        boundingBox.min.add(dx, 0, dz);
        boundingBox.max.add(dx, 0, dz);
    }

    public void ResetTranslations(int TraslationX, int TranslationY){
        float dx = 2 * xResolution * TraslationX;
        float dz = 2 * yResolution * TranslationY;
        for(int i=0; i<staticObjects.size; i++){
            for(int j=0; i<staticObjects.get(i).size;j++){
                for(int  m=0; i<staticObjects.get(i).get(j).size;m++){
                    Vector3 pos=staticObjects.get(i).get(j).get(m).getPosition();
                    staticObjects.get(i).get(j).get(m).setPosition(new Vector3(-2f*xResolution*TraslationX+pos.x,pos.y,-2f*yResolution*TranslationY+pos.z));
                }
            }
        }
        boundingBox.min.add(-dx, 0, -dz);
        boundingBox.max.add(-dx, 0, -dz);
    }
    public void render(ModelBatch modelBatch, Environment environment){
        for (Array<Array<StaticObject>> row : staticObjects) {
            for (Array<StaticObject> cell : row) {
                for (StaticObject obj : cell) {
                    obj.render(modelBatch,environment);
                }
            }
        }

        for (Array<Array<MovingObject>> row : movingObjects) {
            for (Array<MovingObject> cell : row) {
                for (MovingObject obj : cell) {
                    obj.render(modelBatch,environment);
                }
            }
        }

    }
    public void render(ModelBatch modelBatch){
        for (Array<Array<StaticObject>> row : staticObjects) {
            for (Array<StaticObject> cell : row) {
                for (StaticObject obj : cell) {
                    obj.render(modelBatch);
                }
            }
        }

        for (Array<Array<MovingObject>> row : movingObjects) {
            for (Array<MovingObject> cell : row) {
                for (MovingObject obj : cell) {
                    obj.render(modelBatch);
                }
            }
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
    private int[] normalizeCoordinates(Vector3 position, int rows, int columns){
        float norx=position.x%(xResolution*2);
        float norz=position.z%(yResolution*2);
        int normalizedy=(int)Math.min(rows-1,Math.max(0,(norz-1)/2));
        int normalizedx=(int) Math.min(columns-1,Math.max(0,(norx-1)/2));
        return new int[]{normalizedx,normalizedy};
    }
}
