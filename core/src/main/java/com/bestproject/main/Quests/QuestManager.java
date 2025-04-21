package com.bestproject.main.Quests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Iterator;

public class QuestManager {
    ArrayList<Quest> quests=new ArrayList<>();
    int current_quest=0;
    ArrayList<Integer> completedQuests=new ArrayList<>();
    public QuestManager(){
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
        if(current_quest<quests.size() && current_quest>=0) {
            quests.get(current_quest).drawObjectives(spriteBatch, shapeRenderer);
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
