package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.MapUtils.HITBOXMAP;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.ObjectItem;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.Tiles.Tile;

import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Map implements Disposable {
    protected int rows, columns;
    public Array<Array<Array<MovingObject>>> movingObjects; //disposed
    public Array<Array<Array<StaticObject>>> staticObjects; //disposed
    protected Array<Array<Tile>> Tiles;//disposed
    protected boolean isImpactFraming=false;
    protected float impactFrames=0;
    protected ArrayList<int[]> impact_frames = new ArrayList<>();
    protected ModelBatch modelBatch;
    protected Environment environment;
    public boolean staticRender=true;
    public int mapIndex=-1;

    public Map(){
        initialize();
    }
    protected void initialize(){

    }
    public Environment getEnvironment() {
        return null;
    }

    @Override
    public void dispose() {
    }
    public void initializeMapAttributes(int mapIndex){

    }
    public void AfterInit(){

    }
    public void LoadDependencies(){

    }
    public void RearrangeMoving(Array<MovingObject> source) {
        for (int m = source.size - 1; m >= 0; m--) {
            MovingObject obj = source.get(m);
            Vector3 position = obj.getPosition();
            int destRow = (int) ((position.x-1) / 2);
            int destCol = (int) ((position.z-1) / 2);
            if (destRow >= 0 && destRow < movingObjects.size &&
                destCol >= 0 && destCol < movingObjects.get(destRow).size) {
                source.removeIndex(m);
                movingObjects.get(destRow).get(destCol).add(obj);
            }
        }
    }
    public void MapInitialization(){
    }
    public void removeObjectFromTile(ObjectItem object, int row, int col) {
    }
    public void addStatic(ObjectItem object, int row, int col) {
        staticObjects.get(row).get(col).add((StaticObject) object);
    }
    public void addMoving(MovingObject object, int row, int col) {
        movingObjects.get(row).get(col).add((MovingObject) object);
    }
    public void addStatic(StaticObject object, Vector3 distance) {
        staticObjects.get((int) (distance.x/2)).get((int)(distance.z/2)).add(object);
    }
    public void addMoving(MovingObject object, Vector3 distance) {
        movingObjects.get((int)(distance.x/2)).get((int)(distance.z/2)).add(object);
    }
    public void draw(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){}
    public void addStatic(StaticObject object) {
        Vector3 distance=object.getPosition();
        int destrow=Math.min(rows-1, Math.max((int)((distance.x-1)/2),0));
        int destcol=Math.min(columns-1, Math.max((int)((distance.z-1)/2),0));
        staticObjects.get(destrow).get(destcol).add(object);
    }
    public void addMoving(MovingObject object) {
        Vector3 distance=object.getPosition();
        int destrow=Math.min(rows-1, Math.max((int)((distance.x-1)/2),0));
        int destcol=Math.min(columns-1, Math.max((int)((distance.z-1)/2),0));
        movingObjects.get(destrow).get(destcol).add(object);
    }
    public void update(int startColumn, int endColumn, int startRow, int endRow){

    }
    public void setStaticRender(boolean render){
        this.staticRender=render;
    }
    public void render(PerspectiveCamera perspectiveCamera){

    }
    public void Tile_offsetStatic(ObjectItem object, int row, int col, ArrayList<ArrayList<ArrayList<StaticObject>>> stat) {
        int x= (int) (object.getPosition().z/2);
        int y=(int) (object.getPosition().x/2);
        stat.get(x).get(y).add((StaticObject) object);
    }
    public void Tile_offsetMoving(ObjectItem object, int row, int col, ArrayList<ArrayList<ArrayList<MovingObject>>> mov) {
        int x= (int) (object.getPosition().z/2);
        int y=(int) (object.getPosition().x/2);
        mov.get(x).get(y).add((MovingObject) object);

    }
    public void removeExpiredObjects(Array<StaticObject> objects) {
        Iterator<StaticObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            StaticObject obj = iterator.next();
            if (obj.expire()) {
                iterator.remove();
            }
        }
    }
    public void removeExpiredObjectsMov(Array<MovingObject> objects) {
        Iterator<MovingObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            MovingObject obj = iterator.next();
            if (obj.expire()) {
                iterator.remove();
            }
        }
    }
    public void ForcedHitboxInterraction(MovingObject obj){
        int normalizedy=(int)Math.min(rows-1,Math.max(0,(obj.position.z-1)/2));
        int normalizedx=(int) Math.min(columns-1,Math.max(0,(obj.position.x-1)/2));
        hb_interractions(obj,normalizedx, normalizedy, obj.getUnique_index());
    }
    public void ForcedMovingRearr(Vector3 position){
        int normalizedy=(int)Math.min(rows-1,Math.max(0,(position.z-1)/2));
        int normalizedx=(int) Math.min(columns-1,Math.max(0,(position.x-1)/2));
        RearrangeMoving(movingObjects.get(normalizedx).get(normalizedy));
    }
    public void setImpactFrames(float value){
        impactFrames=value;
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
    public void resetColors(int startRow, int Endrow, int startColumn, int endColumn){
        for (int i = startRow; i <= Endrow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    movingObjects.get(i).get(j).get(m).resetColors();
                }
            }
        }
    }
    protected void hb_interractions(MovingObject movingObject, int index1, int index2, int exclude){
        int[][] indexes=calculate_radius(movingObject.getRadius(),new int[]{index1,index2},rows,columns);
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
    public int[] getNearestObject(Vector3 coords,int[][] calc){
        return null;
    }
    public Vector3 getNearestObjectCoordinates(Vector3 coords,int[][] calc){
        return null;
    }
    protected void setModelInstanceColor(MovingObject movingObject, Color color) {
        if(movingObject.getModelInstance()!=null){
        for (int i = 0; i < movingObject.getModelInstance().materials.size; i++) {
            ColorAttribute colorAttribute = movingObject.getModelInstance().materials.get(i).get(ColorAttribute.class, ColorAttribute.Diffuse);
            movingObject.setTempAttribute(colorAttribute.color);
            if (movingObject.getModelInstance() != null) {
                movingObject.getModelInstance().materials.get(i).set(ColorAttribute.createDiffuse(color));
            }
        }
    }
    }
    public MovingObject getPlayer(){
        int[] index1=new int[]{StaticBuffer.getPlayer_coordinates()[1],StaticBuffer.getPlayer_coordinates()[0]};
        for(int i=0; i<movingObjects.get(index1[0]).get(index1[1]).size; i++){
            if(movingObjects.get(index1[0]).get(index1[1]).get(i).getType()==-1){
                return movingObjects.get(index1[0]).get(index1[1]).get(i);
            }
        }
        return null;
    }
    public void swap(int[] index1, int[] index){
        int m=-1;
        for(int i=0; i<movingObjects.get(index1[0]).get(index1[1]).size; i++){
            if(movingObjects.get(index1[0]).get(index1[1]).get(i).getType()==-1){
                m=i;
                setImactFrames(0.06f);
                impact_frames.add(new int[]{index1[0],index1[1],m});
                impact_frames.add(index);
                break;
            }
        }
        if(m!=-1){
            movingObjects.get(index1[0]).get(index1[1]).get(m).setImpact(true);
            movingObjects.get(index[0]).get(index[1]).get(index[2]).setImpact(true);
            setModelInstanceColor(movingObjects.get(index1[0]).get(index1[1]).get(m),Color.BLACK);
            setModelInstanceColor(movingObjects.get(index[0]).get(index[1]).get(index[2]),Color.BLACK);
            Vector3 player_pos=new Vector3().set(movingObjects.get(index1[0]).get(index1[1]).get(m).getPosition());
            Vector3 enemy_pos=new Vector3().set(movingObjects.get(index[0]).get(index[1]).get(index[2]).getPosition());
            enemy_pos.y=player_pos.y;
            player_pos.y=enemy_pos.y;
            movingObjects.get(index[0]).get(index[1]).get(index[2]).setPosition(player_pos);
            movingObjects.get(index1[0]).get(index1[1]).get(m).setPosition(enemy_pos);

        }
    }
    public void impact_frame(int startRow, int endRow, int startCol, int endCol) {
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    if(movingObjects.get(i).get(j).get(m).isImpact()) {
                        movingObjects.get(i).get(j).get(m).render(modelBatch, environment);
                    }
                }
            }
        }
    }
    public void saveConfiguration(){

    }
    public String[][] getAssetNames(){
        return new String[0][0];
    }
    public void RenderCreative(PerspectiveCamera perspectiveCamera){
        modelBatch.begin(perspectiveCamera);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        draw(0,columns-1,0,rows-1,modelBatch);
        modelBatch.end();
        renderHITBOXES();
    }
    public void renderHITBOXES(){
        for(Array<Array<MovingObject>> i : movingObjects){
            for(Array<MovingObject> j : i){
                for(MovingObject m : j){
                    m.RenderHitboxes();
                }
            }
        }
        for(Array<Array<StaticObject>> i : staticObjects){
            for(Array<StaticObject> j : i){
                for(StaticObject m : j){
                    m.RenderHitboxes();
                }
            }
        }
    }
    public void setPlayerImactFrame(float counter){
        int m=-1;
        for(int i=0; i<movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).size; i++){
            if(movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).get(i).getType()==-1){
                m=i;
                break;
            }
        }
        if(m!=-1) {
            setModelInstanceColor(movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).get(m),Color.BLACK);
            movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).get(m).setImpact(true);
            setImactFrames(counter);
        }
    }
    public void setPlayerCoordinates(Vector3 position){
        for(int i=0; i<movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).size; i++){
            if(movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).get(i).getType()==-1){
                movingObjects.get(StaticBuffer.getPlayer_coordinates()[1]).get(StaticBuffer.getPlayer_coordinates()[0]).get(i).setPosition(new Vector3(position));
                break;
            }
        }
    }
    public int getUniqueIndex(){
        return mapIndex;
    }
    public void setImactFrames(float counter){
        impactFrames=counter;
        isImpactFraming=true;
    }
    public int[][] calculate_radius(int radius, int[] index_of_cell) {
        int x = index_of_cell[0];
        int y = index_of_cell[1];
        int min_x = Math.max(0, x - radius);
        int max_x = Math.min(rows - 1, x + radius);
        int min_y = Math.max(0, y - radius);
        int max_y = Math.min(columns - 1, y + radius);
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
    protected void additionalRender(){

    }
    public Vector3 tie_coordinates(Vector3 coords,int[][] calc){
        return new Vector3();
    }
    public void SnapWallToGrid(StaticObject object, int rotation, int row, int col){
        if(rotation==0){
            object.setPosition(new Vector3(2*(col),1,(row)*2));
        } else if (rotation == 90) {
            object.setPosition(new Vector3(2*(col)+1,1,(row)*2));
        } else if (rotation == 180) {
            object.setPosition(new Vector3(2*(col),1,(row)*2-1));
        } else if (rotation==270) {
            object.setPosition(new Vector3(2*(col)-1,1,(row)*2));
        }
        staticObjects.get(row).get(col).add(object);
    }
    public float GetGroundLevel(Vector3 position){
        int normalizedy=(int)Math.min(rows-1,Math.max(0,position.z/2));
        int normalizedx=(int) Math.min(columns-1,Math.max(0,position.x/2));
        if(Tiles.get(normalizedx).get(normalizedy)!=null){
            return Tiles.get(normalizedx).get(normalizedy).getGroundLevel();
        }
        return 0;
    }
    public HITBOXMAP getHitboxMap(){
        return null;
    }
    public Texture getHeightMap(){
        return null;
    }
    public Texture getShadowTexture(){
        return null;
    }

}
