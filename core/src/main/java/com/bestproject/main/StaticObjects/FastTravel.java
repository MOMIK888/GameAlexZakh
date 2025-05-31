package com.bestproject.main.StaticObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Dialogues.DialogueDecal;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.StaticBuffer;

public class FastTravel extends StaticObject{
    DialogueDecal dialogueDecal;
    public FastTravel(Model model, Vector3 position) {
        super(new ModelInstance(model), position);
        dialogueDecal=new DialogueDecal(StaticBuffer.fonts[0],new Texture[0], "Войти?");
        dialogueDecal.setPosition(new Vector3(position).add(0f,1f,0f));
    }
    @Override
    public void update() {

    }
    @Override
    public void render(ModelBatch modelBatch, Environment environment){
        super.render(modelBatch,environment);
        if(StaticBuffer.getPlayerCooordinates().dst(this.position)<3f) {

            dialogueDecal.render(StaticBuffer.decalBatch,GameCore.camera);
            if(!StaticBuffer.isCreative && !GameEngine.getGameCore().displayQuickTravel && !StaticBuffer.getIsPaused()) {
                dialogueDecal.update(GameCore.deltatime);
                System.out.println(GameEngine.getGameCore().displayQuickTravel);
                if (dialogueDecal.getClick()) {
                    dialogueDecal.setZroClick();
                    GameEngine.getGameCore().displayQuickTravel = true;
                    System.out.println(GameEngine.getGameCore().displayQuickTravel);
                }
            }
        } else{
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
