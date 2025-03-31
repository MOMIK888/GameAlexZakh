package com.bestproject.main.CostumeClasses;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;

public class Healthbar {
    private Decal healthBarDecal;
    private Vector3 position;
    private float hp;
    private float maxHp;
    private Color hpColor;
    private Color frameColor;

    public Healthbar(TextureRegion healthBarTexture, TextureRegion frameTexture, float width, float height, float maxHp, Color hpColor, Color frameColor) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.hpColor = hpColor;
        this.frameColor = frameColor;
        healthBarDecal = Decal.newDecal(width, height, healthBarTexture, true);
        healthBarDecal.setColor(hpColor);
        Decal frameDecal = Decal.newDecal(width, height, frameTexture, true);
        frameDecal.setColor(frameColor);
        position = new Vector3();
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        healthBarDecal.setPosition(position);
    }

    public void setHp(float hp) {
        this.hp = Math.min(Math.max(hp, 0), maxHp);
        float scaleX = this.hp / maxHp;
        healthBarDecal.setScaleX(scaleX);
    }

    public void render(DecalBatch decalBatch) {
        healthBarDecal.setColor(hpColor.r,hpColor.g,hpColor.b, 0.8f);
        Vector3 cameraDirection = GameCore.camera.direction.cpy();
        float yaw = (float) Math.atan2(cameraDirection.x, cameraDirection.z);
        healthBarDecal.setRotationY(yaw * MathUtils.radiansToDegrees);
        decalBatch.add(healthBarDecal);
    }

    public void dispose() {
    }
}
