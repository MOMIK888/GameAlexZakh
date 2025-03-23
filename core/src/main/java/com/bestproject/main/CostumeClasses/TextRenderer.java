package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class TextRenderer implements Disposable {
    private BitmapFont font;
    private char[] text;
    private float x, y;
    int unique_index;
    Color col1;
    public boolean ispressed;
    public TextRenderer(BitmapFont font, String text, int index, Color color, float x, float y) {
        this.font = font;
        this.text = text.toCharArray();
        this.x = x;
        this.y = y;
        unique_index=index;
        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
        font.setUseIntegerPositions(false);
        col1=color;
    }
    public void render(SpriteBatch batch) {
        float textHeight = font.getCapHeight();
        batch.setColor(Color.WHITE);
        float padding = 10;
        float renderY = y - textHeight - padding;
        Matrix4 originalTransform = batch.getTransformMatrix().cpy();
        for (int i = 0; i < text.length; i++) {
            float charWidth = font.getCapHeight(); //character width may change
            float charX = x + (i * charWidth);
            Matrix4 transform = new Matrix4(originalTransform);
            transform.translate(charX + charWidth / 2, renderY + textHeight / 2, 0);
            transform.rotate(0, 0, 1, MathUtils.random(-3, 3));
            transform.translate(-(charX + charWidth / 2), -(renderY + textHeight / 2), 0);
            batch.setTransformMatrix(transform);
            font.draw(batch, String.valueOf(text[i]), charX, renderY);
        }
        batch.setTransformMatrix(originalTransform);
    }
    public void renderNoShaking(SpriteBatch batch, float size) {
        font.getData().setScale(size);
        float textHeight = font.getCapHeight();
        batch.setColor(Color.WHITE);
        float padding = 10;
        float renderY = y - textHeight - padding;
        Matrix4 originalTransform = batch.getTransformMatrix().cpy();
        for (int i = 0; i < text.length; i++) {
            float charWidth = font.getCapHeight(); //character width may change
            float charX = x + (i * charWidth);
            font.draw(batch, String.valueOf(text[i]), charX, renderY);
        }
    }

    public int getIndex(){
        return unique_index;
    }
    public void setText(String text) {
        this.text = text.toCharArray();
    }
    public void renderPadding(ShapeRenderer shapeRenderer){
        float textHeight = font.getCapHeight()*1.3f;
        float padding = 10;
        float renderY = y - textHeight*1.8f - padding;
        float width=font.getCapHeight()*text.length*1.2f;
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        float charX=x;
        float offsetX1 = MathUtils.random(-15, 15);
        float offsetY1 = MathUtils.random(-15, 15);
        float offsetX2 = MathUtils.random(-15, 15);
        float offsetY2 = MathUtils.random(-15, 15);
        float offsetX3 = MathUtils.random(-15, 15);
        float offsetY3 = MathUtils.random(-15, 15);
        float offsetX4 = MathUtils.random(-15, 15);
        float offsetY4 = MathUtils.random(-15, 15);
        Vector2 v1 = new Vector2(charX + offsetX1, renderY + offsetY1);
        Vector2 v2 = new Vector2(charX + width + offsetX2, renderY + offsetY2);
        Vector2 v3 = new Vector2(charX + width + offsetX3, renderY + textHeight + offsetY3);
        Vector2 v4 = new Vector2(charX + offsetX4, renderY + textHeight + offsetY4);
        Color c1=Color.GRAY;
        c1.a=0.6f;
        shapeRenderer.setColor(c1);
        shapeRenderer.triangle(v1.x-MathUtils.random(-15, 15), v1.y-MathUtils.random(-15, 15), v2.x-MathUtils.random(-15, 15), v2.y+MathUtils.random(-15, 15), v3.x+MathUtils.random(-15, 15), v3.y+MathUtils.random(-15, 15));
        shapeRenderer.triangle(v1.x-MathUtils.random(-15, 15), v1.y-MathUtils.random(-15, 15), v3.x-MathUtils.random(-15, 15), v3.y+MathUtils.random(-15, 15), v4.x+MathUtils.random(-15, 15), v4.y-MathUtils.random(-15, 15));
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.triangle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
        shapeRenderer.triangle(v1.x, v1.y, v3.x, v3.y, v4.x, v4.y);
    }
    public void renderPaddings(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(col1);

        float textHeight = font.getCapHeight()*1.3f;
        float padding = 10;
        float renderY = y - textHeight*1.8f - padding;
        shapeRenderer.setColor(Color.SCARLET);
        for (int i = 0; i < text.length; i++) {
            float charWidth = font.getCapHeight();
            float charX = x + (i * charWidth);
            float offsetX1 = MathUtils.random(-8, 8);
            float offsetY1 = MathUtils.random(-8, 8);
            float offsetX2 = MathUtils.random(-8, 8);
            float offsetY2 = MathUtils.random(-8, 8);
            float offsetX3 = MathUtils.random(-8, 8);
            float offsetY3 = MathUtils.random(-8, 8);
            float offsetX4 = MathUtils.random(-8, 8);
            float offsetY4 = MathUtils.random(-8, 8);
            Vector2 v1 = new Vector2(charX + offsetX1, renderY + offsetY1);
            Vector2 v2 = new Vector2(charX + charWidth + offsetX2, renderY + offsetY2);
            Vector2 v3 = new Vector2(charX + charWidth + offsetX3, renderY + textHeight + offsetY3);
            Vector2 v4 = new Vector2(charX + offsetX4, renderY + textHeight + offsetY4);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.triangle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
            shapeRenderer.triangle(v1.x, v1.y, v3.x, v3.y, v4.x, v4.y);
        }
    }
    @Override
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
    public boolean OnTouch(float touchx, float touchy){
        if(x-30<touchx && x<touchx+font.getCapHeight()*text.length){
            if(y-50<touchy && y<touchy+font.getCapHeight()*1.1f){
                ispressed=true;
                return true;
            }
        }
        return false;
    }
}

