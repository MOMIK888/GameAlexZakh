package com.bestproject.main.Maps;

import static com.bestproject.main.StaticBuffer.screenHeight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.CostumeClasses.HeightBasedRect;
import com.bestproject.main.Environment.Rain;
import com.bestproject.main.Environment.WeatherEffector;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.MainGame;
import com.bestproject.main.MapRender.RenderPackage;
import com.bestproject.main.MapUtils.Chunk;
import com.bestproject.main.MapUtils.HITBOXMAP;
import com.bestproject.main.MapUtils.WeatherArea;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.ObjectItem;
import com.bestproject.main.SingleObjectMaps.SingleObjectMap;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;
import com.bestproject.main.Tiles.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ChuckedMap extends Map{
    WeatherEffector weatherEffector;
    protected ColorFulSkybox skybox; //disposed
    public int[] player_coordinates=new int[]{0,0};
    float counterImpact=0;
    ArrayList<HeightBasedRect[]> triggerZones=new ArrayList<>();
    private Color ImpactColor=Color.WHITE;
    DirectionalShadowLight shadowLight;
    ModelBatch shadowBatch;
    FrameBuffer frameBuffer;
    Mesh fullscreenMesh;
    ModelBatch depthModelBatch;
    FrameBuffer depthBuffer;
    ShaderProgram outlineShader;
    ShaderProgram toonShader;
    DirectionalLight directionalLightEx;
    WeatherArea weatherArea;
    Array<MovingObject> movingObjects2=new Array<>();
    Array<StaticObject> staticObjects2 =new Array<>();
    Texture[] uniqueTextures;
    protected class ChumnckInfo{
        public int index;
        public ChumnckInfo(int index){
            this.index=index;
        }
    }
    public Array<Chunk> chunks;
    protected int chunksX, chunksY;
    protected Array<Array<ChumnckInfo>> chunckInfo;
    public ChuckedMap(String chunkInfoDecipher, int randomValue, int chunksX, int chunksY, int mapIndex){
        chunks=new Array<>();
        initializeMapAttributes(mapIndex);
        this.chunksX=chunksX;
        this.chunksY=chunksY;
        chunckInfo=new Array<>();
        if(chunkInfoDecipher=="") {
            for (int i = 0; i < chunksY; i++) {
                chunckInfo.add(new Array<>());
                for (int j = 0; j < chunksX; j++) {
                    chunckInfo.get(i).add(new ChumnckInfo(0));
                }
            }
        } else if (chunkInfoDecipher=="RANDOM"){
            for (int i = 0; i < chunksY; i++) {
                chunckInfo.add(new Array<>());
                for (int j = 0; j < chunksX; j++) {
                    chunckInfo.get(i).add(new ChumnckInfo(MathUtils.random(0,randomValue)));
                }
            }
        } else{
            Array<Array<Integer>> values=decipherChunckInfo(chunkInfoDecipher);
            for (int i = 0; i < values.size; i++) {
                chunckInfo.add(new Array<>());
                for (int j = 0; j < values.get(i).size; j++) {
                    chunckInfo.get(i).add(new ChumnckInfo(values.get(i).get(j)));
                }
            }
        }


    }
    protected static Array<Array<Integer>> decipherChunckInfo(String chunkInfo){
        String[] brokenInfo=chunkInfo.split("\\^");
        Array<String[]> fractured_info=new Array<>();
        for(int i=0; i<brokenInfo.length; i++){
            String[] row=brokenInfo[i].split("\\$");
            fractured_info.add(row);
        }
        Array<Array<Integer>> indexes=new Array<>();
        for(int i=0; i<fractured_info.size; i++){
            Array<Integer> nums=new Array<>();
            for(int j=0; j<fractured_info.get(0).length; j++){
                nums.add(Integer.valueOf(fractured_info.get(i)[j]));
            }
            indexes.add(nums);
        }
        return indexes;
    }
    @Override
    public void initializeMapAttributes(int mapIndex){
        fullscreenMesh=createFullScreenQuad();
        depthBuffer=new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        weatherArea=new WeatherArea(StaticBuffer.constantAssets);
        depthModelBatch=new ModelBatch(Gdx.files.internal("shaders/depthShader/Depth.vert").readString(),
            Gdx.files.internal("shaders/depthShader/Depth.frag").readString());
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        outlineShader=new ShaderProgram(Gdx.files.internal("shaders/Outline/outline.frag").readString()
            , Gdx.files.internal("shaders/Outline/outline.vert").readString());
        LoadDependencies();
        AfterInit();
    }
    @Override
    public void AfterInit(){
        columns=10;
        rows=10;
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f))
            .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;
        shadowBatch = new ModelBatch(new DepthShaderProvider());
        toonShader=new ShaderProgram(Gdx.files.internal("shaders/toon/vertex.glsl").readString(),
            Gdx.files.internal("shaders/toon/fragment.glsl").readString());
    }
    @Override
    public void LoadDependencies(){
    }
    @Override
    public void MapInitialization(){
        initialize();
        buildMap();
        directionalLightEx=new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0f, -0f);
        environment.add(directionalLightEx);
        directionalLightEx.color.set(
            Color.valueOf("4A5A6A").lerp(new Color(0f,0f,0f,1f),0.3f)
        );
        movingObjects=null;
        staticObjects =null;
        Tiles=null;
        weatherEffector=null;
        weatherArea.switchWeather(0);
    }
    public void setPlayer_coordinates(int coord1, int coord2){
        player_coordinates[0]=coord1;
        player_coordinates[1]=coord2;
    }
    public void gridinit(){
    }
    @Override
    public void ForcedHitboxInterraction(MovingObject obj){
        float width=chunks.get(0).width;
        int x = MathUtils.clamp((int)Math.ceil((obj.getPosition().x-width/2)/width),0,chunckInfo.get(0).size-1);
        int y = MathUtils.clamp((int)Math.ceil((obj.getPosition().z-width/2)/width),0,chunckInfo.size-1);
        chunks.get(chunckInfo.get(y).get(x).index).hb_interractions(obj,x,y);
        if (obj.getAtkHbs()!=null){
            for(int i=0; i<movingObjects2.size; i++){
                if(movingObjects2.get(i).getUnique_index()!=obj.getUnique_index()){
                    movingObjects2.get(i).ATKHITBOXINTERRACTIONS(obj.getAtkHbs());
                }
            }
        }
    }
    @Override
    public void ForcedMovingRearr(Vector3 position){

    }
    public int check_zones(){
        Vector3 playerPos=StaticBuffer.getPlayerCooordinates();
        for(int i=0; i<triggerZones.size(); i++){
            for(int j=0; j<triggerZones.get(i).length; j++){
                if(triggerZones.get(i)[j].contains(playerPos)){
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
        skybox=new ColorFulSkybox("Models/Skyboxes/skybox2.g3dj","Models/Skyboxes/SkyboxBlue.png",4f);
        modelBatch=new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 0f));

    }
    @Override
    public void update(int startColumn, int endColumn, int startRow, int endRow){
        skybox.setPosition(StaticBuffer.getPlayerCooordinates());
        weatherArea.update();
        player_coordinates= StaticBuffer.getPlayer_coordinates();
        for(int m = 0; m< staticObjects2.size; m++) {
            staticObjects2.get(m).update();
        }
        for(int m = 0; m<movingObjects2.size; m++) {
            movingObjects2.get(m).update();
        }
        removeExpiredObjectsMov(movingObjects2);
        removeExpiredObjects(staticObjects2);

        check();

    }
    @Override
    public void render(PerspectiveCamera camera){
        int startColumn = 0;
        int endColumn = columns-1;
        int startRow = 0;
        int endRow =rows-1;
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
            if (impactFrames<=0){
                impact_frames.clear();
            }
        } else{
            frameBuffer.begin();
            {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glCullFace(GL20.GL_BACK);
                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Gdx.gl.glDepthMask(true);
                skybox.render(camera);
                modelBatch.begin(camera);
                if (StaticBuffer.renderOverride != null) {
                    StaticBuffer.renderOverride.Render(null, null);
                }
                draw(startColumn, endColumn, startRow, endColumn, modelBatch);
                weatherArea.render(modelBatch);
                modelBatch.end();
                additionalRender();
                if(weatherEffector!=null) {
                    weatherEffector.render(StaticBuffer.decalBatch);
                }
                StaticBuffer.decalBatch.flush();
            }
            frameBuffer.end();
            frameBuffer.getColorBufferTexture().bind(0);
            {
                toonShader.begin();{
                fullscreenMesh.render(toonShader, GL20.GL_TRIANGLES);
            }
                toonShader.end();
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            depthBuffer.begin();{
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
        }
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);
        outlineShader.begin();
        depthBuffer.getTextureAttachments().get(0).bind(0);
        outlineShader.setUniformi("u_depthTexture", 0);
        outlineShader.setUniformf("size", StaticBuffer.screenWidth, screenHeight);
        fullscreenMesh.render(outlineShader,GL20.GL_TRIANGLES);
        outlineShader.end();
        update(startColumn,endColumn,startRow,endRow);
        if(weatherEffector!=null) {
            weatherEffector.update(StaticQuickMAth.move(GameCore.deltatime));
        }

    }
    public Mesh createFullScreenQuad() {
        float[] verts = new float[]{
            -1.f, -1.f,  0.f,  0.f, 0.f,
            1.f, -1.f,  0.f,  1.f, 0.f,
            1.f,  1.f,  0.f,  1.f, 1.f,
            -1.f,  1.f,  0.f,  0.f, 1.f
        };

        short[] indices = new short[]{
            0, 1, 2,
            2, 3, 0
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
        for(int m = 0; m<movingObjects.size; m++) {
            movingObjects2.get(m).render(shadowBatch);
        }
        for(int m = 0; m< staticObjects2.size; m++) {
            staticObjects2.get(m).render(shadowBatch);
        }
        shadowBatch.end();
        shadowLight.end();
    }
    @Override
    public void impact_frame(int startCol, int endCol,int startRow, int endRow) {
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
            movingObjects2.get(i).dispose();
        }
        for(int i = 0; i< staticObjects2.size; i++){
            staticObjects2.get(i).dispose();
        }
    }
    public void draw2(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
        for(int i=0; i<movingObjects.size; i++){
            movingObjects2.get(i).render(modelBatch);

        }
        for(int i = 0; i< staticObjects2.size; i++){
            staticObjects2.get(i).render(modelBatch);

        }
    }
    public void drawWithoutTiles(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
    }


    public void draw(int startColumn, int endColumn, int startRow, int endRow, ModelBatch modelBatch){
        for(int i=0; i<movingObjects2.size; i++){
            movingObjects2.get(i).render(modelBatch,environment);

        }
        for(int i=0; i<staticObjects2.size; i++){
            staticObjects2.get(i).render(modelBatch,environment);

        }
        int[][] surrounding=Chunk.calculate_radius(1,StaticBuffer.getPlayerCooordinates(),chunks.get(0).width,chunckInfo.size,chunckInfo.get(0).size);
        for(int i=0; i<surrounding.length; i++){
            chunks.get(chunckInfo.get(surrounding[i][0]).get(surrounding[i][1]).index).resetTranslations();
            chunks.get(chunckInfo.get(surrounding[i][0]).get(surrounding[i][1]).index).translate(surrounding[i][0],surrounding[i][1]);
            chunks.get(chunckInfo.get(surrounding[i][0]).get(surrounding[i][1]).index).render(modelBatch,environment);
        }

    }
    @Override
    public void swap(int[] index1, int[] index){

    }
    @Override
    public Vector3 tie_coordinates(Vector3 coordinates, int[][] calc){
        return new Vector3();
    }
    @Override
    public int[] getNearestObject(Vector3 coordinates, int[][] calc){
        int[] indexes=new int[]{-1,-1,-1};
        return indexes.clone();
    }
    @Override
    public Vector3 getNearestObjectCoordinates(Vector3 coordinates, int[][] calc){
        return new Vector3();
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
    public void addStatic(StaticObject object) {
        staticObjects2.add(object);
    }
    @Override
    public void addMoving(MovingObject object) {
        movingObjects2.add(object);
    }
    @Override
    public void resetColors(int startRow, int Endrow, int startColumn, int endColumn){

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
    protected void setModelInstanceColor(MovingObject movingObject, Color color) {
    }
    @Override
    public float GetGroundLevel(Vector3 pos){
        return 0;
    }
    @Override
    public void renderHITBOXES(){
        StaticBuffer.showHitbox.render(new Array<>());
    }
    @Override
    public void RenderCreative(PerspectiveCamera perspectiveCamera){
        modelBatch.begin(perspectiveCamera);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        draw(0,columns-1,0,rows-1,modelBatch);
        modelBatch.end();
    }
    @Override
    public HITBOXMAP getHitboxMap(){
        return null; //BCGHFDBNM
    }
    @Override
    public int getUniqueIndex(){
        return mapIndex;
    }
}
