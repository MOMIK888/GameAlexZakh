package com.bestproject.main.Dialogues;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextPrinter {
    private String text;
    private float charDelay;
    private float timer = 0f;
    private int currentCharIndex = 0;
    private int[] bounceOffsets;
    private int[] bounceDirections;

    private boolean isFinished = false;
    private float x, y;
    private float maxWidth;
    private float lineHeight;
    private BitmapFont font;
    private final float baseWidth = 1920f;
    private final float baseHeight = 1080f;

    public TextPrinter(String text, float x, float y, float delay, float maxWidth, BitmapFont font, float screenWidth, float screenHeight) {
        this.font = font;
        this.charDelay = delay;
        this.text = text;
        this.x = scaleX(x, screenWidth);
        this.y = scaleY(y, screenHeight);
        this.maxWidth = scaleX(maxWidth, screenWidth);
        this.lineHeight = font.getLineHeight();
        this.bounceOffsets = new int[this.text.length()];
        this.bounceDirections = new int[this.text.length()];
        for (int i = 0; i < this.text.length(); i++) {
            bounceDirections[i] = 1;
        }
    }

    public void reset(String newText) {
        this.text = newText + " ";
        this.currentCharIndex = 0;
        this.timer = 0f;
        this.isFinished = false;
        this.bounceOffsets = new int[newText.length()];
        this.bounceDirections = new int[newText.length()];
        for (int i = 0; i < newText.length(); i++) {
            bounceOffsets[i] = 0;
            bounceDirections[i] = 1;
        }
    }

    public void skip() {
        currentCharIndex = text.length();
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
    public void update(float delta){
        if (isFinished) return;
        timer+=delta;
    }
    public void draw(SpriteBatch batch) {
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        batch.setColor(Color.WHITE);
        if(true) {

            while (timer >= charDelay && currentCharIndex < text.length()) {
                timer -= charDelay;
                currentCharIndex++;
                if (currentCharIndex >= text.length()) {
                    isFinished = true;
                }
            }
            if(currentCharIndex>text.length()-1) currentCharIndex=text.length()-1;

            float drawX = x;
            float drawY = y;
            StringBuilder currentLine = new StringBuilder();

            for (int i = 0; i < currentCharIndex; i++) {
                char ch = text.charAt(i);
                currentLine.append(ch);

                float lineWidth = font.getCache().setText(currentLine.toString(), 0, 0).width;
                if (lineWidth > maxWidth && currentLine.length() > 1) {
                    currentLine = new StringBuilder(currentLine.substring(currentLine.lastIndexOf(" ") + 1));
                    drawY -= lineHeight;
                    drawX = x;
                }

                float charWidth = font.getCache().setText(String.valueOf(ch), 0, 0).width;
                font.draw(batch, String.valueOf(ch), drawX, drawY + bounceOffsets[i]);
                if (bounceOffsets[i] != 0 || i == currentCharIndex - 1) {
                    bounceOffsets[i] += bounceDirections[i] * (int) scaleY(60, 1080);
                    int bounceLimit = (int) scaleY(10, 1080);
                    if (bounceOffsets[i] >= bounceLimit || bounceOffsets[i] <= -bounceLimit) {
                        bounceDirections[i] *= -1;
                    }
                    if (bounceOffsets[i] == 0) {
                        bounceDirections[i] = 1;
                    }
                }

                drawX += charWidth;
            }
        }
    }

    private float scaleX(float value, float screenWidth) {
        return value * (screenWidth / baseWidth);
    }
    public boolean isfinished(){
        return isFinished;
    }

    private float scaleY(float value, float screenHeight) {
        return value * (screenHeight / baseHeight);
    }
}
