package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class SpriteSheet {
    private TextureRegion[] frames;
    int cols, rows;
    public SpriteSheet(Texture spriteSheet, int frameCols, int frameRows) {
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
        rows=frameRows;
        cols=frameCols;
    }
    public TextureRegion getFrame(int num){
        return frames[num%frames.length];
    }
    public int getNumFrames(){
        return frames.length;
    }
}
