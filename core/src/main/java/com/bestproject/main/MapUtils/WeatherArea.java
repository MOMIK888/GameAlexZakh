package com.bestproject.main.MapUtils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.video.VideoPlayer;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class WeatherArea implements Disposable {
    public static Model weatherarea=null;
    WeatherLayer2[] weatherLayer2s;
    public static ModelInstance modelInstance;
    public static Shader shader;
    public static VideoPlayer videoPlayer;
    public static TextureAttribute diffuse;
    public static float timeStamp=0f;
    public static enum WEATHERPRESETS {
        DEFAULT(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)}),


        FOG(new WeatherLayer2[]{new WeatherLayer2(true),
            new WeatherLayer2(true),
            new WeatherLayer2(true)}),


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
        if(weatherarea==null){
            assetManager.load("Models/Environments/isosphere.g3dj", Model.class);
            assetManager.finishLoading();
            weatherarea=assetManager.get("Models/Environments/isosphere.g3dj");
            modelInstance=new ModelInstance(weatherarea);
            videoPlayer.setLooping(true);
            diffuse=new TextureAttribute(TextureAttribute.Diffuse);
            modelInstance.materials.get(0).set(diffuse);


        }
        weatherLayer2s = WEATHERPRESETS.DEFAULT.getPreset();
    }
    public void update(){
        timeStamp+= StaticQuickMAth.move(GameCore.deltatime);
        if(timeStamp>0.2f){
            timeStamp=0f;
            videoPlayer.update();
            diffuse.set(new TextureRegion(videoPlayer.getTexture()));
        }
    }
    public void render(ModelBatch modelBatch){
        for(int i=0; i<3; i++){
            if(!weatherLayer2s[i].isNull){
                modelInstance.transform.setToScaling(weatherLayer2s[i].deltasize);
                modelInstance.transform.setTranslation(StaticBuffer.getPlayerCooordinates());
                modelInstance.transform.rotate(0f,1f,0f,120f);
                modelBatch.render(modelInstance);
            }
        }
    }
    @Override
    public void dispose() {
        weatherarea.dispose();
    }
    public void switchWeather(int index){
        switch (index){
            case (0):
                videoPlayer.play();
                weatherLayer2s =WEATHERPRESETS.DEFAULT.getPreset();
                break;
            case (1):
                videoPlayer.play();
                weatherLayer2s =WEATHERPRESETS.FOG.getPreset();
                break;
            case (2):
                videoPlayer.play();
                weatherLayer2s =WEATHERPRESETS.RAIN.getPreset();
                break;
            default:
                break;
        }
    }

}
