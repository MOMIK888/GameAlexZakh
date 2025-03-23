package com.bestproject.main;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    private Joystick joystick;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private ShapeRenderer shapeRenderer;
    private PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    private float speed_rotation=0.4f;
    private DirectionalLightEx light;
    private CameraRotation cameraRotation;

    @Override
    public void create() {
        cameraRotation=new CameraRotation();
        shapeRenderer=new ShapeRenderer();
        // create scene
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("Models/cubic.gltf"));
        scene = new Scene(sceneAsset.scene);
        sceneManager = new SceneManager();
        sceneManager.addScene(scene);

        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        camera = new PerspectiveCamera(60f,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        joystick=new Joystick(1000, 100, 100);
        float d = .02f;
        camera.near = d / 1000f;
        camera.far = 200;
        sceneManager.setCamera(camera);

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        if (joystick.isTouched()) {
            Vector2 direction = joystick.getDirection();
            Vector3 forward = new Vector3(camera.direction.x, 0, camera.direction.z).nor();

// Get the camera's right direction (ignoring the Y-axis)
            Vector3 right = new Vector3(forward).crs(Vector3.Y).nor();

// Calculate the movement direction based on joystick input
            Vector3 movement = new Vector3();
            movement.add(forward.scl(direction.y * 1f * Gdx.graphics.getDeltaTime())); // Forward/backward
            movement.add(right.scl(direction.x * 1f * Gdx.graphics.getDeltaTime())); // Left/right

// Translate the camera
            camera.translate(movement);
        }
        setRotation(cameraRotation.getVectors());
        camera.update();
        sceneManager.render();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        joystick.draw(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
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
        if (joystick.contains(screenX, Gdx.graphics.getHeight() - screenY)) {
            joystick.setTouched(true);
            joystick.update(screenX, Gdx.graphics.getHeight() - screenY);
            joystick.setIndex(pointer);
            return true;
        } else{
            cameraRotation.setCurrent_pointer(pointer);
            cameraRotation.startTouch(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        joystick.setTouched(false);
        if(pointer==cameraRotation.getCurrent_pointer()){
            cameraRotation.up();
        } if(pointer==joystick.getIndex()){
            joystick.resetIndex();
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (joystick.isTouched() && pointer==joystick.getIndex()) {
            joystick.update(screenX, Gdx.graphics.getHeight() - screenY);
            return true;
        } else if (pointer == cameraRotation.getCurrent_pointer()) {
            System.out.println(screenX);
            cameraRotation.drag(screenX,screenY);
            return true;
        }

        return false;
    }
    public void setRotation(float[] delta){
        camera.rotate(Vector3.Y, -delta[0] * speed_rotation);
        camera.rotate(camera.direction.cpy().crs(Vector3.Y).nor(), -delta[1] * speed_rotation);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
