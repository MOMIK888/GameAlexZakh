package com.bestproject.main.MapRender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.StaticBuffer;

public class RenderPackage {
    public DirectionalShadowLight shadowLight;
    public ModelBatch shadowBatch;
    public FrameBuffer frameBuffer;
    public Mesh fullscreenMesh;
    public ModelBatch depthModelBatch;
    public ModelBatch modelBatch;
    public FrameBuffer depthBuffer;
    public ShaderProgram outlineShader;
    public ShaderProgram toonShader;
    public Environment environment;
    public DirectionalLight directionalLightEx;
    public RenderPackage(){
        fullscreenMesh=createFullScreenQuad();
        modelBatch=new ModelBatch();
        depthBuffer=new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        depthModelBatch=new ModelBatch(Gdx.files.internal("shaders/depthShader/Depth.vert").readString(),
            Gdx.files.internal("shaders/depthShader/Depth.frag").readString());
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888
            , Gdx.graphics.getWidth()
            , Gdx.graphics.getHeight()
            , true);
        toonShader=new ShaderProgram(Gdx.files.internal("shaders/toon/toon.vert").readString()
            , Gdx.files.internal("shaders/toon/toon.frag").readString());
        outlineShader=new ShaderProgram(Gdx.files.internal("shaders/Outline/outline.frag").readString()
            , Gdx.files.internal("shaders/Outline/outline.vert").readString());
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 40f, 40f, 1f, 30f))
            .set(1f, 1f, 1f, 40.0f, -55f, -55f));
        environment.shadowMap = shadowLight;
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        shadowBatch = new ModelBatch(new DepthShaderProvider());
        directionalLightEx=new DirectionalLight();
        environment.add(directionalLightEx.set(0.5f,0.5f,0.5f,new Vector3()));

    }
    public Mesh createFullScreenQuad() {
        float[] verts = new float[]{
            -1.f, -1.f,  0.f,  0.f, 0.f,
            1.f, -1.f,  0.f,  1.f, 0.f,
            1.f,  1.f,  0.f,  1.f, 1.f,
            -1.f,  1.f,  0.f,  0.f, 1.f
        };

        short[] indices = new short[]{
            0, 1, 2,
            2, 3, 0
        };

        Mesh tmpMesh = new Mesh(true, 4, 6,
            new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));

        tmpMesh.setVertices(verts);
        tmpMesh.setIndices(indices);

        return tmpMesh;
    }
}
