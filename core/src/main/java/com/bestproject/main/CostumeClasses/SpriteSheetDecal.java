package com.bestproject.main.CostumeClasses;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;

public class SpriteSheetDecal implements Disposable {
    private Decal decal;
    private TextureRegion[] frames;
    private float frameDuration;
    private float stateTime;
    private int currentFrame;
    public SpriteSheetDecal(Texture spriteSheet, int frameCols, int frameRows, float frameDuration) {
        this.frameDuration = frameDuration;
        this.stateTime = 0;
        this.currentFrame = 0;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / frameCols,
            spriteSheet.getHeight() / frameRows);
        frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        decal = Decal.newDecal(frames[0], true);
    }
    public void dispose(){
    }
    public SpriteSheetDecal(Texture spriteSheet, int frameCols, int frameRows, float frameDuration, float scale) {
        this.frameDuration = frameDuration;
        this.stateTime = 0;
        this.currentFrame = 0;
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / frameCols,
            spriteSheet.getHeight() / frameRows);
        frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        decal = Decal.newDecal(frames[0], true);
        setScale(scale);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (stateTime >= frameDuration) {
            stateTime = 0;
            currentFrame = (currentFrame + 1) % frames.length;
            decal.setTextureRegion(frames[currentFrame]);
        }
    }
    public void render(){
        Vector3 cameraDirection = GameCore.camera.direction.cpy();
        float yaw = (float) Math.atan2(cameraDirection.x, cameraDirection.z);
        decal.setRotationY(yaw * MathUtils.radiansToDegrees);
        StaticBuffer.decalBatch.add(decal);
    }
    public Decal getDecal() {
        return decal;
    }
    public void setPosition(Vector3 position) {
        decal.setPosition(position);
    }
    public void setRotation(float yaw, float pitch, float roll) {
        decal.setRotation(yaw, pitch, roll);
    }
    public void setStateTime(float time){
        stateTime=time;
        update(0);
    }
    public void setScale(float scale) {
        decal.setScale(scale);
    }
}
