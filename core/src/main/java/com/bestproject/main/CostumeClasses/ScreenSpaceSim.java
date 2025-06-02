package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface ScreenSpaceSim {
    void render(ShapeRenderer shapeRenderer);
    void render(SpriteBatch spriteBatch);
    void update(float delta);
    boolean expire();

}
