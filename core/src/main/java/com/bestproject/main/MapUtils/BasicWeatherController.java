package com.bestproject.main.MapUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.bestproject.main.Skyboxes.ColorFulSkybox;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class BasicWeatherController {
    private final Sun sun;
    private float timeOfDay;
    public int currentTime;
    private final Color sunriseColor = new Color(1.0f, 0.6f, 0.3f, 1f);
    private final Color dayColor     = new Color(1.0f, 1.0f, 0.9f, 1f);
    private final Color sunsetColor  = new Color(1.0f, 0.4f, 0.2f, 1f);
    private final Color nightColor   = new Color(0.8f, 0.8f, 1.0f, 1f);
    private final Color currentSunColor = new Color();
    public BasicWeatherController(float startTime) {
        this.timeOfDay = startTime;
        this.sun = new Sun(StaticQuickMAth.time);
        updateTimeBlock();
    }
    public void render(){
        sun.render(StaticBuffer.decalBatch);
    }
    public void update(float time) {
        timeOfDay = time;

        if (timeOfDay >= 24f) timeOfDay -= 24f;

        sun.update(time);
        updateTimeBlock();
        updateSunColor();
    }

    private void updateTimeBlock() {
        if (timeOfDay >= 20f || timeOfDay < 5f) {
            currentTime = 1;
        } else {
            currentTime = 0;
        }
    }
    public void synchSkybox(ColorFulSkybox skybox){
        skybox.setCurrentTime(currentTime);
    }
    private void updateSunColor() {
        float alpha;
        if (timeOfDay >= 5f && timeOfDay < 12f) {
            alpha = (timeOfDay - 5f) / 7f;
        } else if (timeOfDay >= 12f && timeOfDay < 16f) {
            alpha = 1f;
        } else if (timeOfDay >= 16f && timeOfDay < 20f) {
            alpha = 1f - ((timeOfDay - 16f) / 4f);
        } else {
            alpha = 0f;
        }
        alpha = MathUtils.clamp(alpha, 0f, 0.6f);
        if (timeOfDay >= 5f && timeOfDay < 8f) {
            float t = (timeOfDay - 5f) / 3f;
            currentSunColor.set(sunriseColor).lerp(dayColor, t);
        } else if (timeOfDay >= 8f && timeOfDay < 17f) {
            currentSunColor.set(dayColor);
        } else if (timeOfDay >= 17f && timeOfDay < 20f) {
            float t = (timeOfDay - 17f) / 3f;
            currentSunColor.set(dayColor).lerp(sunsetColor, t);
        } else {
            currentSunColor.set(nightColor);
        }
        currentSunColor.a = alpha;
    }
    public Vector4 getSunRayDir() {
        Vector3 dir = sun.getLightDir();
        return new Vector4(dir.x, dir.y, dir.z, currentSunColor.a);
    }
    public Color getSunColor() {
        return currentSunColor;
    }
    public float getTimeOfDay() {
        return timeOfDay;
    }
}

