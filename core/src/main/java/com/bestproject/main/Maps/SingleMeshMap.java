package com.bestproject.main.Maps;

import static com.bestproject.main.Game.GameCore.deltatime;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.CostumeClasses.HeightBasedRect;
import com.bestproject.main.Environment.Rain;
import com.bestproject.main.Environment.WeatherEffector;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.LoadScreen.LoadingScreen;
import com.bestproject.main.MainGame;
import com.bestproject.main.MapUtils.BasicWeatherController;
import com.bestproject.main.MapUtils.HITBOXMAP;
import com.bestproject.main.MapUtils.WeatherArea;
import com.bestproject.main.MovingObjects.MovingObject;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.ObjectItem;
import com.bestproject.main.SingleObjectMaps.SingleObjectMap;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticObjects.EffectMap.StaticEffect;
import com.bestproject.main.StaticObjects.StaticObject;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;
import com.bestproject.main.Tiles.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SingleMeshMap extends Map{
    WeatherEffector weatherEffector;
    protected ColorFulSkybox skybox; //disposed
    public int[] player_coordinates=new int[]{0,0};
    float counterImpact=0;
    ShaderProgram shadowMapDebugShader=new ShaderProgram(Gdx.files.internal("shaders/Debug/vertex.glsl").readString(),
        Gdx.files.internal("shaders/Debug/fragment.glsl").readString());
    ArrayList<HeightBasedRect[]> triggerZones=new ArrayList<>();
    private Color ImpactColor=Color.WHITE;
    DirectionalShadowLight shadowLight;
    ModelBatch shadowBatch;
    FrameBuffer frameBuffer;
    Mesh fullscreenMesh;
    ModelBatch depthModelBatch;
    FrameBuffer depthBuffer;
    FrameBuffer shadebuffer=new FrameBuffer(Pixmap.Format.RGBA8888, (int) StaticBuffer.screenWidth, (int) screenHeight,true);
    ShaderProgram outlineShader;
    ShaderProgram toonShader;
    ShaderProgram impactShader;
    DirectionalLight directionalLightEx;
    WeatherArea weatherArea;
    Array<MovingObject> movingObjects2=new Array<>();
    Array<StaticObject> staticObjects2 =new Array<>();
    SingleObjectMap singleObjectMap;
    HITBOXMAP hitboxmap;
    Texture[] uniqueTextures;
    BasicWeatherController basicWeatherController;


    public SingleMeshMap(int mapIndex){
        initializeMapAttributes(mapIndex);
    }
    @Override
    public void initializeMapAttributes(int mapIndex){
        if(mapIndex==0) {
            hitboxmap = new HITBOXMAP(StaticBuffer.decipherHitboxInfo("3.3999996$1.1$0.87499976&2.7999997$1.9999999$1.5999999&0&^3.0$6.7$2.0&4.0$4.0$4.0&0&^1.0999995$0.6000001$0.3000001&1.0999995$0.6000001$0.70000005&0&^1.1999999$0.30000007$0.70000005&1.4$0.2000001$1.4&0&^1.0999999$4.2999997$0.7500002&0.10000066$1.3999997$1.2499998&0&^-2.2499986$1.5999997$0.32500052&1.1999997$0.45000035$0.44999996&0&^2.8500006$-3.299997$0.97500014&3.7999911$4.6999974$1.7249976&0&^-4.25$1.05$0.8500001&2.7499988$1.9999993$1.6500001&0&^4.6$2.7$0.8000001&0.20000029$1.4999998$1.5999998&0&^-1.9999976$-2.1749978$0.7250003&0.15000112$1.4749995$1.3249996&0&^-2.199997$1.8999991$0.75000066&1.4499989$0.050001394$1.4499989&0&^-3.7999885$5.4499974$0.95000046&3.799973$4.899997$1.799998&0&^-3.7499664$-4.649962$0.7000019&3.7999463$3.6999865$2.9999845&0&^3.9$-10.999999$1.0000001&3.4999995$4.999997$4.1999993&0&^11.700001$-8.7$1.0000001&3.749998$3.3999996$4.1999993&0&^11.899998$0.89999735$2.2999997&4.3999977$3.7999992$4.599999&0&^11.899998$10.349989$2.0&4.3999977$3.5999997$3.9999995&0&^8.100007$13.749981$2.3999991&3.3000002$3.899999$4.599998&0&^9.54999$10.99999$0.32499948&0.45000035$1.2999992$0.44999996&0&^-0.39999753$11.299975$0.3000001&0.6000001$1.0999995$0.70000005&0&^-5.5499687$8.524913$0.7250003&0.15000112$1.4749995$1.4749938&0&^-0.7000261$11.149905$0.7250003&0.15000112$1.4749995$1.4749938&0&^4.6744986$-6.400074$0.74998504&0.15000112$1.4749995$1.3999537&0&^9.649868$-9.697936$0.32499948&0.5498782$1.2001213$0.44999996&0&^5.0525546$-6.2522106$0.34993842&0.5498782$1.2001213$0.5497557&0&^-1.9999979$12.0$1.0&18.99999$0.40000027$4.199998&0&^8.100001$-10.699999$1.0&5.1499915$0.80000025$4.199998&0&^9.950002$11.0$1.0&0.3875003$25.42499$4.199998&0&^10.6500025$-4.199998$1.0&0.3875003$6.6749897$4.199998&0&^-5.849968$8.524913$0.2750012&0.5000005$1.275$0.62499547&0&^-5.949968$7.324915$0.87500006&0.8499998$0.9750005$1.5749936&0&^-5.949968$6.2249174$0.5250007&0.8499998$1.225$0.77499515&0&^-5.949968$4.97492$0.5250007&0.8499998$1.3749998$0.77499515&0&^-6.774966$0.32492408$0.42500183&2.5749915$2.149994$0.19999684&0&^-6.774966$0.32492408$0.32500258&2.5999913$2.5249913$0.1249974&0&^-6.774966$0.32492408$0.21562839&2.5749915$2.8968635$0.1249974&0&^-6.774966$0.32492408$0.13437898&2.5874913$3.2656107$0.1249974&0&^-4.4062333$-0.18132216$0.51875114&0.4375073$0.4375067$0.8812418&0&^-2.856243$-2.5750551$0.36875334&1.537491$0.5375066$0.58124626&0&^1.7499994$4.7$0.3000001&1.1999995$0.50000006$0.45000017&0&^-1.6499982$5.7999988$0.49999994&0.4500001$1.749999$0.84999985&0&^-7.7999673$11.124911$0.7250003&2.5499995$1.4749995$1.4749938&0&^-6.1499677$0.37492314$1.0250005&0.45000008$2.1749992$1.0749948&0&^-10.999991$0.7000255$1.0&0.30000055$24.79998$4.199998&0&^-4.2000012$-10.099959$1.0&14.399995$0.7$4.199998&0&^-12.149955$-1.6499653$0.7000019&3.7999463$18.099966$2.9999845&0&^-12.149955$2.9500093$2.7999904&3.7999463$8.100021$1.2999939&0&^-9.7499695$1.1499994$0.55000174&1.6500049$4.999983$1.6500001&0&^-9.7499695$-2.2499819$0.55000174&1.5500054$1.8999999$1.6500001&0&^-9.949968$-7.149955$0.55000174&0.85000926$2.0999987$1.0500034&0&^-10.349966$-5.1249647$0.20000394&0.300012$2.3499937$0.2500078&0&^-9.349971$-9.724942$0.20000394&2.3499937$0.300012$0.35000563&0&^5.849999$1.8000002$0.12500015&2.4$3.3999987$0.15000021&0&^5.874999$1.8000002$0.20000008&2.4500003$2.8999968$0.15000021&0&^5.874999$1.8000002$0.42499986&2.5000005$2.0499935$0.15000021&0&^5.849999$1.8000002$0.29999998&2.5250006$2.449995$0.15000021&0&^5.1749997$1.7000002$1.0249991&0.6000015$2.0499933$1.1499989&0&^9.174991$1.8000001$0.7249996&1.4000006$5.34999$1.3499987&0&^5.099998$-3.9499938$0.9249997&0.80000174$3.4499984$1.8499997&0&^-3.799988$-6.8499913$1.1249993&3.9499967$0.80000174$2.149999&0&^4.249989$-6.049992$0.5250009&0.6500063$0.80000174$0.85000193&0&^3.0999982$1.7999988$1.6000001&3.499998$3.699997$0.10000024&0&^0.2750008$6.799994$3.7999992&1.6249999$3.699997$0.15000024&0&^-3.5999942$5.549995$3.1999998&4.024997$4.899996$0.15000024&0&^-3.9999936$0.35000226$1.6000023&3.6249979$3.6999972$0.15000024&0&^-5.149987$-1.9999945$1.7000034&1.2250016$2.0999997$0.15000024&0&^"));
        } else{
            hitboxmap = new HITBOXMAP(StaticBuffer.decipherHitboxInfo(MainGame.databaseInterface[0].getInfo(mapIndex)));
        }
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
        impactShader=new ShaderProgram(Gdx.files.internal("shaders/ImpacrShader/vertex.glsl").readString(),
            Gdx.files.internal("shaders/ImpacrShader/fragment.glsl").readString());
    }
    @Override
    public void LoadDependencies(){
    }
    @Override
    public void MapInitialization(){
        initialize();
        buildMap();
        weatherArea=new WeatherArea(StaticBuffer.constantAssets);
        directionalLightEx=new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0f, -0f);
        environment.add(directionalLightEx);
        singleObjectMap=new SingleObjectMap(StaticBuffer.currentModels.get(0));
        directionalLightEx.color.set(
            Color.valueOf("4A5A6A").lerp(new Color(0f,0f,0f,1f),0.3f)
        );
        movingObjects=null;
        staticObjects =null;
        Tiles=null;
        singleObjectMap.scale(110,110,110);
        weatherEffector=new Rain();
        basicWeatherController=new BasicWeatherController(StaticQuickMAth.time);
    }
    public void setPlayer_coordinates(int coord1, int coord2){
        player_coordinates[0]=coord1;
        player_coordinates[1]=coord2;
    }
    public void gridinit(){
    }
    @Override
    public void ForcedHitboxInterraction(MovingObject obj){
        hitboxmap.hitboxInterraction(obj);
        if(obj.getAtkHbs()!=null) {
            for (int i = 0; i < movingObjects2.size; i++) {
                if (movingObjects2.get(i).getUnique_index() != obj.getUnique_index()) {
                    movingObjects2.get(i).ATKHITBOXINTERRACTIONS(obj.getAtkHbs());
                }
            }
        }
        if(obj.gethbs()!=null){
            for (int i = 0; i < movingObjects2.size; i++) {
                if (movingObjects2.get(i).getUnique_index() != obj.getUnique_index()) {
                    movingObjects2.get(i).HITBOXINTERRACTION(obj.gethbs());
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
        skybox.update();
        basicWeatherController.update(StaticQuickMAth.time);
        Color color=basicWeatherController.getSunColor();
        Vector4 direction=basicWeatherController.getSunRayDir();
        color.lerp(new Color(0,0,0,1f),0.7f-direction.w);
        directionalLightEx.setColor(color.r,color.g,color.b,1f);
        directionalLightEx.setDirection(direction.x,direction.y,direction.z);
        shadowLight.set(directionalLightEx);

        skybox.setPosition(StaticBuffer.getPlayerCooordinates());
        if(weatherArea!=null) {
            weatherArea.update();
        }
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
            Gdx.gl.glClearColor(1,1,1,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            float delta=Gdx.graphics.getDeltaTime();
            impactFrames-=delta;
            drawImpact(camera,startColumn,endColumn,startRow,endRow);
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
                basicWeatherController.render();
                basicWeatherController.synchSkybox(skybox);

                skybox.render(camera,modelBatch);
                modelBatch.begin(camera);

                if (StaticBuffer.renderOverride != null) {
                    StaticBuffer.renderOverride.Render(null, null);
                }
                draw(startColumn, endColumn, startRow, endColumn, modelBatch);
                if(weatherArea!=null) {
                    weatherArea.render(modelBatch);
                }
                modelBatch.end();
                additionalRender();
                if(weatherEffector!=null) {
                    weatherEffector.render(StaticBuffer.decalBatch);
                }
                StaticBuffer.damageRenderer.render(StaticBuffer.decalBatch,deltatime,GameCore.camera);
                StaticBuffer.decalBatch.flush();
            }
            frameBuffer.end();
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
        {
            Gdx.gl.glCullFace(GL20.GL_BACK);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        environment.shadowMap=shadowLight;
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);
        frameBuffer.getColorBufferTexture().bind(0);
        {
            toonShader.begin();{
            fullscreenMesh.render(toonShader, GL20.GL_TRIANGLES);
        }
            toonShader.end();
        }
        outlineShader.begin();
        depthBuffer.getTextureAttachments().get(0).bind(0);
        outlineShader.setUniformi("u_depthTexture", 0);
        outlineShader.setUniformf("size", StaticBuffer.screenWidth, screenHeight);
        fullscreenMesh.render(outlineShader,GL20.GL_TRIANGLES);
        outlineShader.end();
        update(startColumn,endColumn,startRow,endRow);
        if(weatherEffector!=null) {
            weatherEffector.update(StaticQuickMAth.move(deltatime));
        }

    }
    protected void drawImpact(PerspectiveCamera camera, int startColumn, int endColumn,int startRow, int endRow){
        frameBuffer.begin();
        {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glCullFace(GL20.GL_BACK);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glDepthMask(true);
            skybox.render(camera,modelBatch);
            modelBatch.begin(camera);
            if (StaticBuffer.renderOverride != null) {
                StaticBuffer.renderOverride.Render(null, null);
            }
            draw(startColumn, endColumn, startRow, endColumn, modelBatch);
            if(weatherArea!=null) {
                weatherArea.render(modelBatch);
            }
            modelBatch.end();
            additionalRender();
            if(weatherEffector!=null) {
                weatherEffector.render(StaticBuffer.decalBatch);
            }
            StaticBuffer.damageRenderer.render(StaticBuffer.decalBatch,deltatime,GameCore.camera);
            StaticBuffer.decalBatch.flush();
        }
        frameBuffer.end();
        frameBuffer.getColorBufferTexture().bind(0);
        {
            impactShader.begin();{
            fullscreenMesh.render(impactShader, GL20.GL_TRIANGLES);
        }
            impactShader.end();
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
            weatherEffector.update(StaticQuickMAth.move(deltatime));
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
    @Override
    public Texture getShadowTexture(){
        return shadowLight.getFrameBuffer().getColorBufferTexture();
    }

    public void drawShadows(PerspectiveCamera camera){
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);
        shadowLight.getCamera().position.set(StaticBuffer.getPlayerCooordinates());
        shadowLight.getCamera().update();
        shadowLight.begin(camera);
        shadowBatch.begin(shadowLight.getCamera());
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);
        for(int m = 0; m<movingObjects2.size; m++) {
            movingObjects2.get(m).render(shadowBatch);
        }
        for(int m = 0; m< staticObjects2.size; m++) {
            staticObjects2.get(m).render(shadowBatch);
        }
        singleObjectMap.render(shadowBatch,environment);
        shadowBatch.end();
        shadowLight.end();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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
        singleObjectMap.render(modelBatch);
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
        singleObjectMap.render(modelBatch,environment);
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
        StaticBuffer.showHitbox.render(hitboxmap.hitboxArray);
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
        return hitboxmap;
    }
    @Override
    public int getUniqueIndex(){
        return mapIndex;
    }
}


