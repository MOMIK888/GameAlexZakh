package com.bestproject.main.Quests;

import com.badlogic.gdx.math.Vector3;

public class NewBieQuest extends Quest{
    public NewBieQuest(Vector3 dest, int unique_index){
        this.unique_index=0;
        objectives.add(new Objective(new int[]{0,0},
            new int[]{0,0},
            new int[][]{{1,0,unique_index}},
            false,
            new Vector3(dest),
            10
        ));
        objectives.get(0).setTask_name("Поговорить");
        name="Начало";
    }
    @Override
    public void Complete_objectives(int kill, int collect, int[] tasks){
        if(true){
            objectives.get(current_objective).setComplete(kill,collect,tasks,true);
            if(objectives.get(current_objective).getIscompleted()){
                if(current_objective<objectives.size()-1){
                    current_objective+=1;
                } else{
                    isCompleted=true;
                }
            }
        }
    }
}
