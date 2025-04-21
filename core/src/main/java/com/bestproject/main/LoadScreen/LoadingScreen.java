package com.bestproject.main.LoadScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.StaticBuffer;
import com.crashinvaders.vfx.effects.VfxEffect;

import jdk.internal.icu.text.NormalizerBase;

public class LoadingScreen implements Disposable {
    protected String[][] loadingMaterialsSorting;
    protected String[] assets;

    protected boolean isLoaded=false;
    protected int currentindexingX=0;
    protected int NumberOfAssets;
    protected float cdAnim=1f;
    public LoadingScreen(String[] assets, AssetManager assetManager, String[][] materials){
        StaticBuffer.setIsLoading(true);
        this.assets=assets.clone();
        if(assets.length>0){
            assetManager.load(assets[currentindexingX], Model.class);
        }
        NumberOfAssets=assets.length;
        loadingMaterialsSorting=materials;

    }
    public void DrawAssets(){

    }
    public void Update(AssetManager assetManager){
        float deltatime= Gdx.graphics.getDeltaTime();
        boolean isFinished=assetManager.update();
        if(!isLoaded && assets.length>0) {
            if (isFinished) {
                currentindexingX += 1;
                updateLoader(currentindexingX);
                if (currentindexingX == assets.length) {
                    isLoaded=true;
                    assetManager.finishLoading();
                    for(String i: loadingMaterialsSorting[0]){
                        StaticBuffer.currentModels.add(assetManager.get(i, Model.class));
                    }
                    for(String i : loadingMaterialsSorting[1]){
                        StaticBuffer.current_enemies.add(assetManager.get(i,Model.class));
                    }
                } else{
                    assetManager.load(assets[currentindexingX],Model.class);
                }
            }
        } else{
            cdAnim-=deltatime;
            if(cdAnim<=0){
                StaticBuffer.setIsLoading(false);
            }
        }
    }
    protected void updateLoader(int progress){

    }

    @Override
    public void dispose() {

    }
}
