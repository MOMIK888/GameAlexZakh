package com.bestproject.main.Dialogues;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;

public class DialogueDecal {
    private final BitmapFont font;
    private final ObjectMap<Character, TextureRegion> glyphs = new ObjectMap<>();

    private final Array<Decal> textDecals = new Array<>();
    private final Texture[] faceTextures;
    private Decal faceDecal;

    private Vector3 position = new Vector3();
    private float clickcd = 0f;
    private String currentLine = "";
    private float spacing = 0.08f;

    public enum Emotions {
        DEFAULT(0),
        EXCLAMATION(1),
        QUESTION(2),
        EMBARRASSMENT(3);

        private final int value;
        Emotions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public DialogueDecal(BitmapFont font, Texture[] faceTextures, String text) {
        this.font = font;
        this.faceTextures = faceTextures;
        extractGlyphs();
        setText(text);
    }

    private void extractGlyphs() {
        BitmapFont.BitmapFontData data = font.getData();
        TextureRegion region = font.getRegion();

        for (char c = 0; c < Character.MAX_VALUE; c++) {
            BitmapFont.Glyph glyph = data.getGlyph(c);
            if (glyph != null) {
                TextureRegion tr = new TextureRegion(region.getTexture(), glyph.srcX, glyph.srcY, glyph.width, glyph.height);
                tr.flip(false, false);
                glyphs.put(c, tr);
            }
        }
    }


    public void setText(String message) {
        textDecals.clear();
        currentLine = message;

        char[] chars = message.toCharArray();
        float xOffset = -(chars.length * spacing * 0.5f);
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            TextureRegion tr = glyphs.get(c);
            if (tr == null) continue;
            Decal d = Decal.newDecal(tr, true);
            d.setScale(0.01f);
            d.setPosition(position.x + xOffset + i * spacing, position.y, position.z);
            textDecals.add(d);
        }
    }

    public void render(DecalBatch batch, Camera camera) {
        Vector3 camPos = camera.position;
        Vector3 perpendicularVector=new Vector3(camera.direction).rotate(90,0f,1f,0f);

        for (int i = 0; i < textDecals.size; i++) {
            Decal d = textDecals.get(i);
            d.lookAt(camPos, Vector3.Y);
            d.setPosition(
                position.x + (i - textDecals.size / 2f) * -perpendicularVector.x*0.2f,
                position.y,
                position.z+(i - textDecals.size / 2f)*-perpendicularVector.z*0.2f
            );
            batch.add(d);
        }
    }

    public void update(float deltaTime) {
        clickcd -= deltaTime;
        if(GameEngine.gameCore.DecalButtonTiring<0) {
            if (isClicked(GameCore.camera.getPickRay(GameCore.lastclick.getX(), GameCore.lastclick.getY()))) {
                clickcd = 0.1f;
                GameCore.lastclick.invalidate();
                GameEngine.gameCore.DecalButtonTiring=1;
            }
            ;
        }
    }
    public void setZroClick(){
        clickcd=-4;
    }

    public boolean isClicked(Ray ray) {
        BoundingBox aabb = new BoundingBox(
            new Vector3(position.x - 0.5f, position.y - 0.2f, position.z - 0.1f),
            new Vector3(position.x + 0.5f, position.y + 0.2f, position.z + 0.1f)
        );
        return Intersector.intersectRayBoundsFast(ray, aabb);
    }

    public void setPosition(Vector3 pos) {
        this.position.set(pos);
        setText(currentLine);
    }

    public void setEmotion(Emotions emotion) {
        int index = emotion.getValue();
    }
    public boolean getClick(){
        return clickcd>0;
    }
}

