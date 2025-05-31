package com.bestproject.main.FightUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class DamageRenderer {
    private final DigitTextures digitTextures;
    private final Array<DamageParticle> particles = new Array<>();

    public DamageRenderer(BitmapFont font) {
        this.digitTextures = new DigitTextures(font);
    }

    public void showDamage(Vector3 position, float damage, Color color) {
        int value = Math.round(damage);
        char[] chars = String.valueOf(value).toCharArray();

        Array<Decal> decals = new Array<>();
        float spacing = 5f;

        for (int i = 0; i < chars.length; i++) {
            int digit = chars[i] - '0';
            TextureRegion region = digitTextures.digits[digit];
            Decal decal = Decal.newDecal(region, true);
            decal.setDimensions(0.15f,0.15f); // Adjust for your world scale
            decal.setColor(color);
            decal.setPosition(position.x + (i) * spacing, position.y, position.z);
            decals.add(decal);
        }

        particles.add(new DamageParticle(decals, new Vector3(position), color.cpy()));
    }

    public void render(DecalBatch batch, float deltaTime, Camera camera) {
        for (int i = particles.size - 1; i >= 0; i--) {
            DamageParticle p = particles.get(i);
            p.time += deltaTime;
            if (p.time >= 1f) {
                particles.removeIndex(i);
                continue;
            }

            float alpha = 1f-p.time;
            p.position.y += deltaTime * 2f;
            Vector3 perpendicularVector=new Vector3(camera.direction).rotate(90,0f,1f,0f);
            for (int j = 0; j < p.decals.size; j++) {
                Decal d = p.decals.get(j);
                d.lookAt(camera.position, camera.up);
                d.setPosition(
                    p.position.x + (j - p.decals.size / 2f) * -perpendicularVector.x*0.2f,
                    p.position.y,
                    p.position.z+(j - p.decals.size / 2f)*-perpendicularVector.z*0.2f
                );

                d.setColor(p.color.r, p.color.g, p.color.b, alpha);
                batch.add(d);
            }
        }
    }

    private static class DamageParticle {
        Array<Decal> decals;
        Vector3 position;
        Color color;
        float time;

        DamageParticle(Array<Decal> decals, Vector3 position, Color color) {
            this.decals = decals;
            this.position = position;
            this.color = color;
            this.time = 0f;
        }
    }
}
class DigitTextures {
    public final TextureRegion[] digits = new TextureRegion[10];

    public DigitTextures(BitmapFont font) {
        BitmapFont.BitmapFontData data = font.getData();
        TextureRegion region = font.getRegion(); // Contains the whole glyph atlas texture

        for (int i = 0; i <= 9; i++) {
            char ch = (char) ('0' + i);
            BitmapFont.Glyph glyph = data.getGlyph(ch);
            if (glyph == null) {
                throw new RuntimeException("Font does not contain glyph for digit: " + ch);
            }

            // Extract glyph region from font atlas
            int x = glyph.srcX;
            int y = glyph.srcY;
            int width = glyph.width;
            int height = glyph.height;

            TextureRegion digitRegion = new TextureRegion(region.getTexture(), x, y, width, height);
            digitRegion.flip(false, false);
            digits[i] = digitRegion;
        }
    }
}




