package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.MapRender.RenderPackage;
import com.bestproject.main.MapUtils.Chunk;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.ObjectItem;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;

import java.util.Iterator;


public class ChuckedMap extends Map{
    protected class ChumnckInfo{
        public int index;
        public int TranslationX;
        public int TranslationY;
        public ChumnckInfo(int x, int y, int index){
            TranslationX=x;
            TranslationY=y;
            this.index=index;
        }
    }
    RenderPackage renderPackage;
    public Array<Chunk> chunks;
    protected int chunksX, chunksY;
    Array<MovingObject> movingObjects;
    Array<StaticObject> staticObjects;
    protected Array<Array<ChumnckInfo>> chunckInfo;
    protected int chunkSizeX, chunkSizeY;
    public ChuckedMap(){
        renderPackage=new RenderPackage();
        chunks=new Array<>();
        chunckInfo=new Array<>();
        for (int i = 0; i < chunksY; i++) {
            chunckInfo.add(new Array<>());
            for (int j = 0; j < chunksX; j++) {
                chunckInfo.get(i).add(new ChumnckInfo(j, i, chunks.size - 1));
            }
        }


    }
    protected Chunk getChunkFromWorldPosition(Vector3 pos) {
        int chunkX = (int)(pos.x / (chunkSizeX * 2));
        int chunkY = (int)(pos.z / (chunkSizeY * 2));

        if (chunkX >= 0 && chunkY >= 0 && chunkX < chunksX && chunkY < chunksY) {
            int index = chunckInfo.get(chunkY).get(chunkX).index;
            return chunks.get(index);
        }
        return null;
    }
    @Override
    public void removeExpiredObjects(Array<StaticObject> objects) {
        Iterator<StaticObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            StaticObject obj = iterator.next();
            if (obj.expire()) {
                iterator.remove();
            }
        }
    }
    @Override
    public void removeExpiredObjectsMov(Array<MovingObject> objects) {
        Iterator<MovingObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            MovingObject obj = iterator.next();
            if (obj.expire()) {
                iterator.remove();
            }
        }
    }
    @Override
    public void addStatic(StaticObject object) {
        Chunk chunk = getChunkFromWorldPosition(object.getPosition());
        if (chunk != null) {
            int localX = (int)((object.getPosition().x % (chunkSizeX * 2)) / 2);
            int localY = (int)((object.getPosition().z % (chunkSizeY * 2)) / 2);
            chunk.staticObjects.get(localY).get(localX).add(object);
        }
    }

    @Override
    public void addMoving(MovingObject object) {
        Chunk chunk = getChunkFromWorldPosition(object.getPosition());
        if (chunk != null) {
            int localX = (int)((object.getPosition().x % (chunkSizeX * 2)) / 2);
            int localY = (int)((object.getPosition().z % (chunkSizeY * 2)) / 2);
            chunk.movingObjects.get(localY).get(localX).add(object);
        }
    }

    @Override
    public void dispose() {
        for (Chunk chunk : chunks) {
            chunk.dispose();
        }
    }
    @Override
    public void render(PerspectiveCamera camera) {
        Vector3 playerCoords = StaticBuffer.getPlayerCooordinates();
        int playerChunkX = (int) (playerCoords.x / (chunkSizeX * 2));
        int playerChunkY = (int) (playerCoords.z / (chunkSizeY * 2));
        draw(playerChunkX,playerChunkY,camera);
    }
    @Override
    public void draw(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){}
    public void draw(int playerChunkX, int playerChunkY, PerspectiveCamera camera){
        for (int offsetY = -1; offsetY <= 1; offsetY++) {
            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                int targetX = playerChunkX + offsetX;
                int targetY = playerChunkY + offsetY;
                if (targetX < 0 || targetY < 0 || targetX >= chunksX || targetY >= chunksY)
                    continue;
                int chunkIndex = chunckInfo.get(targetY).get(targetX).index;
                Chunk chunk = chunks.get(chunkIndex);
                chunk.translate(targetX, targetY);
                if (camera.frustum.boundsInFrustum(chunk.getBounds())) {
                    chunk.render(modelBatch, renderPackage.environment);
                }
                chunk.ResetTranslations(targetX, targetY);
            }
        }
    }
    @Override
    public void update(int startColumn, int endColumn, int startRow, int endRow){
        renderPackage.shadowLight.update(GameCore.camera);
        for (int i = startRow; i <= movingObjects.size; i++) {
            movingObjects.get(i).update();
        }for (int i = startRow; i <= staticObjects.size; i++) {
            staticObjects.get(i).update();
        }

        check();
        removeExpiredObjectsMov(movingObjects);
        removeExpiredObjects(staticObjects);

    }
    protected void check(){

    }
    @Override
    public void addStatic(ObjectItem object, int row, int col) {

    }
    @Override
    public void addMoving(MovingObject object, int row, int col) {

    }
    @Override
    public void addStatic(StaticObject object, Vector3 distance) {

    }
    @Override
    public void addMoving(MovingObject object, Vector3 distance) {

    }
    @Override
    public void resetColors(int startRow, int Endrow, int startColumn, int endColumn){

    }
    @Override
    public void ForcedHitboxInterraction(MovingObject obj){
        hb_interraction(obj, obj.getUnique_index());
    }
    @Override
    public void ForcedMovingRearr(Vector3 position){

    }
    @Override
    protected void hb_interractions(MovingObject movingObject, int index1, int index2, int exclude){
    }
    protected void hb_interraction(ObjectItem objectItem, int exclude){
        Vector3 playerCoords = StaticBuffer.getPlayerCooordinates();
        int playerChunkX = (int) (playerCoords.x / (chunkSizeX * 2));
        int playerChunkY = (int) (playerCoords.z / (chunkSizeY * 2));
    }
    @Override
    public int[] getNearestObject(Vector3 coords,int[][] calc){
        return null;
    }
    @Override
    public Vector3 getNearestObjectCoordinates(Vector3 coords,int[][] calc){
        return null;
    }
    @Override
    protected void setModelInstanceColor(MovingObject movingObject, Color color) {
    }
    @Override
    public MovingObject getPlayer(){
        return null;
    }
    @Override
    public void swap(int[] index1, int[] index){
    }
    @Override
    public void impact_frame(int startRow, int endRow, int startCol, int endCol) {
    }
    @Override
    public void saveConfiguration(){

    }
    @Override
    public String[][] getAssetNames(){
        return new String[0][0];
    }
    @Override
    public void RenderCreative(PerspectiveCamera perspectiveCamera){
    }
    @Override
    public void renderHITBOXES(){
    }
    @Override
    public void setPlayerImactFrame(float counter){

    }
    @Override
    public void setPlayerCoordinates(Vector3 position){
    }
    @Override
    public void setImactFrames(float counter){
        impactFrames=counter;
        isImpactFraming=true;
    }
    @Override
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
    @Override
    public Vector3 tie_coordinates(Vector3 coords,int[][] calc){
        return new Vector3();
    }
    @Override
    public void SnapWallToGrid(StaticObject object, int rotation, int row, int col){

    }
    @Override
    public float GetGroundLevel(Vector3 position){
        return 0f;
    }

}

