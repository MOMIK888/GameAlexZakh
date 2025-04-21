package com.bestproject.main.MapUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.video.VideoPlayer;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class WeatherAreaTest implements Disposable {
    public static Model weatherarea=null;
    WeatherLayer2[] weatherLayer2s;
    public static ModelInstance[] modelInstance;
    public static Shader shader;
    public static Texture videoPlayer;
    public static TextureAttribute diffuse;
    public static float timeStamp=0f;
    public static enum WEATHERPRESETS {
        DEFAULT(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)}),


        FOG(new WeatherLayer2[]{new WeatherLayer2(1f,0.2f,new float[]{1,1}),
            new WeatherLayer2(4f,0.6f,new float[]{1,1}),
            new WeatherLayer2(18f,0.9f,new float[]{1,1})}),


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
    public WeatherAreaTest(AssetManager assetManager){
        if(weatherarea==null){
            assetManager.load("Models/Environments/isosphere.g3dj", Model.class);
            assetManager.finishLoading();
            weatherarea=assetManager.get("Models/Environments/isosphere.g3dj");
            modelInstance=new ModelInstance[]{new ModelInstance(weatherarea),new ModelInstance(weatherarea),new ModelInstance(weatherarea)};
            videoPlayer=new Texture(Gdx.files.internal("TemporaryAssetDump/shades_mr_perswall.jpg"));
            diffuse=new TextureAttribute(TextureAttribute.Diffuse, videoPlayer);
            for(int i=0; i<3; i++) {
                modelInstance[i].materials.get(0).set(new TextureAttribute(TextureAttribute.Diffuse, videoPlayer));
                modelInstance[i].materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            }



        }
        weatherLayer2s = WEATHERPRESETS.FOG.getPreset();
        for(int i=0; i<3; i++) {
            modelInstance[i].transform.scale(weatherLayer2s[i].deltasize.x,weatherLayer2s[i].deltasize.y,weatherLayer2s[i].deltasize.z);
            modelInstance[i].transform.rotate(0f,1f,0f,120*i);
        }
    }
    public void update(){
        timeStamp+= StaticQuickMAth.move(GameCore.deltatime);
        if(timeStamp>0.2f){
            timeStamp=0f;
            diffuse.set(new TextureRegion(videoPlayer));
        }
    }
    public void render(ModelBatch modelBatch){
        for(int i=2; i>=0; i--){
            if(!weatherLayer2s[i].isNull){
                Vector3 translation=new Vector3();
                modelInstance[i].transform.setTranslation(StaticBuffer.getPlayerCooordinates());
                if( modelInstance[i].materials.get(0).get(BlendingAttribute.class, BlendingAttribute.Type)!=null) {
                    modelInstance[i].materials.get(0).get(BlendingAttribute.class, BlendingAttribute.Type).opacity = weatherLayer2s[i].alpha;
                }
                modelBatch.render(modelInstance[i]);
            }
        }
    }
    @Override
    public void dispose() {
        weatherarea.dispose();
        videoPlayer.dispose();

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
class WeatherLayer2 {
    Vector3 deltasize;
    float alpha=1f;
    float[] UVStretch;
    boolean isNull=false;
    public WeatherLayer2(float deltasize, float alpha, float[] UVStretch){
        this.deltasize=new Vector3(deltasize,deltasize,deltasize);
        this.alpha=alpha;
        this.UVStretch=UVStretch.clone();
    }
    public WeatherLayer2(boolean isNull){
        this.isNull=isNull;
    }
}
