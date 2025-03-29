package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FrontFaceShader implements Shader {
    public ShaderProgram shader;
    private int u_projViewWorldTrans;
    public FrontFaceShader(String v, String f){
        shader=new ShaderProgram(v,f);
    }
    @Override
    public void init() {
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return false;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        shader.bind();
        shader.setUniformMatrix(u_projViewWorldTrans, camera.combined);
    }

    @Override
    public void render(Renderable renderable) {
        shader.setUniformMatrix("u_projViewWorldTrans", renderable.worldTransform);
        renderable.meshPart.mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void end() {

    }

    @Override
    public void dispose() {

    }
}
