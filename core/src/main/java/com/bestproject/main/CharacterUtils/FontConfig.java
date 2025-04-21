package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.net.CookieHandler;

public class FontConfig {
    protected BitmapFont font;
    protected float fontSize;
    protected Color fontcolor;
    public FontConfig(BitmapFont font, float size, Color color){
        this.font=font;
        this.fontSize=size;
        this.fontcolor=color;
    }
    public void setFontConfig(){
        getFont().getData().setScale(getFontSize());
        getFont().setColor(getFontcolor());
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(Color fontcolor) {
        this.fontcolor = fontcolor;
    }
}
