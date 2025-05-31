package com.bestproject.main.Quests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MainGame;
import com.bestproject.main.StaticBuffer;

import java.util.ArrayList;
import java.util.Iterator;

public class QuestManager {
    ArrayList<Quest> quests=new ArrayList<>();
    int current_quest=0;
    ArrayList<Integer> completedQuests=new ArrayList<>();
    public QuestManager(){
        if(MainGame.databaseInterface[1].getInfo(0)==null){
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
        Iterator<Quest> iterator = quests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (quest.getIscompleted()) {
                quest.getRewards();
                completedQuests.add(quest.unique_index);
                iterator.remove();
            }
        }
    }
}
