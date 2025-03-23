package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class UVSHADER implements Shader {
    private ShaderProgram shaderProgram;
    String vert, frag;
    public UVSHADER(String vert, String frag){
        this.vert=vert;
        this.frag=frag;
    }
    @Override
    public void init() {
        shaderProgram = new ShaderProgram(
            Gdx.files.internal(vert).readString(),
            Gdx.files.internal(frag).readString()
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

    }
    public void setOffset(float offx, float offy){
        shaderProgram.setUniformf("u_uvOffset", offx,offy);
    }
    public void setLight(Vector3 light){
        shaderProgram.setUniformf("u_lightDir", light.x, light.y, light.z);
    }

    @Override
    public void render(Renderable renderable) {
        // Set the world transformation matrix
        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);

        // Bind the texture if your model uses one
        if (renderable.material.has(TextureAttribute.Diffuse)) {
            TextureAttribute textureAttribute = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);
            textureAttribute.textureDescription.texture.bind(0);
            shaderProgram.setUniformi("u_texture", 0);
        }

        // Render the mesh part
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

