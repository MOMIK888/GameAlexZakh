package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;

public class CostumeShaderProvider implements ShaderProvider {
    private CostumeShader shader; //disposed
    String vert, frag;
    public CostumeShaderProvider(String vert, String frag){
        this.vert=vert;
        this.frag=frag;
    }
    @Override
    public Shader getShader(Renderable renderable) {
        if (shader == null) {
            shader = new CostumeShader(vert, frag);
            shader.init();
        }
        return shader;
    }

    @Override
    public void dispose() {
        if (shader != null) shader.dispose();
    }
}
