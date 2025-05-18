package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.Enemies.Blue_Slime;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.Barrel;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticObjects.TestCollision;
import com.bestproject.main.Tiles.StoneTile;
import com.bestproject.main.Tiles.Tile;
import com.bestproject.main.Wall.StoneWall;

import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;

import java.util.ArrayList;

public class MapTest extends Map implements Disposable {
    protected Skybox skybox; //disposed
    protected DirectionalLightEx light; //undisposable
    public int[] player_coordinates=new int[]{0,0};
    public MapTest(){
        StaticBuffer.initialize_Abyss_models();
        StaticBuffer.LoadEffects();
        columns=10;
        rows=10;
        initialize();
        movingObjects.get(0).get(0).add(new Player(new Vector3(0f,0.30f,0f)));
        movingObjects.get(1).get(1).add(new Blue_Slime(new Vector3(1f,0f,1f)));
        staticObjects.get(2).get(0).add(new Barrel(new Vector3(4f,0.5f,0f)));
        SnapWallToGrid(new StoneWall(new Vector3(2f,0f,2f),270),270,1,1);
    }
    public void setPlayer_coordinates(int coord1, int coord2){
        player_coordinates[0]=coord1;
        player_coordinates[1]=coord2;
    }
    public void gridinit(){
        movingObjects=new Array<>();
        staticObjects=new Array<>();
        Tiles=new Array<>(rows);
        for (int row = 0; row < rows; row++) {
            staticObjects.add(new Array<>(columns));
            movingObjects.add(new Array<>(columns));
            Tiles.add(new Array<>(columns));
            for (int col = 0; col < columns; col++) {
                staticObjects.get(row).add(new Array<StaticObject>());
                movingObjects.get(row).add(new Array<MovingObject>());
                Tiles.get(row).add(new StoneTile(new Vector3(2*row,0,2*col)));
            }
        }
    }
    @Override
    protected void initialize(){
        skybox=new Skybox("Skyboxes/clouds.jpg");
        modelBatch=new ModelBatch();
        gridinit();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 0f));
        environment.add((new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0f, -0f)));
    }
    @Override
    public void update(int startColumn, int endColumn, int startRow, int endRow){
        player_coordinates= StaticBuffer.getPlayer_coordinates();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                removeExpiredObjects(staticObjects.get(i).get(j));
                for(int m = 0; m<staticObjects.get(i).get(j).size; m++) {
                    staticObjects.get(i).get(j).get(m).update();
                }
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    hb_interractions(movingObjects.get(i).get(j).get(m),i,j,m);
                    movingObjects.get(i).get(j).get(m).update();
                }
                removeExpiredObjectsMov(movingObjects.get(i).get(j));
            }
        }

    }
    @Override
    public void render(PerspectiveCamera camera){
        int startColumn = player_coordinates[0]-2;
        int endColumn = player_coordinates[0]+2;
        int startRow = player_coordinates[1]-2;
        int endRow =player_coordinates[1]+2;
        startColumn = Math.max(0, startColumn);
        endColumn = Math.min(columns - 1, endColumn);
        startRow = Math.max(0, startRow);
        endRow = Math.min(rows - 1, endRow);
        skybox.render(camera);
        modelBatch.begin(camera);
        if(impactFrames>0){
            impactFrames-= GameEngine.getGameCore().deltatime;
            impact_frame(startColumn,endColumn,startRow,endRow);
            modelBatch.end();
            if (impactFrames<=0){
                resetColors(startRow,endRow,startColumn,endColumn);
                impact_frames.clear();
            }
            return;
        }
        draw(startColumn,endColumn,startRow,endRow,modelBatch);
        modelBatch.end();
        StaticBuffer.decalBatch.flush();
        update(startColumn,endColumn,startRow,endRow);

    }
    public void RearrangeMoving(Array<MovingObject> source) {
        for (int m = source.size - 1; m >= 0; m--) {
            MovingObject obj = source.get(m);
            Vector3 position = obj.getPosition();
            int destRow = (int) (position.x / 2);
            int destCol = (int) (position.z / 2);
            if (destRow >= 0 && destRow < movingObjects.size &&
                destCol >= 0 && destCol < movingObjects.get(destRow).size) {
                source.removeIndex(m);
                movingObjects.get(destRow).get(destCol).add(obj);
            }
        }
    }
    @Override
    public void dispose(){
        skybox.dispose();
        modelBatch.dispose();
        for(int i=0; i<movingObjects.size; i++){
            for(int j=0; j<movingObjects.get(i).size; j++){
                for(int m=0; m<movingObjects.get(i).get(j).size; m++){
                    movingObjects.get(i).get(j).get(m).dispose();
                }
            }
        }
        for(int i=0; i<staticObjects.size; i++){
            for(int j=0; j<staticObjects.get(i).size; j++){
                for(int m=0; m<staticObjects.get(i).get(j).size; m++){
                    staticObjects.get(i).get(j).get(m).dispose();
                }
            }
        }
        for(int i=0; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                Tiles.get(i).get(j).dispose();
            }
        }
    }


    public void draw(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                modelBatch.render(Tiles.get(i).get(j).getModelInstance(), environment);
                for(int m = 0; m<staticObjects.get(i).get(j).size; m++) {
                    staticObjects.get(i).get(j).get(m).render(modelBatch,environment);
                }
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    movingObjects.get(i).get(j).get(m).render(modelBatch,environment);
                    movingObjects.get(i).get(j).get(m).render();
                }
            }
        }
    }
    public void hitbox_interractions(int startColumn, int endColumn, int startRow, int endRow){
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    hb_interractions(movingObjects.get(i).get(j).get(m),i,j,m);
                }
            }
        }
    }
    protected void hb_interractions(MovingObject movingObject, int index1, int index2, int exclude){
        int[][] indexes=calculate_radius(movingObject.getRadius(),new int[]{index1,index2},rows,columns);
        for(int[] i : indexes){
            for(int j=0; j<movingObjects.get(i[0]).get(i[1]).size; j++){
                if(i[0]!=index1 || i[1]!=index1 || j!=exclude){
                    movingObject.HITBOXINTERRACTION(movingObjects.get(i[0]).get(i[1]).get(j).gethbs());
                    movingObject.ATKHITBOXINTERRACTIONS(movingObjects.get(i[0]).get(i[1]).get(j).getAtkHbs());
                }
            }
            for(int j=0; j<staticObjects.get(i[0]).get(i[1]).size; j++){
                movingObject.HITBOXINTERRACTION(staticObjects.get(i[0]).get(i[1]).get(j).gethbs());
            }
        }
    }
    public int[][] calculate_radius(int radius, int[] index_of_cell, int limit_x, int limit_y) {
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
    @Override
    public Vector3 tie_coordinates(Vector3 coordinates, int[][] calc){
        int[] indexes=new int[]{-1,-1,-1};
        Vector3 cooordinates_lock=new Vector3(-1000,-1000,-1000);
        for (int i=0; i < calc.length; i++) {
            for(int m=0; m<movingObjects.get(calc[i][0]).get(calc[i][1]).size; m++){
                if(movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getType()==1){
                    if(StaticBuffer.getDistance(coordinates.x,coordinates.z,cooordinates_lock.x,cooordinates_lock.z)>=StaticBuffer.getDistance(coordinates.x,coordinates.z,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().x,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().z)){
                        cooordinates_lock=movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition();
                        coordinates=movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition();
                        indexes[0]=calc[i][0];
                        indexes[1]=calc[i][1];
                        indexes[2]=m;
                    }
                }
            }
        }
        if (indexes[0]==-1){
            return new Vector3();
        }
        return movingObjects.get(indexes[0]).get(indexes[1]).get(indexes[2]).getPosition();
    }
    @Override
    public int[] getNearestObject(Vector3 coordinates, int[][] calc){
        int[] indexes=new int[]{-1,-1,-1};
        Vector3 cooordinates_lock=new Vector3(-1000,-1000,-1000);
        for (int i=0; i < calc.length; i++) {
            for(int m=0; m<movingObjects.get(calc[i][0]).get(calc[i][1]).size; m++){
                if(movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getType()==1){
                    if(StaticBuffer.getDistance(coordinates.x,coordinates.z,cooordinates_lock.x,cooordinates_lock.z)>=StaticBuffer.getDistance(coordinates.x,coordinates.z,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().x,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().z)){
                        cooordinates_lock=movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition();
                        coordinates=movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition();
                        indexes[0]=calc[i][0];
                        indexes[1]=calc[i][1];
                        indexes[2]=m;
                    }
                }
            }
        }
        return indexes.clone();
    }


}
