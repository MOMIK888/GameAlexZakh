package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureUtils {
    public static Texture createRedFrameTexture(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.fillRectangle(0, 0, width, 2);
        pixmap.fillRectangle(0, height - 2, width, 2);
        pixmap.fillRectangle(0, 0, 2, height);
        pixmap.fillRectangle(width - 2, 0, 2, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    public static Texture createBlueFrameTexture(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 1, 1);
        pixmap.fillRectangle(0, 0, width, 2);
        pixmap.fillRectangle(0, height - 2, width, 2);
        pixmap.fillRectangle(0, 0, 2, height);
        pixmap.fillRectangle(width - 2, 0, 2, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    public static Texture createCirCleTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(width/2,height/2,height/2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
