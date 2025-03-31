package com.bestproject.main.Maps;

import static com.bestproject.main.StaticBuffer.screenHeight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ReflectionPool;
import com.bestproject.main.CostumeClasses.FrontSaceDepthProvider;
import com.bestproject.main.Enemies.Blue_Slime;
import com.bestproject.main.Enemies.FlameBoss;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.Barrel;
import com.bestproject.main.StaticObjects.Building1;
import com.bestproject.main.StaticObjects.Building2;
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
    FrameBuffer frameBuffer;
    Mesh fullscreenMesh;
    ModelBatch depthModelBatch;
    FrameBuffer depthBuffer;
    ShaderProgram outlineShader;


    public CITY(){
        fullscreenMesh=createFullScreenQuad();
        depthBuffer=new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        depthModelBatch=new ModelBatch(Gdx.files.internal("shaders/depthShader/Depth.vert").readString(),
        Gdx.files.internal("shaders/depthShader/Depth.frag").readString());
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        outlineShader=new ShaderProgram(Gdx.files.internal("shaders/Outline/outline.frag").readString()
            , Gdx.files.internal("shaders/Outline/outline.vert").readString());
        StaticBuffer.initialize_City_models();
        StaticBuffer.LoadEffects();
        columns=21;
        rows=21;
        initialize();
        buildMap();
        addMoving(new Player(new Vector3(20f,0.30f,8f)));
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 40f, 40f, 1f, 30f))
            .set(1f, 1f, 1f, 40.0f, -55f, -55f));
        environment.shadowMap = shadowLight;
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        modelBatch=new ModelBatch(new DefaultShaderProvider());



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

        float mapWidth = columns * 2;
        float mapHeight = rows * 2;
        Vector3 center = new Vector3(mapWidth/2, mapHeight/2, 0);
        float radius = mapWidth*4;

        for (int i = 0; i < 349; i += 35) {
            float angle = (float) Math.toRadians(i);
            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);
            StaticObject building;
            building=new Building1(new Vector3(x, 10, y), -i, 2f);
            addStatic(building);
            x = center.x + radius * (float) Math.cos(angle+0.6f);
            y = center.y + radius * (float) Math.sin(angle+0.6f);
        }
        for (int i = 45; i < 385; i += 45) {
            float angle = (float) Math.toRadians(i);
            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);
            StaticObject building;
            building=new Building2(new Vector3(x, 6, y), -i, 2f);
            addStatic(building);
        }
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
        skybox=new ColorFulSkybox("Models/Skyboxes/colorfulSkybox.g3dj","Models/Skyboxes/fb56bd46-c7cc-46fd-953c-59ce09405460_scaled.jpg",20);
        modelBatch=new ModelBatch();
        gridinit();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 0f));
        environment.add((new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, -0f, -0f)));
    }
    @Override
    public void update(int startColumn, int endColumn, int startRow, int endRow){
        shadowLight.getCamera().position.set(StaticBuffer.getPlayerCooordinates().x,StaticBuffer.getPlayerCooordinates().y+10,StaticBuffer.getPlayerCooordinates().z);
        shadowLight.getCamera().update();
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
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
           depthBuffer.begin();{
                Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glCullFace(GL20.GL_BACK);
                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Gdx.gl.glDepthMask(true);
                depthModelBatch.begin(camera);
                drawWithoutTiles(startColumn,endColumn,startRow,endColumn,depthModelBatch);
                depthModelBatch.end();
            }
            depthBuffer.end();

            Gdx.gl.glCullFace(GL20.GL_BACK);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
            Gdx.gl.glDepthMask(true);
            modelBatch.begin(camera);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            if(StaticBuffer.renderOverride!=null){
                StaticBuffer.renderOverride.Render(null,null);
            }
            draw(startColumn,endColumn,startRow,endColumn,modelBatch);
            modelBatch.end();
            outlineShader.begin();
            depthBuffer.getTextureAttachments().get(0).bind(0);
            outlineShader.setUniformi("u_depthTexture", 0);
            outlineShader.setUniformf("size", StaticBuffer.screenWidth, screenHeight);
            fullscreenMesh.render(outlineShader,GL20.GL_TRIANGLES);
            outlineShader.end();
            update(startColumn,endColumn,startRow,endRow);
        }

    }
    public Mesh createFullScreenQuad() {
        float[] verts = new float[]{
            // X,    Y,    Z,   U,  V
            -1.f, -1.f,  0.f,  0.f, 0.f,  // Bottom-left  (0)
            1.f, -1.f,  0.f,  1.f, 0.f,  // Bottom-right (1)
            1.f,  1.f,  0.f,  1.f, 1.f,  // Top-right    (2)
            -1.f,  1.f,  0.f,  0.f, 1.f   // Top-left     (3)
        };

        short[] indices = new short[]{
            0, 1, 2, // First triangle
            2, 3, 0  // Second triangle
        };

        Mesh tmpMesh = new Mesh(true, 4, 6,
            new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));

        tmpMesh.setVertices(verts);
        tmpMesh.setIndices(indices);

        return tmpMesh;
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
        depthBuffer.dispose();
        depthModelBatch.dispose();
        skybox.dispose();
        fullscreenMesh.dispose();
        frameBuffer.dispose();
        shadowLight.dispose();
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
    public void drawWithoutTiles(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                for(int m = 0; m<staticObjects.get(i).get(j).size; m++) {
                    if(staticObjects.get(i).get(j).get(m).isIslIneArt()) {
                        staticObjects.get(i).get(j).get(m).render(modelBatch);
                    }
                }
                if(!staticRender){
                    return;
                }
                for(int m = 0; m<movingObjects.get(i).get(j).size; m++) {
                    if(movingObjects.get(i).get(j).get(m).isIslIneArt()) {
                        movingObjects.get(i).get(j).get(m).render(modelBatch);
                        movingObjects.get(i).get(j).get(m).render();
                    }
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
                if(staticRender) {
                    for (int m = 0; m < staticObjects.get(i).get(j).size; m++) {
                        staticObjects.get(i).get(j).get(m).render(modelBatch, environment);
                    }
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
    @Override
    public Vector3 getNearestObjectCoordinates(Vector3 coordinates, int[][] calc){
        int[] indexes=new int[]{-1,-1,-1};
        Vector3 cooordinates_lock=new Vector3(-1000,-1000,-1000);
        for (int i=0; i < calc.length; i++) {
            for(int m=0; m<movingObjects.get(calc[i][0]).get(calc[i][1]).size; m++){
                if(movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getType()==2){
                    if(StaticBuffer.getDistance(coordinates.x,coordinates.z,cooordinates_lock.x,cooordinates_lock.z)>=StaticBuffer.getDistance(coordinates.x,coordinates.z,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().x,movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition().z)){
                        cooordinates_lock=movingObjects.get(calc[i][0]).get(calc[i][1]).get(m).getPosition();
                        indexes[0]=calc[i][0];
                        indexes[1]=calc[i][1];
                        indexes[2]=m;
                    }
                }
            }
        }
        return cooordinates_lock;
    }




}

