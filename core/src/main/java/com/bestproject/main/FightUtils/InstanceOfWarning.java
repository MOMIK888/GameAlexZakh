package com.bestproject.main.FightUtils;

import com.badlogic.gdx.math.Vector3;

public class InstanceOfWarning {
    public enum Type {
        CIRCULAR,
        LINEAR
    }

    public Type type;
    public Vector3 position;
    public float rotation; // in degrees
    public float width;
    public float height;

    public InstanceOfWarning(Type type, Vector3 position, float width, float height, float rotation) {
        this.type = type;
        this.position = new Vector3(position);
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}

