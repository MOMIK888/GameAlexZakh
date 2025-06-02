package com.bestproject.main.Game;

import static com.bestproject.main.StaticBuffer.screenHeight;
import static com.bestproject.main.StaticBuffer.screenWidth;
import static com.bestproject.main.StaticBuffer.spriteBatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CameraRotation;
import com.bestproject.main.CharacterUtils.DialogueScreen;
import com.bestproject.main.CharacterUtils.MapQuickTravel;
import com.bestproject.main.CostumeClasses.Click;
import com.bestproject.main.CostumeClasses.CostumeShader;
import com.bestproject.main.CostumeClasses.CostumeShaderProvider;
import com.bestproject.main.CostumeClasses.FPS;
import com.bestproject.main.CostumeClasses.ScreenSpaceSim;
import com.bestproject.main.Joystick;
import com.bestproject.main.LoadScreen.FirstLoadingScreen;
import com.bestproject.main.LoadScreen.LoadingScreen;
import com.bestproject.main.Maps.BossArena;
import com.bestproject.main.Maps.Dungeon;
import com.bestproject.main.Maps.Map;
import com.bestproject.main.Maps.MapTest;
import com.bestproject.main.Maps.Tavern;
import com.bestproject.main.Maps.Village;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.ScreenEmulator;
import com.bestproject.main.Skyboxes.Skybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.StaticShaders;
import com.bestproject.main.Tiles.StoneTile;

import java.awt.Point;

public class GameCore implements Disposable, InputProcessor {
    public static PerspectiveCamera camera=new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());;
    public static float cameraRoationm=90;
    public static float cameraRoationX=0;
    public static Click lastclick=new Click();
    public float DecalButtonTiring=0.0f;
    MapQuickTravel mapQuickTravel=new MapQuickTravel();
    public static ScreenSpaceSim screenSpaceSim=null;
    private ShapeRenderer shapeRenderer;
    static Vector3 additionalCooordinates=new Vector3();
    public boolean displayQuickTravel=false;
    public Array<ScreenEmulator> scm=new Array<>();
    Joystick joystick;
    public static float deltatime=0;
    static CameraRotation cameraRotation=new CameraRotation();
    static CameraController cameraController=new CameraController();
    public FirstPersonCameraController fps;
    public static int TemporaryMapBuffer=-1;
    int Screenheight=Gdx.graphics.getHeight();
    public LoadingScreen loadingScreen;
    static boolean isStarting=false;
    Map map;
    public GameCore() {
        StaticShaders.init();
        shapeRenderer=new ShapeRenderer();
        joystick=new Joystick(150f,150f,160f);
        camera.translate(0,1f,0);
        camera.fieldOfView = 60;
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.near = 0.1f;
        camera.far = 1000f;
        camera.update();
        Gdx.input.setInputProcessor(this);
        camera.rotate(90,0f,1f,0f);
        map=null;
        StaticBuffer.effectBuffer.update();
        StaticBuffer.textRenderer.add(new FPS(StaticBuffer.fonts[0],"", Color.BLACK,Gdx.graphics.getWidth()-200,1000 ));
    }
    public void setInput(){
        Gdx.input.setInputProcessor(this);
    }
    public boolean render() {
        if (isStarting){
            isStarting=false;
            DialogueScreen dialogueScreen=new DialogueScreen(new String[]{"... ", "Не могу поверить они отправили меня сюда...", "Я, конечно, слышал о том, что экзамены по магии надо сдавать вдалеке от дома.", "Но это просто смешно!", "ТЫСЯЧА километров ради того, чтобы приехать в какою-то деревню...","Ладно, время выдвигаться и наконец покончить с этим как можно быстрее..."});
            dialogueScreen.afterInit();
            screenSpaceSim=dialogueScreen;

        }

        deltatime=Gdx.graphics.getDeltaTime();
        StaticBuffer.questManager.update();
        if(StaticBuffer.isIsLoading()){
            loadingScreen.Update(StaticBuffer.assetManager);
            loadingScreen.DrawAssets();
            if(!StaticBuffer.isIsLoading()){
                map.MapInitialization();
                loadingScreen.dispose();
            }
            return false;
        }
        update();
        cameraRotation.update();
        if(DecalButtonTiring>=0){
            DecalButtonTiring-=deltatime;
        }
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        if(StaticBuffer.isCreative){
            map.RenderCreative(camera);
            float[] val=cameraRotation.getAngles(cameraRotation.getVectors());
            camera.rotate(-val[0],0,1f,0f);
            camera.rotate(-val[1],0f,0f,1f);
            camera.normalizeUp();
            Vector3 newpos=new Vector3(camera.position);
            Vector3 pickRay=camera.getPickRay(StaticBuffer.screenWidth/2f,StaticBuffer.screenHeight/2).direction;
            Vector2 jval=joystick.getDIrectionCreative();
            float speed=4f;
            newpos=newpos.add(pickRay.x*jval.y*deltatime*3*speed,pickRay.y*jval.y*deltatime*3*speed,pickRay.z*jval.y*deltatime*3*speed);
            StaticBuffer.creativeMode.draw();
            fps.update(0.01f);
            camera.position.set(newpos);
        } else{
            if(!displayQuickTravel) {
                map.render(camera);
            }
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(screenSpaceSim!=null){
            screenSpaceSim.render(shapeRenderer);

        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        joystick.draw(shapeRenderer);
        shapeRenderer.end();
        StaticBuffer.ui.draw(StaticBuffer.spriteBatch, StaticBuffer.TestShapeRenderer);
        StaticBuffer.effectBuffer.render(StaticBuffer.decalBatch);
        if(displayQuickTravel){
            mapQuickTravel.update(deltatime);
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            shapeRenderer.setAutoShapeType(true);
            StaticBuffer.spriteBatch.begin();
            mapQuickTravel.draw(StaticBuffer.spriteBatch);
            StaticBuffer.spriteBatch.end();
            shapeRenderer.end();

        }
        if(TemporaryMapBuffer!=-1){
            if(TemporaryMapBuffer==0){
                map=new Tavern();
            } else if(TemporaryMapBuffer==1){
                if(map.getUniqueIndex()!=0) {
                    map = new Village();
                }
            } else if(TemporaryMapBuffer==2){
                if(map.getUniqueIndex()!=3) {
                    map = new BossArena();
                }
            }
            TemporaryMapBuffer=-1;
        }
        StaticBuffer.spriteBatch.begin();
        if(screenSpaceSim!=null){
            screenSpaceSim.render(spriteBatch);
            screenSpaceSim.update(deltatime);
            if(screenSpaceSim.expire()){
                screenSpaceSim=null;
            }

        }
        StaticBuffer.spriteBatch.end();
        return true;
    }
    public void update(){
        camera.update();
        StaticBuffer.ui.update();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        if(!StaticBuffer.isIsLoading()) {
            map.dispose();
        }
        StaticBuffer.disposeAll();
        StaticShaders.disposeAll();
        if(loadingScreen!=null){
            loadingScreen.dispose();
        }
    }
    public void setLoadingScreen(LoadingScreen loadingScreen){
        this.loadingScreen=loadingScreen;
    }
    public static void roTateCameraAroundPivot(){
        float[] values=cameraRotation.getAngles(cameraRotation.getVectors());
        values[0]*=-1;
        values[1]*=-1;
        values[1]=MathUtils.clamp(values[1],-90-cameraRoationX,90-cameraRoationX);
        cameraRoationm+=values[0];
        cameraRoationX+=values[1];
        additionalCooordinates.y-=0.5f;
        additionalCooordinates=StaticQuickMAth.RotateAroundPivot(additionalCooordinates,StaticBuffer.getPlayerCooordinates(), new Vector3(1,0,0),values[0]+cameraRoationm);
        additionalCooordinates=StaticQuickMAth.RotateAroundPivotX(additionalCooordinates,StaticBuffer.getPlayerCooordinates(),values[1]+cameraRoationX);
        camera.position.set(additionalCooordinates);
        camera.lookAt(StaticBuffer.getPlayerCooordinates());
        Vector3 direction = new Vector3();
        direction.set(StaticBuffer.getPlayerCooordinates()).sub(camera.position).nor();

        Vector3 right = direction.cpy().crs(Vector3.Y).nor();
        Vector3 up = right.cpy().crs(direction).nor();

        camera.up.set(up);
        lastclick.update();


    }
    public Joystick getJoystick(){
        return joystick;
    }
    public static void setCameraCoordinatesPlayer(float x,float y, float z){
        additionalCooordinates.set(x,y,z);
        roTateCameraAroundPivot();
    }
    public float getDeltatime(){
        return deltatime;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(StaticBuffer.isCreative){
            StaticBuffer.creativeMode.onTouch(pointer,screenX,Gdx.graphics.getHeight()-screenY);
        }
        if(scm.size>0){
            for(int i=0; i<scm.size; i++){
                scm.get(i).OnTouch(screenX,Gdx.graphics.getHeight()-screenY,pointer);
            }
            scm.clear();
        }
        else if(joystick.contains(screenX,Screenheight-screenY)){
            joystick.update(screenX,Screenheight-screenY);
            joystick.setIndex(pointer);
            joystick.setTouched(true);
            DecalButtonTiring=1f;
        } else if(StaticBuffer.ui.onTouch(screenX,Gdx.graphics.getHeight()-screenY,pointer)){
            return true;
        } else{
            if(!StaticBuffer.isCreative) {
                cameraRotation.startTouch(screenX, Screenheight - screenY);
                cameraRotation.setCurrent_pointer(pointer);
            } else{
                fps.touchDown(screenX,screenY,pointer,button);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(joystick.getIndex()==pointer){
            joystick.resetIndex();
            joystick.setTouched(false);
        } if(cameraRotation.getCurrent_pointer()==pointer){
            cameraRotation.up();
        }
        for(int i=0; i<scm.size; i++){
            scm.get(i).OnUp(screenX,Gdx.graphics.getHeight()-screenY,pointer);
        }
        StaticBuffer.ui.onUp(screenX,Gdx.graphics.getHeight()-screenY,pointer);
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(joystick.getIndex()==pointer){
            joystick.update(screenX,Gdx.graphics.getHeight()-screenY);
            DecalButtonTiring=1f;
        } else if(cameraRotation.getCurrent_pointer()==pointer){
            cameraRotation.drag(screenX,Screenheight-screenY);
        }
        if(StaticBuffer.isCreative){
            fps.touchDragged(screenX,screenY,pointer);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    public Map getMap(){
        return  map;
    }
    public void setMap(Map map){
        this.map=map;
    }
    public void setTemporaryMapBuffer(int mapIndex){
        TemporaryMapBuffer=mapIndex;
    }
    public static void setStartingCutscene(){
        isStarting=true;
    }
}
class CameraController {
    private float currentYRotation = 0;
    public CameraController() {
    }
    public float rotateCameraAroundPlayer(PerspectiveCamera camera, Vector3 playerPosition, float deltaYRotation) {
        float newYRotation = currentYRotation + deltaYRotation;
        newYRotation = Math.max(-90, Math.min(90, newYRotation));
        float actualDeltaYRotation = newYRotation - currentYRotation;
        currentYRotation = newYRotation;
        camera.position.set(playerPosition);
        camera.rotateAround(playerPosition, Vector3.Y, actualDeltaYRotation);
        camera.lookAt(playerPosition);
        camera.update();

        return actualDeltaYRotation;
    }

    public float getCurrentYRotation() {
        return currentYRotation;
    }

}


