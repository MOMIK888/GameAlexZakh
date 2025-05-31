package com.bestproject.main.Quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
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
    public void drawObjectives(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, Objective objective, float screenWidth, float screenHeight) {
        Vector3 destination = objective.getDestination();
        float centerX = screenWidth / 2;
        float centerY = screenHeight / 2;
        float distance = destination.dst(StaticBuffer.getPlayerCooordinates());
        Color originalColor = shapeRenderer.getColor();
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        float rectWidth = 400*StaticBuffer.getScaleX();
        float rectHeight = 50*StaticBuffer.getScaleX();
        float x = 0;
        float y = centerY - rectHeight;
        if(objective.getWorldIndex()!=-1 && GameEngine.getGameCore().getMap().getUniqueIndex()!=objective.getWorldIndex()){
            shapeRenderer.rect(x, y, rectWidth, rectHeight * 2);
            shapeRenderer.end();
            font.setColor(Color.WHITE);
            font.getData().setScale(0.5f);
            batch.begin();
            batch.setColor(1,1,1,0.5f);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            font.draw(batch, name, x + 10, y + rectHeight + 10);
            font.draw(batch, "Надо перейти на карту с регистром - " + objective.getWorldIndex(), x + 10, y + rectHeight - 10);
            batch.end();
        }
        else if (distance > objective.getRange()) {
            shapeRenderer.rect(x, y, rectWidth, rectHeight * 2);
            shapeRenderer.end();
            batch.begin();
            batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            font.setColor(Color.WHITE);
            font.setColor(1,1,1,0.5f);
            batch.setColor(1,1,1,0.5f);
            font.getData().setScale(0.5f);
            font.draw(batch, name, x + 10, y + rectHeight + 10-rectHeight*2);
            font.draw(batch, "Расстояние: " + distance, x + 10, y + rectHeight - 10);
            batch.end();
            float angle = subtractCameraAngle(GameCore.camera.direction,calculateAngle(StaticBuffer.getPlayerCooordinates().x,StaticBuffer.getPlayerCooordinates().z,destination.x,destination.z));
            shapeRenderer.begin();

            drawCompass(shapeRenderer, angle, x + rectWidth - 20, y + rectHeight, 10);
            shapeRenderer.end();
        } else {
            int lines = 1 + objective.getTakamount();
            float height = 20 * lines;
            y = centerY - height;
            rectHeight = height;
            shapeRenderer.rect(x, y, rectWidth, rectHeight * 2);
            shapeRenderer.end();
            font.setColor(Color.WHITE);
            font.getData().setScale(0.5f);
            batch.begin();
            batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch.setColor(1,1,1,0.5f);
            batch.enableBlending();
            font.draw(batch, name, x + 10, y + rectHeight * 2 - 10);

            float textY = y + rectHeight * 2 - 40;
            int offset = 0;
            int[] kill = objective.getKill();
            if (kill[0] > kill[1]) {
                font.draw(batch, "Убийства: " + kill[0] + "/" + kill[1], x + 10, textY - offset * 20);
                offset++;
            }
            int[] collect = objective.getCollect();
            if (collect[0] > collect[1]) {
                font.draw(batch, "Сбор: " + collect[0] + "/" + collect[1], x + 10, textY - offset * 20);
                offset++;
            }
            int[][] tasks = objective.getTasks();
            if (tasks != null && tasks[0][0] > tasks[0][1]) {
                font.draw(batch, objective.task_name, x + 10, textY - offset * 20);
            }
            batch.end();
            font.setColor(1,1,1,1f);
        }
    }
    public float subtractCameraAngle(Vector3 cameraDirection, float objectiveAngleRad) {
        float camAngleRad = (float) Math.atan2(cameraDirection.x, cameraDirection.z); // Note: x then z
        float relativeAngle = objectiveAngleRad - camAngleRad;
        while (relativeAngle > Math.PI) relativeAngle -= Math.PI * 2;
        while (relativeAngle < -Math.PI) relativeAngle += Math.PI * 2;
        return relativeAngle;
    }
    public float calculateAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.atan2(dy, dx);
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
        if(getIndex()!=-1) {
            MainGame.databaseInterface[1].setInfo(getIndex(), "1");
        }
    }
}

