package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FrontSaceDepthProvider implements ShaderProvider {
    private FrontFaceShader frontFaceShader;
    private int u_projViewWorldTrans;
    String vert, frag;
    public FrontSaceDepthProvider(){
        vert=Gdx.files.internal("shaders/depthShader/Depth.vert").readString();
        frag= Gdx.files.internal("shaders/depthShader/Depth.frag").readString();

    }
    @Override
    public Shader getShader(Renderable renderable) {
        if(frontFaceShader==null) {
            frontFaceShader = new FrontFaceShader(vert, frag);
            frontFaceShader.init();
        }
        return frontFaceShader;
    }

    @Override
    public void dispose() {

    }
}
