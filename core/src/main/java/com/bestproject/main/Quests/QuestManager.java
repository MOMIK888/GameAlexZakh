package com.bestproject.main.Quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CharacterUtils.DialogueScreen;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MainGame;
import com.bestproject.main.ObjectItem;
import com.bestproject.main.StaticBuffer;

import java.util.ArrayList;
import java.util.Iterator;

public class QuestManager {
    ArrayList<Quest> quests=new ArrayList<>();
    int current_quest=0;
    ArrayList<Integer> completedQuests=new ArrayList<>();
    public QuestManager(){
        MainGame.databaseInterface[1].setInfo(0,"0");
        if(true){
            quests.add(new Quest(){
                @Override
                public int getIndex(){
                    return 0;
                }
                @Override
                public void getRewards(){
                    super.getRewards();
                    quests.add(new Quest(){
                        @Override
                        public int getIndex(){
                            return 1;
                        }
                    });
                    DialogueScreen dialogueScreen=new DialogueScreen(new String[]{"*Тук тук тук*", "Кто там?", "Маг Е класса, пришел сюда выполнить некое задание.", "И как я могу верить тебе?", "Может быть ты из этик...", "Культистов...", "Какие еще культисты?", "Ты серьезно не знаешь?",
                    "Короче, это куча психов поклоняющихся технике и отвергающих магию", "Они у меня уже ТРЕТИЙ тостер украли!", "Я думаю ты понял суть. Просто победи их *Машину* и я тебе поверю...", "Да, конечно...", "...", "(И, что куча людей ворующих тостеры может мне сделать...)"});
                    GameCore.screenSpaceSim=dialogueScreen;
                    dialogueScreen.afterInit();
                    Quest quest=new Quest(){
                        @Override
                        public int getIndex(){
                            return 1;
                        }
                        @Override
                        public void getRewards() {
                            super.getRewards();
                            quests.add(new Quest() {
                                @Override
                                public int getIndex() {
                                    return 2;
                                }
                            });
                            DialogueScreen dialogueScreen = new DialogueScreen(new String[]{"Что...", "Это...","Такое..."});
                            GameCore.screenSpaceSim = dialogueScreen;
                            dialogueScreen.afterInit();
                        }
                        @Override
                        public void drawObjectives(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, Objective objective, float screenWidth, float screenHeight) {
                            Vector3 destination = objective.getDestination();
                            float centerX = screenWidth / 2;
                            float centerY = screenHeight / 2;
                            float distance = destination.dst(StaticBuffer.getPlayerCooordinates());
                            Color originalColor = shapeRenderer.getColor();
                            shapeRenderer.begin();
                            shapeRenderer.setColor(Color.DARK_GRAY);
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            float rectWidth = 400 * StaticBuffer.getScaleX();
                            float rectHeight = 50 * StaticBuffer.getScaleX();
                            float x = 0;
                            float y = centerY - rectHeight;
                            if (objective.getWorldIndex() != -1 && GameEngine.getGameCore().getMap().getUniqueIndex() != objective.getWorldIndex()) {
                                shapeRenderer.rect(x, y, rectWidth, rectHeight * 2);
                                shapeRenderer.end();
                                font.setColor(Color.WHITE);
                                font.getData().setScale(0.5f);
                                batch.begin();
                                batch.setColor(1, 1, 1, 0.5f);
                                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                                font.draw(batch, name, x + 10, y + rectHeight + 10);
                                font.draw(batch, "Надо перейти на карту с регистром - " + objective.getWorldIndex(), x + 10, y + rectHeight - 10);
                                batch.end();
                            } else if (distance > objective.getRange()) {
                                shapeRenderer.rect(x, y, rectWidth, rectHeight * 2);
                                shapeRenderer.end();
                                batch.begin();
                                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                                font.setColor(Color.WHITE);
                                font.setColor(1, 1, 1, 0.5f);
                                batch.setColor(1, 1, 1, 0.5f);
                                font.getData().setScale(0.5f);
                                font.draw(batch, name, x + 10, y + rectHeight + 10 - rectHeight * 2);
                                font.draw(batch, "Расстояние: " + distance, x + 10, y + rectHeight - 10);
                                batch.end();
                                float angle = subtractCameraAngle(GameCore.camera.direction, calculateAngle(StaticBuffer.getPlayerCooordinates().x, StaticBuffer.getPlayerCooordinates().z, destination.x, destination.z));
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
                                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                                batch.setColor(1, 1, 1, 0.5f);
                                batch.enableBlending();
                                font.draw(batch, name, x + 10, y + rectHeight * 2 - 10);

                                float textY = y + rectHeight * 2 - 40;
                                int offset = 0;
                                int[] kill = objective.getKill();

                                if (kill != null && kill.length > 1 && kill[0] > kill[1]) {
                                    font.draw(batch, "Убийства: " + kill[0] + "/" + kill[1], x + 10, textY - offset * 20);
                                    offset++;
                                }
                                int[] collect = objective.getCollect();
                                if (collect != null && collect.length > 1 && collect[0] > collect[1]) {
                                    font.draw(batch, "Сбор: " + collect[0] + "/" + collect[1], x + 10, textY - offset * 20);
                                    offset++;
                                }
                                int[][] tasks = objective.getTasks();
                                if (tasks != null && tasks[0][0] > tasks[0][1]) {
                                    font.draw(batch, objective.task_name, x + 10, textY - offset * 20);
                                }
                                if (GameEngine.gameCore.getMap().getUniqueIndex() == this.objectives.get(0).worldIndex) {
                                    isCompleted = true;
                                }
                                batch.end();
                                font.setColor(1, 1, 1, 1f);

                            }
                        }
                        @Override
                        public void Complete_objectives(int kill, int collect, int[] tasks){
                            if (GameEngine.gameCore.getMap().getUniqueIndex() == this.objectives.get(0).worldIndex) {
                                isCompleted = true;
                            }
                        }
                    };
                    Objective objective=new Objective(null,null,null,false, new Vector3(), 10000);
                    objective.worldIndex=3;
                    addQuest(quest);
                }
            });
            for(int i=0; i<quests.size(); i++){
                if(quests.get(i).getIndex()==0){
                    ArrayList<Objective> objectives=new ArrayList<>();
                    objectives.add(new Objective(null,null,null,false, new Vector3(),1f){
                        @Override
                        public void setComplete(int kill, int collect, int[] tasks, boolean isdest){
                            if(GameEngine.getGameCore().getMap().getUniqueIndex()==0){
                                if(this.destination.dst(StaticBuffer.getPlayerCooordinates())<range){
                                    iscompleted=true;
                                    setQuestsValue(0,0,new int[0]);
                                }
                            }
                        }
                    });
                    GameCore.setStartingCutscene();
                    quests.get(i).initialize(objectives,"Начало? (Плохая погода действует мне на нервы...)",0);

                } else if(quests.get(i).getIndex()==1){

                }
            }
        }
    }
    public boolean isQuest(int index){
        for(int i=0; i<completedQuests.size(); i++){
            if((int) completedQuests.get(i)==index){
                return false;
            }
        }
        return true;
    }
    public void addQuest(Quest quest){
        quests.add(quest);
    }
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        if(current_quest<quests.size() && current_quest>=0 && !quests.isEmpty()) {
            if(quests.get(current_quest).objectives.size()>quests.get(current_quest).current_objective) {
                quests.get(current_quest).drawObjectives(spriteBatch, shapeRenderer, StaticBuffer.fonts[0], quests.get(current_quest).objectives.get(quests.get(current_quest).current_objective), StaticBuffer.screenWidth, StaticBuffer.screenHeight);
            }
        } else if (!quests.isEmpty()) {
            current_quest=quests.size()-1;
        }
    }
    public void addCompleted(int unique_index){
        completedQuests.add(unique_index);
    }
    public void setQuestsValue(int kill, int collect, int[] tasks){
        for(int i=0; i<quests.size(); i++){
            quests.get(i).Complete_objectives(kill, collect, tasks);
        }
        for(int i=0; i<quests.size();i++){
            Quest quest = quests.get(i);
            if (quest.getIscompleted()) {
                quest.getRewards();
                completedQuests.add(quest.unique_index);
                quests.remove(i);
                i--;
            }
        }
    }
    public void update(){
        for(int i=0; i<quests.size();i++){
            Quest quest = quests.get(i);
            if (quest.getIscompleted()) {
                quest.getRewards();
            }
        }
        for(int i=0; i<quests.size();i++){
            Quest quest = quests.get(i);
            if (quest.getIscompleted()) {
                completedQuests.add(quest.unique_index);
                quests.remove(i);
                i--;
            }
        }
    }
}
