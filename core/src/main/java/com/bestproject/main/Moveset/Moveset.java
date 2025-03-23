package com.bestproject.main.Moveset;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.MovingObjects.Player;

import java.util.ArrayList;

public class Moveset implements Disposable {
    protected Model characterModel;
    protected ArrayList<ImageButton> buttons; //disposed
    protected ArrayList<Integer> simoltanious_buttons;
    protected ArrayList<Model> attacks;
    protected float[] charge=new float[]{0,0};
    protected float stamina;
    protected float hp;
    protected float critchance;
    protected float ability_cooldown;
    protected float critmultiplier;
    protected float isRoll;
    protected float[] cooldowns=new float[]{0,0,0,0};
    protected int[][] Bitmap_button_coordinates;
    public float cd=0;
    protected Vector3 lock_omn_coordinates=new Vector3();
    boolean isPunch=false;
    int current_state=0; //0-стачичный 1-Атака 2-Способность, 3-ульта 4-перекат
    public Moveset(){
        ability_cooldown=0;
        isRoll=0;
        buttons=new ArrayList<>();
        simoltanious_buttons=new ArrayList<>();
        attacks=new ArrayList<>();
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
    public void PlayerInterrractions(Player player){

    }
    public void drawButtonPaddings(ShapeRenderer shapeRenderer){

    }
    public void draw(SpriteBatch spriteBatch){

    }
    public void update(){

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
    public void button_suggestion(int index){
        simoltanious_buttons.add(index);
    }
    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public Model getCharacterModel() {
        return characterModel;
    }

    public void setCharacterModel(Model characterModel) {
        this.characterModel = characterModel;
    }
    protected void goThrouh(){

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
}
