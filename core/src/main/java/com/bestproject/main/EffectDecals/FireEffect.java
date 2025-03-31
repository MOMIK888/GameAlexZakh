package com.bestproject.main.EffectDecals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticQuickMAth;

public class FireEffect extends Effect {
    private final Array<FireParticle> decals;
    public static final Texture fireTexture = generateFireTexture();;
    private final Vector3 position;
    private float emissionArea;
    int initialialcap=40;

    public FireEffect(float length, Vector3 position, float emissionArea, int cap) {
        super(length);
        this.position = position;
        this.emissionArea = emissionArea;
        this.decals = new Array<>();
        initialialcap=cap;

        generateDecals();
    }
    public void setEmissionArea(float value){
        this.emissionArea=value;
    }
    public void setCapacity(int value){
        if(initialialcap<value){
            for(int i=0; i<value-initialialcap; i++){
                generateDecal();
            }
        }
        this.initialialcap=value;

    }


    private static Texture generateFireTexture() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 0.5f, 0, 1)); // Fire-like orange color
        pixmap.fillRectangle(0, 0, 16, 16);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void generateDecals() {
        for (int i = 0; i < 40; i++) {
            Decal decal = Decal.newDecal(MathUtils.random(0.05f,0.1f), MathUtils.random(0.05f,0.1f), new TextureRegion(fireTexture));
            resetDecal(decal);
            decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            decals.add(new FireParticle(decal,position.y+MathUtils.random(0.2f,1f),MathUtils.random(1f,2f)));
        }
    }
    private void generateDecal() {
            Decal decal = Decal.newDecal(MathUtils.random(0.05f,0.1f), MathUtils.random(0.05f,0.1f), new TextureRegion(fireTexture));
            resetDecal(decal);
            decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            decals.add(new FireParticle(decal,position.y+MathUtils.random(0.2f,1f),MathUtils.random(0.5f,1f)));
    }

    private void resetDecal(Decal decal) {
        float x = position.x + (float) (Math.random() * emissionArea - emissionArea / 2);
        float y = position.y;
        float z = position.z + (float) (Math.random() * emissionArea - emissionArea / 2);
        decal.setPosition(x, y, z);
        decal.setColor(new Color(1, (float) Math.random() * 0.5f + 0.5f, 0, 1));
        decal.setRotationX((float) (Math.random() * 360));
        decal.setRotationY((float) (Math.random() * 360));
        decal.setScale((float) (Math.random() * 0.5 + 0.5));
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        for (FireParticle decal : decals) {
            decal.update(delta);

        }
        for(int i=0; i<decals.size; i++){
            if(decals.get(i).expiration()){
                decals.removeIndex(i);
                i--;
                generateDecal();
            }
        }
    }
    @Override
    public void render(DecalBatch batch) {
        update(StaticQuickMAth.move(GameCore.deltatime));
        for (FireParticle decal : decals){
            if(length<0.3f){
                Color clor=decal.decal.getColor();
                clor.a=length/0.3f;
                decal.decal.setColor(clor);
            }
            batch.add(decal.decal);
        }
    }

    @Override
    protected void expire() {
        decals.clear();
    }
}
class FireParticle{
    Decal decal;
    float endPosition;
    float speed;
    public FireParticle(Decal decal, float endPosition, float speed){
        this.decal=decal;
        this.endPosition=endPosition;
        this.speed=speed;
    }
    public void update(float delta){
        decal.setY(delta * (float) (speed) + decal.getPosition().y);
        decal.rotateX((float) (Math.random() * 120 - 2.5));
        decal.rotateY((float) (Math.random() * 120 - 2.5));

    }
    public boolean expiration(){
        return decal.getPosition().y>=endPosition;
    }

}
