package com.bestproject.main.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.Enemies.Blue_Slime;
import com.bestproject.main.Enemies.FlameBoss;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.Barrel;
import com.bestproject.main.StaticObjects.Crate;
import com.bestproject.main.StaticObjects.EmptyHitbox;
import com.bestproject.main.StaticObjects.Flame;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticObjects.Torch;
import com.bestproject.main.StaticShaders;
import com.bestproject.main.Tiles.ConcreteTile;
import com.bestproject.main.Tiles.StoneTile;
import com.bestproject.main.Tiles.Tile;
import com.bestproject.main.Wall.StoneWall;
import com.bestproject.main.Wall.StoneWallDoor;

import net.mgsx.gltf.scene3d.attributes.FogAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;

import java.util.ArrayList;
import java.util.List;

public class CITY extends Map{
    protected ColorFulSkybox skybox; //disposed
    public int[] player_coordinates=new int[]{0,0};
    float counterImpact=0;
    ArrayList<Rectangle[]> triggerZones=new ArrayList<>();
    private Color ImpactColor=Color.WHITE;
    DirectionalShadowLight shadowLight;
    ModelBatch shadowBatch;

    public CITY(){
        StaticBuffer.initialize_City_models();
        StaticBuffer.LoadEffects();
        columns=11;
        rows=11;
        initialize();
        buildMap();
        triggerZones.add(new Rectangle[]{new Rectangle(10,2,4,6)});
        triggerZones.add(new Rectangle[]{});
        triggerZones.add(new Rectangle[]{});
        addMoving(new Player(new Vector3(20f,0.30f,8f)));
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 40f, 40f, .1f, 50f))
            .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;
        environment.set(new FogAttribute(FogAttribute.FogEquation).set(0.1f, 50, 2));

        shadowBatch = new ModelBatch(new DepthShaderProvider());
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
                Tiles.get(row).add(new ConcreteTile(new Vector3(2*row,0,2*col)));
            }
        }
    }
    public int check_zones(){
        Vector3 playerPos=StaticBuffer.getPlayerCooordinates();
        for(int i=0; i<triggerZones.size(); i++){
            for(int j=0; j<triggerZones.get(i).length; j++){
                if(triggerZones.get(i)[j].contains(playerPos.x,playerPos.z)){
                    return i;
                }
            }
        }
        return -1;
    }
    public void check(){
        int index=check_zones();
        if(index==0){
            triggerZones.remove(index);
        }
    }
    public void buildMap(){
        addMoving(new FlameBoss(new Vector3(10,0,10)));

    }
    public static List<int[]> buildWalls(Array<Array<Tile>> grid) {
        List<int[]> walls = new ArrayList<>();
        for (int y = 0; y < grid.size; y++) {
            Array<Tile> row = grid.get(y);
            for (int x = 0; x < row.size; x++) {
                Tile tile = row.get(x);
                if (tile == null) {
                    continue;
                }
                int index=Math.max(0,x-1);
                if(row.get(index)==null){
                    walls.add(new int[]{y,x,0, 1, 0});
                }
                index=Math.min(row.size-1,x+1);
                if(row.get(index)==null){
                    walls.add(new int[]{y,x,0, -1, 0});
                }
                index=Math.max(0,y-1);
                row=grid.get(index);
                if(row.get(x)==null){
                    walls.add(new int[]{y,x,1, 0, 90});
                }
                index=Math.min(grid.size-1,y+1);
                row=grid.get(index);
                if(row.get(x)==null){
                    walls.add(new int[]{y,x,-1, 0, 90});
                }
                row=grid.get(y);
            }
        }

        return walls;
    }
    @Override
    protected void initialize(){
        skybox=new ColorFulSkybox("");
        modelBatch=new ModelBatch();
        gridinit();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 0f));
        environment.add((new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, -0f, -0f)));
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
                RearrangeMoving(movingObjects.get(i).get(j));
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    movingObjects.get(i).get(j).get(m).update();

                }
                removeExpiredObjectsMov(movingObjects.get(i).get(j));
                check();
            }
        }

    }
    @Override
    public void render(PerspectiveCamera camera){
        int startColumn = 0;
        int endColumn = columns-1;
        int startRow = 0;
        int endRow =rows-1;
        skybox.render(camera);
        if(impactFrames>0){
            Gdx.gl.glClearColor(ImpactColor.r,ImpactColor.g,ImpactColor.b,ImpactColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            float delta=Gdx.graphics.getDeltaTime();
            impactFrames-=delta;
            modelBatch.begin(camera);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            impact_frame(startColumn,endColumn,startRow,endRow);
            modelBatch.end();
            update(startColumn,endColumn,startRow,endRow);
            StaticBuffer.TestShapeRenderer.setAutoShapeType(true);
            StaticBuffer.TestShapeRenderer.begin();
            StaticShaders.impactSHADER.renderLines(new Vector2(1000,500), new Vector2(100,1000),StaticBuffer.TestShapeRenderer,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            StaticBuffer.TestShapeRenderer.end();
            counterImpact+=delta;
            if(counterImpact>0.02f){
                counterImpact=0;
                float offset= MathUtils.random(1f);
                resetColors(startRow,endRow,startColumn,endColumn);
                ImpactColor=new Color(1-offset,1-offset,1-offset,1);
                StaticShaders.impactSHADER.setColor(new Color(1-offset,1-offset,1-offset,1));
                StaticShaders.impactSHADER.setSecondaryColor(new Color(0+offset,0+offset,0+offset,1));
                for (int i = startRow; i <= endRow; i++) {
                    for (int j = startColumn; j <= endColumn; j++) {
                        for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                            if(movingObjects.get(i).get(j).get(m).isImpact()) {
                                setModelInstanceColor(movingObjects.get(i).get(j).get(m),new Color(0+offset,0+offset,0+offset,1));
                            }
                        }
                    }
                }
            }
            if (impactFrames<=0){
                resetColors(startRow,endRow,startColumn,endColumn);
                impact_frames.clear();
            }
        } else{
            drawShadows(camera,startColumn,endColumn,startRow,endRow);
            modelBatch.begin(camera);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            draw(startColumn,endColumn,startRow,endRow,modelBatch);
            modelBatch.end();
            update(startColumn,endColumn,startRow,endRow);
        }

    }
    public void drawShadows(PerspectiveCamera camera,int startColumn,int endColumn,int startRow,int endRow){
        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    movingObjects.get(i).get(j).get(m).render(shadowBatch);
                }
            }
        }
        shadowBatch.end();
        shadowLight.end();
    }
    @Override
    public void impact_frame(int startCol, int endCol,int startRow, int endRow) {
        environment.set(ColorAttribute.createDiffuse(Color.BLACK));
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    if(movingObjects.get(i).get(j).get(m).isImpact()) {
                        movingObjects.get(i).get(j).get(m).render(modelBatch);
                    }
                }
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
                if(Tiles.get(i).get(j)!=null){
                    Tiles.get(i).get(j).dispose();
                }
            }
        }
    }


    public void draw(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                if(Tiles.get(i).get(j)!=null){
                    modelBatch.render(Tiles.get(i).get(j).getModelInstance(), environment);
                }
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
                if(movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getType()==2){
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

