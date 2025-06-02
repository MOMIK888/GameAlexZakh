package com.bestproject.main;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.video.VideoPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class StartingScreen implements Disposable, InputProcessor {

    SpriteBatch batch;
    PerspectiveCamera camera;
    VideoPlayer videoPlayer;
    ModelBatch modelBatch;
    public boolean gameStarted = false;
    private Array<ThreeDButton> buttons;
    private ModelBuilder modelBuilder;
    private Model buttonModel;
    public StartingScreen() {
        batch = new SpriteBatch();
        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        videoPlayer= VideoPlayerCreator.createVideoPlayer();
        try {
            videoPlayer.play(Gdx.files.internal("LoadingSC/townRender2.mp4"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 5, 10);
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 100f;
        Gdx.input.setInputProcessor(this);
        camera.update();
        buttonModel = modelBuilder.createBox(2, 1, 0.5f,

            new Material(),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        buttons = new Array<>();
        ThreeDButton startButton = new ThreeDButton(new Vector3(-2, 0, 0));
        ThreeDButton secondButton = new ThreeDButton(new Vector3(2, 0, 0));

        buttons.add(startButton);
        buttons.add(secondButton);
    }
    @Override
    public boolean  touchDown(int touchX, int touchY, int pointer, int button) {
        Ray pickRay = camera.getPickRay(touchX, touchY);
        for (int i = 0; i < buttons.size; i++) {
            ThreeDButton buttonn = buttons.get(i);
            if (Intersector.intersectRayBounds(pickRay, buttonn.boundingBox, null)) {
                if (i == 0) {
                    gameStarted = true;
                } else {
                }
                return true;
            }
        }
        return false;
    }

    public void render() {
        videoPlayer.update();

        camera.update();
        Texture frame = videoPlayer.getTexture();
        if (frame != null) {
            batch.begin();
            batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }
        modelBatch.begin(camera);
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDepthMask(true);
        for (ThreeDButton button : buttons) {
            modelBatch.render(button.instance);
        }
        modelBatch.end();
        camera.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        batch.dispose();
        buttonModel.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private class ThreeDButton {
        public ModelInstance instance;
        public BoundingBox boundingBox;

        public ThreeDButton(Vector3 position) {
            this.instance = new ModelInstance(buttonModel);
            this.instance.transform.setToTranslation(position);
            boundingBox = new BoundingBox();
            buttonModel.calculateBoundingBox(boundingBox);
            boundingBox.mul(this.instance.transform);
        }
    }
}

