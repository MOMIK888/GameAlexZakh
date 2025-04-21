package com.bestproject.main.CharacterUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.Vector3;

public class Elytra {
    private static final float GRAVITY = 9.84f;
    private static final float DRAG = 0.33f;
    private static final float PITCH_FACTOR = 0.1f;
    private static final float MAX_SPEED = 50f;

    private Vector3 velocity;
    public boolean isGliding=true;

    public Elytra() {
        this.velocity = new Vector3();
    }

    public void update(Vector3 force, float cameraYaw, float cameraPitch, float deltaTime) {
        velocity.set(force);
        float pitchRadians = (float) Math.toRadians(cameraPitch);
        float yawRadians = (float) Math.toRadians(cameraYaw);
        Vector3 direction = new Vector3(
             (float) Math.cos(yawRadians) * (float) Math.cos(pitchRadians),
            - (float) Math.sin(pitchRadians),
            (float) Math.sin(yawRadians) * (float) Math.cos(pitchRadians)
        );
        velocity.y -= GRAVITY * deltaTime;
        float lastval=Math.abs(velocity.y);
        velocity.add(direction.scl(force.len() * deltaTime,force.len() * deltaTime,force.len() * deltaTime));
        float diff=Math.abs(velocity.y)-lastval;
        float speed = velocity.len();
        velocity.x-=diff*direction.x;
        velocity.z-=diff*direction.z;
        velocity.x-=velocity.x*DRAG*deltaTime;
        velocity.z-=velocity.z*DRAG*deltaTime;
        velocity.y-=velocity.y*1.22f*deltaTime;
        if (velocity.len() > MAX_SPEED) {
            velocity.nor().scl(MAX_SPEED);
        }

    }

    public Vector3 getVelocity() {
        Vector3 oldvel=new Vector3(velocity);
        return oldvel;
    }
    public void rocketFly(){

    }
}

