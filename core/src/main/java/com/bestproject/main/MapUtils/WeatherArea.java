package com.bestproject.main.MapUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.SpriteSheet;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class WeatherArea implements Disposable {
    public static Model weatherarea=null;
    static WeatherLayer2[] weatherLayer2s;
    public static ModelInstance[] modelInstance;
    public static Shader shader;
    public static Texture spritesheet;
    public static SpriteSheet spriteSheet;
    public static TextureAttribute diffuse;
    public static ModelBatch weatherBatch;
    public static ShaderProgram shaderProgram;
    public static float timeStamp=0f;
    public static enum WEATHERPRESETS {
        DEFAULT(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)}),


        FOG(new WeatherLayer2[]{new WeatherLayer2(5f,0.2f,new float[]{1,1}),
            new WeatherLayer2(6f,0.6f,new float[]{1,1}),
            new WeatherLayer2(10f,0.9f,new float[]{1,1})}),


        RAIN(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)}),


        SNOW(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)});

        private WeatherLayer2[] value;
        private WEATHERPRESETS(WeatherLayer2[] value) {
            this.value = value;
        }
        public WeatherLayer2[] getPreset() {
            return value;
        }
    }
    public WeatherArea(AssetManager assetManager){
        if(weatherarea==null) {
            assetManager.load("Models/Environments/isosphere.g3dj", Model.class);
            assetManager.finishLoading();
            shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/WeatherShader/vertex.glsl").readString(), Gdx.files.internal("shaders/WeatherShader/fragment.glsl").readString());
            weatherBatch = new ModelBatch(new DefaultShaderProvider() {
                @Override
                protected Shader createShader(Renderable renderable) {
                    return new DefaultShader(renderable, new DefaultShader.Config(), shaderProgram);
                }
            });
            weatherarea = assetManager.get("Models/Environments/isosphere.g3dj");
            modelInstance = new ModelInstance[]{new ModelInstance(weatherarea), new ModelInstance(weatherarea), new ModelInstance(weatherarea)};
            spritesheet = new Texture(Gdx.files.internal("Images/Effect2d/rain.png"));
            spriteSheet = new SpriteSheet(spritesheet, 8, 6);
            diffuse = new TextureAttribute(TextureAttribute.Diffuse, spriteSheet.getFrame(0));
            for (int i = 0; i < 3; i++) {
                modelInstance[i].materials.get(0).set(diffuse);
                modelInstance[i].materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            }


            weatherLayer2s = WEATHERPRESETS.FOG.getPreset();
            for (int i = 0; i < 3; i++) {
                modelInstance[i].transform.scale(weatherLayer2s[i].deltasize.x, weatherLayer2s[i].deltasize.y, weatherLayer2s[i].deltasize.z);
                modelInstance[i].transform.rotate(0f, 1f, 0f, 120 * i);
            }
        }
    }
    public void update(){
        timeStamp+= StaticQuickMAth.move(GameCore.deltatime);
        diffuse.set(spriteSheet.getFrame((int)(timeStamp/0.05f)));
    }
    public void render(ModelBatch modelBatch){
        if(weatherarea!=null) {
            weatherBatch.begin(modelBatch.getCamera());
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            for (int i = 2; i >= 0; i--) {
                if (!weatherLayer2s[i].isNull) {
                    Vector3 translation = new Vector3();
                    modelInstance[i].transform.setTranslation(StaticBuffer.getPlayerCooordinates());
                    if (modelInstance[i].materials.get(0).get(BlendingAttribute.class, BlendingAttribute.Type) != null) {
                        modelInstance[i].materials.get(0).get(BlendingAttribute.class, BlendingAttribute.Type).opacity = weatherLayer2s[i].alpha;
                    }
                    TextureRegion region = spriteSheet.getFrame((int) (timeStamp / 0.05f));
                    diffuse.textureDescription.texture = region.getTexture();
                    float u = region.getU();
                    float v = region.getV();
                    float u2 = region.getU2();
                    float v2 = region.getV2();
                    Vector2 uvOffset = new Vector2(u, v);
                    Vector2 uvScale = new Vector2(u2 - u, v2 - v);
                    shaderProgram.begin();
                    shaderProgram.setUniformf("u_uvOffset", uvOffset);
                    shaderProgram.setUniformf("u_uvScale", uvScale);
                    shaderProgram.setUniformf("u_stretchUVX", 5f);
                    shaderProgram.setUniformf("u_stretchUVY", 1f);
                    shaderProgram.setUniformf("u_opacity", weatherLayer2s[i].alpha);
                    shaderProgram.setUniformi("u_texture", 50);
                    spritesheet.bind(50);
                    shaderProgram.end();
                    weatherBatch.render(modelInstance[i]);
                }
            }
            weatherBatch.end();
        }
    }
    @Override
    public void dispose() {

    }
    public void switchWeather(int index){
        switch (index){
            case (0):
                weatherLayer2s =WEATHERPRESETS.DEFAULT.getPreset();
                break;
            case (1):
                weatherLayer2s =WEATHERPRESETS.FOG.getPreset();
                break;
            case (2):
                weatherLayer2s =WEATHERPRESETS.RAIN.getPreset();
                break;
            default:
                break;
        }
    }

}
