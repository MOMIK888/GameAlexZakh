package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class FPS extends TextRenderer{
    public FPS(BitmapFont font, String text,Color color, float x, float y) {
        super(font, text, -100, color, x, y);
    }

    @Override
    public void renderPadding(ShapeRenderer shapeRenderer) {
    }

    @Override
    public void renderPaddings(ShapeRenderer shapeRenderer) {
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        setText(String.valueOf(Gdx.graphics.getFramesPerSecond()));
    }
}
