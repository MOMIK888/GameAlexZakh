package com.bestproject.main.Skyboxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.StaticBuffer;

public class ColorFulSkybox implements Disposable {
    public Model skybox;
    private Texture skyboxTexture;
    public static ShaderProgram skyboxShader;
    public Vector3 position=new Vector3();


    public ColorFulSkybox(String Path, String TexturePath, float size) {
        StaticBuffer.assetManager.load(Path, Model.class);
        StaticBuffer.assetManager.finishLoading();
        skybox =  StaticBuffer.assetManager.get(Path,Model.class);
        skyboxTexture=new Texture(TexturePath);
        skyboxShader = new ShaderProgram(
            Gdx.files.internal("shaders/skybox2.vert"),
            Gdx.files.internal("shaders/skybox2.frag")
        );
        if (!skyboxShader.isCompiled()) {
            Gdx.app.error("Skybox Shader", skyboxShader.getLog());
        }
    }

    public void render(PerspectiveCamera camera, ModelBatch mb) {
        Gdx.gl.glDepthMask(false);

        skyboxShader.begin();
        skyboxShader.setUniformMatrix("u_projTrans", camera.combined);
        skyboxShader.setUniformi("u_texture", 0);
        skyboxShader.setUniformf("u_offset", position.x, position.y, position.z);
        skyboxTexture.bind(0);
        skybox.meshes.get(0).render(skyboxShader,GL20.GL_TRIANGLES);
        skyboxShader.end();
        Gdx.gl.glDepthMask(true);
    }
    @Override
    public void dispose() {
        skybox.dispose();
        skyboxTexture.dispose();
        skyboxShader.dispose();
    }
    public void setPosition(Vector3 position){
        this.position.set(position);
    }
}
