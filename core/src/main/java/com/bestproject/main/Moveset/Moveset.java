package com.bestproject.main.Moveset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CharacterUtils.CharacterWidget;
import com.bestproject.main.CharacterUtils.FontConfig;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

import java.util.ArrayList;

public class Moveset implements Disposable {
    protected int charinfo=0;
    protected Model characterModel;
    protected ModelInstance modelInstance;
    protected ArrayList<ImageButton> buttons; //disposed
    protected ArrayList<Integer> simoltanious_buttons;
    protected ArrayList<Model> attacks;
    AnimationController[] controllers;
    protected float[] charge=new float[]{0,0};
    protected float stamina;
    protected String MovesetName="moveset";
    protected static FontConfig font=new FontConfig(StaticBuffer.fonts[0],1f,StaticBuffer.choice_colors[1]);
    protected float hp;
    protected float maxHp;
    protected float critchance;
    protected float ability_cooldown;
    protected float critmultiplier;
    protected float isRoll;
    protected float[] cooldowns=new float[]{0,0,0,0};
    protected int[][] Bitmap_button_coordinates;
    public float cd=0;
    protected Vector3 lock_omn_coordinates=new Vector3();
    protected CharacterWidget characterWidget;
    public float damageMultiplier=0f;
    boolean isPunch=false;
    boolean lastManstanding=false;
    int current_state=0; //0-стачичный 1-Атака 2-Способность, 3-ульта 4-перекат
    public Moveset(){
        ability_cooldown=0;
        isRoll=0;
        buttons=new ArrayList<>();
        simoltanious_buttons=new ArrayList<>();
        attacks=new ArrayList<>();
        controllers=new AnimationController[3];
        characterWidget=new CharacterWidget(null);
    }
    public void DrawMovesetWidget(ShapeRenderer shapeRenderer){

    }
    @Override
    public void dispose() {
        for(int i=0; i<buttons.size(); i++){
            buttons.get(i).dispose();
        }
        characterModel.dispose();
        for(int i=0; i<attacks.size(); i++){
            attacks.get(i).dispose();
        }

    }
    public void ActivateLms(){
        lastManstanding=true;
    }
    public void resetLMS(){
        lastManstanding=false;
    }
    public boolean getLMS(){
        return hp>0;
    }
    public void PlayerInterrractions(Player player){

    }
    public void drawButtonPaddings(ShapeRenderer shapeRenderer){

    }
    public void draw(SpriteBatch spriteBatch){

    }
    public void update(){

    }
    public void chargeUlt(float value){
        charge[1]+=value;
    }
    public boolean OnTouch(float touchX, float touchY, int pointer){
        return false;
    }
    public boolean OnHold(float touchX, float touchY, int pointer){
        return  false;
    }
    public boolean OnRelease(float touchX, float touchY, int pointer){
        return false;
    }
    public void setStamina(float Stamina){
        this.stamina=Stamina;
    }
    public float getStamina(){
        return stamina;
    }
    public void resetPointer(int pointer){

    }
    public void updateInfo(){
        String info=String.valueOf(hp) + "&" + String.valueOf(charge[1]);
        StaticBuffer.databaseController.updateCharacterDb(charinfo,info);
    }
    public void button_suggestion(int index){
        simoltanious_buttons.add(index);
    }
    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
        if(hp<=0){
            StaticBuffer.ui.AnalyzeLms();
        }
    }

    public Model getCharacterModel() {
        return characterModel;
    }

    public void setCharacterModel(Model characterModel) {
        this.characterModel = characterModel;
    }
    protected void goThrouh(){

    }
    protected void breakdown(){

    }
    public boolean OnHold(){
        return false;
    }
    public boolean isRolling(){
        return isRoll>0;
    }
    public float getIsRoll(){
        return  isRoll;
    }

    public int getCurrentState() {
        return current_state;
    }
    public void forceNullification(Player player){
        float glvevel= GameEngine.getGameCore().getMap().GetGroundLevel(new Vector3((float) player.hitboxes[0].x, (float) player.hitboxes[0].z, (float) player.hitboxes[0].y));

        player.movement.add(player.force);
        if(player.hitboxes[0].getBottom()>=glvevel) {
            if (player.force.x > 0) {
                player.force.x -= StaticQuickMAth.move(0.1f) * GameCore.deltatime/8;
                if (player.force.x < 0) {
                    player.force.x = 0;
                }
            } else {
                player.force.x += StaticQuickMAth.move(0.1f) * GameCore.deltatime/8;
                if (player.force.x > 0) {
                    player.force.x = 0;
                }
            }
            if (player.force.z > 0) {
                player.force.z -= StaticQuickMAth.move(0.1f) * GameCore.deltatime/8;
                if (player.force.z < 0) {
                    player.force.z = 0;
                }
            } else {
                player.force.z += StaticQuickMAth.move(0.1f) * GameCore.deltatime/8;
                if (player.force.z > 0) {
                    player.force.z = 0;
                }
            }
            if (player.isGravityAffected) {
                player.force.y -= StaticQuickMAth.getGravityAcceleration() * player.gravity_multip/2f/(Math.max(1,(Math.abs(player.movement.x)+Math.abs(player.movement.z))*50));
            } else if (player.force.y < 0) {
                player.force.y = 0;
            }
            if (player.force.y < -0.5f) {
                player.force.y = -0.5f;
            }
            if(player.hitboxes[0].getBottom()<glvevel){
                player.position.set(new Vector3(player.position.x, (float) (glvevel+(player.hitboxes[0].height/2.01f)), player.position.z));
                player.gravity_multip=1f;
            }
        } else{
            if (player.force.x > 0) {
                player.force.x -= StaticQuickMAth.move(0.4f) * GameCore.deltatime;
                if (player.force.x < 0) {
                    player.force.x = 0;
                }
            } else {
                player.force.x += StaticQuickMAth.move(0.4f) * GameCore.deltatime;
                if (player.force.x > 0) {
                    player.force.x = 0;
                }
            }
            if (player.force.z > 0) {
                player.force.z -= StaticQuickMAth.move(0.4f) * GameCore.deltatime;
                if (player.force.z < 0) {
                    player.force.z = 0;
                }
            } else {
                player.force.z += StaticQuickMAth.move(0.4f) * GameCore.deltatime;
                if (player.force.z > 0) {
                    player.force.z = 0;
                }
            }
            if(player.force.y<0){
                player.force.y=0;
            }
            if(player.hitboxes[0].getBottom()<glvevel){
                player.position.set(new Vector3(player.position.x, (float) (glvevel+(player.hitboxes[0].height/2.01f)), player.position.z));
                player.gravity_multip=1f;
            }
        }
    }
    public ModelInstance getModelInstance(){
        return modelInstance;
    }
    public CharacterWidget getCharacterWidget(){
        return characterWidget;
    }

}
