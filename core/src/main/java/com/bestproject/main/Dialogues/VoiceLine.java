package com.bestproject.main.Dialogues;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

import org.w3c.dom.Text;

public class VoiceLine {
    public String[] text;
    public BitmapFont font;
    public boolean isFinished = false;
    public int index = 0;
    public float letterTimer = 0f;
    public int letterIndex = 0;
    protected float letterDelay = 0.05f;
    protected static FrameBuffer frameBuffer;
    protected Texture textTexture;
    protected Decal textDecal;
    protected static SpriteBatch fboBatch;
    public boolean needsUpdate = true;

    public VoiceLine(BitmapFont font, String[] text) {
        this.font = font;
        this.text = text;

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 128, false);
        fboBatch = new SpriteBatch();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void reset() {
        isFinished = false;
        index = 0;
        letterIndex = 0;
        letterTimer = 0f;
        needsUpdate = true;
    }

    public void update(float delta) {
        if (isFinished) return;

        letterTimer += delta;
        if (letterTimer >= letterDelay) {
            letterTimer -= letterDelay;
            letterIndex++;
            if (letterIndex > text[index].length()) {
                letterIndex = text[index].length();
            }
            needsUpdate = true;
        }
    }

    public void nextLine() {
        index++;
        if (index >= text.length) {
            isFinished = true;
            index = text.length - 1;
        } else {
            letterIndex = 0;
            letterTimer = 0f;
            needsUpdate = true;
        }
    }

    private void updateTexture(Texture[] txt, Texture[] emotions) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fboBatch.begin();
        for(int i=0; i<txt.length; i++){
            fboBatch.draw(txt[0],0,StaticBuffer.screenHeight, StaticBuffer.screenWidth,-StaticBuffer.screenHeight);
        }
        String currentText = text[index];
        float x = StaticBuffer.screenWidth/2;
        float y = frameBuffer.getHeight() - 20;

        for (int i = 0; i < letterIndex && i < currentText.length(); i++) {
            char c = currentText.charAt(i);
            float bounce = (float)Math.sin((System.currentTimeMillis() / 100.0 + i * 0.5)) * 3f;
            font.getData().setScale(3f);
            font.getData().flipped=false;
            font.draw(fboBatch, String.valueOf(c), x, y + bounce);
            x += font.getSpaceXadvance() + 2;
        }
        for(int i=0; i<emotions.length; i++){
            fboBatch.draw(emotions[0],0,0,frameBuffer.getWidth(),-frameBuffer.getHeight());
        }

        fboBatch.end();
        frameBuffer.end();
        textTexture = frameBuffer.getColorBufferTexture();
        TextureRegion textureRegion=new TextureRegion(textTexture);
        textureRegion.flip(true,false);
        textDecal = Decal.newDecal(textureRegion, true);
        textDecal.setPosition(0, 0, 0);

        needsUpdate = false;
    }

    public Decal draw(Texture[] textures, Texture[] emotions) {
        if (isFinished) return textDecal;

        if (needsUpdate) {
            updateTexture(textures,emotions);
        }

        if (textDecal != null) {
            return textDecal;
        } else{
            return null;
        }
    }
}
