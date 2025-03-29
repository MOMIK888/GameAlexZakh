package com.bestproject.main.CostumeClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ToonShader extends DefaultShader {
    private int u_lightDir, u_lightColor, u_ambientColor, u_objectColor;
    private int u_bones;

    public ToonShader(Renderable renderable) {
        super(renderable, new DefaultShader.Config());

    }

    @Override
    public void init() {
        super.init();
        u_lightDir = program.getUniformLocation("u_lightDir");
        u_lightColor = program.getUniformLocation("u_lightColor");
        u_ambientColor = program.getUniformLocation("u_ambientColor");
        u_objectColor = program.getUniformLocation("u_objectColor");
        u_bones = program.getUniformLocation("u_bones");
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);
        program.setUniformf(u_lightDir, 0.0f, -1.0f, -1.0f);
        program.setUniformf(u_lightColor, Color.WHITE.r, Color.WHITE.g, Color.WHITE.b);
        program.setUniformf(u_ambientColor, 0.2f, 0.2f, 0.2f);
    }

    @Override
    public void render(Renderable renderable) {
        ColorAttribute diffuse = (ColorAttribute) renderable.material.get(ColorAttribute.Diffuse);
        program.setUniformf(u_objectColor, diffuse.color.r, diffuse.color.g, diffuse.color.b);
        if (renderable.bones != null && renderable.bones.length > 0) {
            for (int i = 0; i < renderable.bones.length; i++) {
                program.setUniformMatrix(u_bones + "[" + i + "]", renderable.bones[i]);
            }
        }

        super.render(renderable);
    }
}

