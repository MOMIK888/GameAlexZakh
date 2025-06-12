package com.bestproject.main.Skyboxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.SpriteSheet;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class ColorFulSkybox implements Disposable {
    public Model skybox;
    private Texture skyboxTexture;
    public float transitionTimeMax=2f;
    public static ShaderProgram skyboxShader=null;
    public static ShaderProgram transitionShader=null;
    public Vector3 position=new Vector3();
    public static FrameBuffer buffer=null;
    float transition=0f;
    int currentTime=0;
    float timeSnap=0;
    int prevTime=0;

    public static enum TIMES {
        MORNING(0),
        NIGHT(1),
        RAIN(2),
        SNOW(3);

        private final int value;
        private TIMES(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    private static SpriteSheet nightTexture;
    public ColorFulSkybox(String Path, String TexturePath, float size) {
        if(buffer==null){
            buffer=new FrameBuffer(Pixmap.Format.RGB888,1024,1024,false);
        }
        StaticBuffer.assetManager.load(Path, Model.class);
        StaticBuffer.assetManager.finishLoading();
        skybox =  StaticBuffer.assetManager.get(Path,Model.class);
        skyboxTexture=new Texture(TexturePath);
        nightTexture=new SpriteSheet(new Texture(Gdx.files.internal("Models/Skyboxes/space.jpg")),8,8);
        if(skyboxShader==null) {
            skyboxShader = new ShaderProgram(
                Gdx.files.internal("shaders/skybox2.vert"),
                Gdx.files.internal("shaders/skybox2.frag")
            );

            if (!skyboxShader.isCompiled()) {
                Gdx.app.error("Skybox Shader", skyboxShader.getLog());
            }
        }
    }

    public void render(PerspectiveCamera camera, ModelBatch mb) {
        Gdx.gl.glDepthMask(false);
        float delta = StaticQuickMAth.move(GameCore.deltatime);
        timeSnap += delta/10;
        if (transition > 0) {
            transition-=delta;
            skyboxShader.begin();
            Matrix4 viewMatrix = new Matrix4(camera.view);
            viewMatrix.setTranslation(0, 0, 0);
            skyboxShader.setUniformMatrix("u_view", viewMatrix);
            skyboxShader.setUniformMatrix("u_proj", camera.projection);
            skyboxShader.setUniformi("u_texture", 0);
            buffer.getColorBufferTexture().bind(0);
            skybox.meshes.get(0).render(skyboxShader, GL20.GL_TRIANGLES);
            skyboxShader.end();
            Gdx.gl.glDepthMask(true);
        } else {

            skyboxShader.begin();
            Matrix4 viewMatrix = new Matrix4(camera.view);
            viewMatrix.setTranslation(0, 0, 0);
            skyboxShader.setUniformMatrix("u_view", viewMatrix);
            skyboxShader.setUniformMatrix("u_proj", camera.projection);
            skyboxShader.setUniformi("u_texture", 0);
            buffer.getColorBufferTexture().bind(0);
            skybox.meshes.get(0).render(skyboxShader, GL20.GL_TRIANGLES);
            skyboxShader.end();
            Gdx.gl.glDepthMask(true);
        }
    }
    public void update(){
        if(transition>0){
            TextureRegion currentTexture = getCurrentWeatherTexture();
            TextureRegion nextTexture = getNextWeatherTexture();
            float alpha = 1f - (transition / transitionTimeMax);
            buffer.begin();
            StaticBuffer.spriteBatch.begin();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            StaticBuffer.spriteBatch.setColor(1f, 1f, 1f, 1f - alpha);
            StaticBuffer.spriteBatch.draw(currentTexture, 0, 0, screenWidth, screenHeight);
            StaticBuffer.spriteBatch.setColor(1f, 1f, 1f, alpha);
            StaticBuffer.spriteBatch.draw(nextTexture, 0, 0, screenWidth, screenHeight);
            StaticBuffer.spriteBatch.setColor(1f, 1f, 1f, 1f);
            StaticBuffer.spriteBatch.end();
            buffer.end();
        } else if (currentTime == 1) {
            TextureRegion currentTexture = getNextWeatherTexture();
            timeSnap=timeSnap%nightTexture.getNumFrames();
            buffer.begin();
            StaticBuffer.spriteBatch.begin();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            StaticBuffer.spriteBatch.draw(
                currentTexture,
                0, 0,
                screenWidth, screenHeight);
            StaticBuffer.spriteBatch.end();
            buffer.end();
        }
    }

    private TextureRegion getNextWeatherTexture() {
        if(currentTime==1){
            return nightTexture.getFrame((int)timeSnap);
        } else if(currentTime==0){
            return new TextureRegion(skyboxTexture);
        }
        return null;
    }

    private TextureRegion getCurrentWeatherTexture() {
        if(prevTime==1){
            TextureRegion txt=nightTexture.getFrame((int)timeSnap);
            txt.flip(true,false);
            return txt;
        } else if(prevTime==0){
            return new TextureRegion(skyboxTexture);
        }
        return null;
    }

    public void setCurrentTime(int time){
        if(currentTime!=time) {
            prevTime = currentTime;
            this.currentTime = time;
            transition = transitionTimeMax;
            this.timeSnap = 0;
        }
    }
    @Override
    public void dispose() {
        skybox.dispose();
        skyboxTexture.dispose();
        skyboxShader.dispose();
    }
    public void setPosition(Vector3 position){
        this.position.set(position);
    }
}
