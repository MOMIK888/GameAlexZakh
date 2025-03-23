package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class CostumeShader implements Shader {
    private ShaderProgram shaderProgram;
    String vert, frag;
    public CostumeShader(String vert, String frag){
        this.vert=vert;
        this.frag=frag;
    }
    @Override
    public void init() {
        shaderProgram = new ShaderProgram(
            Gdx.files.internal("shaders/TestShader/vertexAnimeGreen.glsl"),
            Gdx.files.internal("shaders/TestShader/fragmentAnimeGreen.glsl")
        );
        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("Shader", "Compilation failed: " + shaderProgram.getLog());
        }
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projViewTrans", camera.combined);
        shaderProgram.setUniformf("u_color1", 1.0f, 0.0f, 0.0f); // Red
        shaderProgram.setUniformf("u_color2", 0.0f, 0.0f, 1.0f); // Blue
        shaderProgram.setUniformf("u_colorRampPosition", 0.2f);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }
    public void setLight(Vector3 light){
        shaderProgram.setUniformf("u_lightDir", light.x, light.y, light.z);
    }

    @Override
    public void render(Renderable renderable) {
        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        renderable.meshPart.render(shaderProgram);
    }

    @Override
    public void end() {
        shaderProgram.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }
}
