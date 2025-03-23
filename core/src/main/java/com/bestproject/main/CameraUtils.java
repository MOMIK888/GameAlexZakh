package com.bestproject.main;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class CameraUtils {
    public static Vector3 pushTowardsCamera(Camera camera, Vector3 position, float offset) {
        Vector3 direction = new Vector3(camera.position).sub(position).nor();
        Vector3 newPosition = new Vector3(position).mulAdd(direction, -offset);
        return newPosition;
    }
}
