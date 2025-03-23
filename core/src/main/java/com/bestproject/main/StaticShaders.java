package com.bestproject.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.CostumeShader;
import com.bestproject.main.CostumeClasses.IMPACTFRAME;
import com.bestproject.main.CostumeClasses.UVSHADER;

public class StaticShaders implements Disposable {
    public static UVSHADER texcoords_shader=new UVSHADER("shaders/TexCooordsShader/vert.glsl","shaders/TexCooordsShader/frag.glsl");
    public static IMPACTFRAME impactSHADER=new IMPACTFRAME();
    public static void init(){
        texcoords_shader.init();
    }
    public static void disposeAll(){
        texcoords_shader.dispose();
    }
    @Override
    public void dispose() {
        disposeAll();
    }
}
