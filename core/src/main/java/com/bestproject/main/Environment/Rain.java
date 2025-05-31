package com.bestproject.main.Environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MapRender.HitboxAndMappingUtils;
import com.bestproject.main.MapUtils.HeightMap;
import com.bestproject.main.StaticBuffer;

public class Rain extends WeatherEffector  {
    private int NUM_DROPS = 100;
    private float MIN_SPEED = 15f;
    private float MAX_SPEED = 30f;
    private float MIN_HEIGHT = 0f;
    private float MAX_HEIGHT = 15f;


    RainDrop[] raindrops;
    RainDropMidRes[] raindropsMid;
    RainDropLowRes[] raindropsLow;
    Splash[] splashes;
    Texture rainTexture;
    Texture[] rainTextureMid;
    TextureRegion[] midTxtReg;
    Texture[] rainTextureLow;
    TextureRegion[] lowTxtReg;
    Texture splashTexture;
    HeightMap heightMap;
    TextureRegion splash;
    private class Splash {
        Decal decal;
        float lifetime;
        float age = 0;
        boolean life;

        Splash(float x, float y, float z, TextureRegion region) {
            decal = Decal.newDecal(0.1f, 0.1f, region, true);
            decal.setPosition(x, y, z);
            decal.setColor(1f, 1f, 1f, 0.3f);
            lifetime = MathUtils.random(0.2f, 0.3f);
        }

        public boolean update(float delta) {
            age += delta;
            float scale = 0.15f+0.15f*(age/lifetime);
            decal.setDimensions(scale,scale);

            decal.setColor(1f, 1f, 1f, 0.3f * (1f - age / lifetime));

            life= age >= lifetime;
            return life;
        }
        public void reset(int x, int y, int z){
            decal.setPosition(x, y, z);
            decal.setColor(1f, 1f, 1f, 0.3f);
            lifetime = MathUtils.random(0.2f, 0.3f);
            age=0;
            life=false;
        }
        public void reset(Vector3 pos, float h){
            age=0;
            decal.setColor(1f, 1f, 1f, 0.3f);
            lifetime = MathUtils.random(0.2f, 0.3f);
            life=false;
            decal.setPosition(pos.x, h+0.11f, pos.z);
        }

        public void render(DecalBatch batch) {
            if(!life) {
                decal.lookAt(GameCore.camera.position, Vector3.Y);
                batch.add(decal);
            }
        }
    }

    public Rain() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        splashTexture=new Texture(Gdx.files.internal("Images/Effect2d/splash.png"));
        heightMap=HitboxAndMappingUtils.makeHitboxHeightMap(GameEngine.getGameCore().getMap().getHitboxMap().hitboxArray,256);
        pixmap.setColor(Color.WHITE);
        rainTextureMid=new Texture[]{new Texture(Gdx.files.internal("Images/Effect2d/Raindrops/drop1MidRes.png"))};
        midTxtReg=new TextureRegion[rainTextureMid.length];
        for(int i=0; i<midTxtReg.length; i++){
            midTxtReg[i]=new TextureRegion(rainTextureMid[i]);
        }
        rainTextureLow=new Texture[]{new Texture(Gdx.files.internal("Images/Effect2d/Raindrops/lowResDrops1.png"))};
        lowTxtReg=new TextureRegion[rainTextureLow.length];
        for(int i=0; i<lowTxtReg.length; i++){
            lowTxtReg[i]=new TextureRegion(rainTextureLow[i]);
        }
        raindropsMid=new RainDropMidRes[100];
        raindropsLow=new RainDropLowRes[100];
        pixmap.fill();
        splash=new TextureRegion(splashTexture);
        rainTexture = new Texture(pixmap);
        pixmap.dispose();

        raindrops = new RainDrop[NUM_DROPS];
        splashes=new Splash[NUM_DROPS];
        for (int i = 0; i < NUM_DROPS; i++) {
            raindrops[i] = new RainDrop();
            raindrops[i].reset();
        }
        for (int i = 0; i < NUM_DROPS; i++) {
            splashes[i]=new Splash(0,0,0,splash);
        }for (int i = 0; i < raindropsMid.length; i++) {
            raindropsMid[i]=new RainDropMidRes(midTxtReg);
        }
        for (int i = 0; i < raindropsLow.length; i++) {
            raindropsLow[i]=new RainDropLowRes(lowTxtReg);
        }

    }

    private class RainDrop {
        Decal decal;
        float speed;
        float heightLimit;

        RainDrop() {
            decal = Decal.newDecal(0.05f, 0.2f, new TextureRegion(rainTexture), true);
            decal.setColor(1f, 1f, 1f, 0.6f);
        }

        public void reset() {
            float width = 0.02f;
            float height = 0.1f + (float) Math.random() * 0.3f;
            decal.setDimensions(width, height);
            float distance= MathUtils.random(0f,4f);
            float rad=MathUtils.random(0f,6.28f)*50;
            float y=10f;
            float x=(float)Math.cos(rad)*distance+StaticBuffer.getPlayerCooordinates().x;
            float z=(float)Math.sin(rad)*distance+ StaticBuffer.getPlayerCooordinates().z;
            decal.setPosition(x, y, z);
            speed = MIN_SPEED + (float) Math.random() * (MAX_SPEED - MIN_SPEED);
            heightLimit = heightMap.getHeight(x,z);
        }

        public boolean update(float delta) {
            decal.lookAt(GameCore.camera.position, Vector3.Y);
            decal.translate(0, -speed * delta, 0);
            return decal.getPosition().y <= heightLimit;
        }

        public void render(DecalBatch batch) {
            batch.add(decal);
        }
    }
    private class RainDropMidRes {
        Decal decal;
        float speed;
        float heightLimit;

        RainDropMidRes(TextureRegion[] tr) {
            decal = Decal.newDecal(0.05f, 0.2f, tr[MathUtils.random(0,tr.length-1)], true);
            decal.setColor(1f, 1f, 1f, 0.6f);
        }

        public void reset(TextureRegion[] textureRegions) {
            float width = 0.5f+(float)Math.random()/2;
            float height = 0.5f + (float) Math.random()*2;
            decal.setDimensions(width, height);
            decal.setTextureRegion(textureRegions[MathUtils.random(0,textureRegions.length-1)]);
            float distance= MathUtils.random(3f,6f);
            float rad=MathUtils.random(0f,6.28f)*50;
            float y=10f+MathUtils.random(0f,2f);
            float x=(float)Math.cos(rad)*distance+StaticBuffer.getPlayerCooordinates().x;
            float z=(float)Math.sin(rad)*distance+ StaticBuffer.getPlayerCooordinates().z;
            decal.setPosition(x, y, z);
            speed = MIN_SPEED + (float) Math.random() * (MAX_SPEED - MIN_SPEED);
            heightLimit = heightMap.getHeight(x,z);
        }

        public boolean update(float delta) {
            decal.lookAt(GameCore.camera.position, Vector3.Y);
            decal.translate(0, -speed * delta, 0);
            return decal.getPosition().y <= heightLimit;
        }

        public void render(DecalBatch batch) {
            batch.add(decal);
        }
    }
    private class RainDropLowRes {
        Decal decal;
        float speed;
        float heightLimit;

        RainDropLowRes(TextureRegion[] tr) {
            decal = Decal.newDecal(0.4f, 0.8f, tr[MathUtils.random(0,tr.length-1)], true);
            decal.setColor(1f, 1f, 1f, 0.6f);
        }

        public void reset(TextureRegion[] textureRegions) {
            float width = 2f+(float)Math.random();
            float height = 2f + (float) Math.random()*4;
            decal.setDimensions(width, height);
            decal.setTextureRegion(textureRegions[MathUtils.random(0,textureRegions.length-1)]);
            float distance= MathUtils.random(5.6f,12f);
            float rad=MathUtils.random(0f,6.28f)*50;
            float y=10f+MathUtils.random(0f,2f);
            float x=(float)Math.cos(rad)*distance+StaticBuffer.getPlayerCooordinates().x;
            float z=(float)Math.sin(rad)*distance+ StaticBuffer.getPlayerCooordinates().z;
            decal.setPosition(x, y, z);
            speed = MIN_SPEED + (float) Math.random() * (MAX_SPEED - MIN_SPEED);
            heightLimit = heightMap.getHeight(x,z);
        }

        public boolean update(float delta) {
            decal.lookAt(GameCore.camera.position, Vector3.Y);
            decal.translate(0, -speed * delta, 0);
            return decal.getPosition().y <= heightLimit;
        }

        public void render(DecalBatch batch) {
            batch.add(decal);
        }
    }

    @Override
    public void update(float deltaTime) {
        for (int i =0; i<raindrops.length; i++) {
            splashes[i].update(deltaTime);
            if(raindrops[i].update(deltaTime)){
                splashes[i].reset(raindrops[i].decal.getPosition(),raindrops[i].heightLimit);
                raindrops[i].reset();

            }
        }
        for (int i =0; i<raindropsMid.length; i++) {
            if(raindropsMid[i].update(deltaTime)){
                raindropsMid[i].reset(midTxtReg);

            }
        }
        for (int i =0; i<raindropsLow.length; i++) {
            if(raindropsLow[i].update(deltaTime)){
                raindropsLow[i].reset(lowTxtReg);

            }
        }

    }

    @Override
    public void render(DecalBatch batch) {
        for (int i =0; i<raindrops.length; i++) {
            raindrops[i].render(batch);
            splashes[i].render(batch);
        }
        for (int i =0; i<raindropsMid.length; i++) {
            raindropsMid[i].render(batch);
        }
        for (int i =0; i<raindropsLow.length; i++) {
            raindropsLow[i].render(batch);
        }
    }

    @Override
    public void dispose() {
        rainTexture.dispose();
        for(int i=0; i<rainTextureMid.length; i++){
            rainTextureMid[i].dispose();
        }
    }
    @Override
    public HeightMap getHeightMap(){
        return heightMap;
    }
}


