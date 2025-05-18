package com.bestproject.main.Quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bestproject.main.MainGame;
import com.bestproject.main.StaticBuffer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Quest {
    ArrayList<Objective> objectives=new ArrayList<>();
    int current_objective=0;
    String name="";
    int unique_index;
    boolean is_dest=true;
    boolean isCompleted=false;
    boolean is_init=false;
    public Quest(){

    }
    public boolean getIscompleted(){
        return isCompleted;
    }
    public void drawObjectives(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer){

    }
    public int getIndex(){
        return -1;
    }
    public void initialize(ArrayList<Objective> objectiveArray, String name, int unique_index){
        this.name=name;
        this.objectives=objectiveArray;
        this.unique_index=unique_index;
        is_init=true;
    }
    public void Complete_objectives(int kill, int collect, int[] tasks){
        if(!objectives.isEmpty()) {
            if (is_dest) {
                objectives.get(current_objective).setComplete(kill, collect, tasks, true);
                if (objectives.get(current_objective).getIscompleted()) {
                    if (current_objective < objectives.size() - 1) {
                        current_objective += 1;
                    } else {
                        isCompleted = true;
                    }
                }
            }
        }
    }
    public int getUnique_index(){
        return unique_index;
    }
    public int getDistance(int[] point1, int[] point2){
        int x1 = point1[0];
        int y1 = point1[1];
        int x2 = (int) (point2[0]);
        int y2 = (int) (point2[1]);
        int dx = x2 - x1;
        int dy = y2 - y1;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
    public static double calculateAngle(int[] x1, int[] x2) {
        double dx = (x2[0]) - x1[0];
        double dy = (x2[1]) - x1[1];
        return Math.atan2(dy, dx);
    }

    public static void drawCompass(ShapeRenderer shapeRenderer, double radians, float x, float y, float radius) {
        float centerX =x;
        float centerY=y;
        Gdx.gl.glLineWidth(5);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(StaticBuffer.choice_colors[1]);
        shapeRenderer.circle(centerX, centerY, radius);
        int endX = (int) (centerX + radius * Math.cos(radians));
        int endY = (int) (centerY + radius * Math.sin(radians));
        shapeRenderer.line(centerX, centerY, endX, endY);
    }
    public void getRewards(){
        MainGame.databaseInterface[1].setInfo(getIndex(),"1");
    }
}

