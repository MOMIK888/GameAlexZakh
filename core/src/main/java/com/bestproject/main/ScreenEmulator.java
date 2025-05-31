package com.bestproject.main;

public interface ScreenEmulator {
    void OnTouch(float screenX, float screenY, int pointer);

    void OnScroll(float screenX, float screenY, int pointer);

    void OnUp(float screenX, float screenY, int pointer);
}
