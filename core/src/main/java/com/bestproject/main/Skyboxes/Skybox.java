package com.bestproject.main.Skyboxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

import shaders.DiffuseShader;

public class Skybox implements Disposable {
    public Mesh skyboxMesh;
    private Texture skyboxTexture;
    public static ShaderProgram skyboxShader;

    public Skybox(String texturePath) {
        skyboxMesh = createSkyboxMesh();
        skyboxTexture = new Texture(Gdx.files.internal(texturePath));
        skyboxTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        skyboxShader = new ShaderProgram(
            Gdx.files.internal("shaders/skybox.vert"),
            Gdx.files.internal("shaders/skybox.frag")
        );
        if (!skyboxShader.isCompiled()) {
            Gdx.app.error("Skybox Shader", skyboxShader.getLog());
        }
    }

    public void render(PerspectiveCamera camera) {
        Gdx.gl.glDepthMask(false);
        skyboxShader.begin();
        skyboxShader.setUniformMatrix("u_projTrans", camera.combined);
        skyboxShader.setUniformi("u_texture", 0);
        skyboxTexture.bind(0);
        skyboxMesh.render(skyboxShader, GL20.GL_TRIANGLES);
        skyboxShader.end();
        Gdx.gl.glDepthMask(true);
    }

    protected Mesh createSkyboxMesh() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("skybox", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position, new Material());
        builder.box(100f, 100f, 100f);
        return modelBuilder.end().meshParts.get(0).mesh;
    }

    @Override
    public void dispose() {
        skyboxMesh.dispose();
        skyboxTexture.dispose();
        skyboxShader.dispose();
    }
}
