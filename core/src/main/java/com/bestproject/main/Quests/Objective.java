package com.bestproject.main.Quests;

import com.badlogic.gdx.math.Vector3;

public class Objective {
    int[] kill;
    int[] collect;
    int[][] tasks;
    boolean iscompleted=false;
    Vector3 destination;
    int takamount=3;
    float range;
    String task_name="";
    int worldIndex=-1;

    public Objective(int[] kill, int[] collect, int[][] tasks, boolean iscompleted, Vector3 destination, float range) {
        this.kill = kill;
        this.collect = collect;
        this.tasks = tasks;
        this.iscompleted = iscompleted;
        this.destination = destination;
        this.range = range;
        if(kill!=null) {
            if (this.kill[0] <= this.kill[1]) {
                takamount -= 1;

            }
        }
        if(collect!=null) {
            if (this.collect[0] <= this.collect[1]) {
                takamount -= 1;
            }
        }
        if(this.tasks!=null) {
            if(this.tasks[0][0] <= this.tasks[0][1]){
                takamount-=1;
            }
        } else {
            takamount-=1;
        }
    }
    public int getWorldIndex(){
        return worldIndex;
    }
    public Objective(int[] kill, int[] collect, int[][] tasks, boolean iscompleted, Vector3 destination, float range, int worldIndex) {
        this.kill = kill;
        this.collect = collect;
        this.tasks = tasks;
        this.iscompleted = iscompleted;
        this.destination = destination;
        this.range = range;
        if(kill!=null) {
            if (this.kill[0] <= this.kill[1]) {
                takamount -= 1;

            }
        }
        if(collect!=null) {
            if (this.collect[0] <= this.collect[1]) {
                takamount -= 1;
            }
        }
        if(this.tasks!=null) {
            if(this.tasks[0][0] <= this.tasks[0][1]){
                takamount-=1;
            }
        } else {
            takamount-=1;
        }
        this.worldIndex=worldIndex;
    }

    public float getRange(){
        return range;
    }
    public int getTakamount(){
        return takamount;
    }

    public void setComplete(int kill, int collect, int[] tasks, boolean isdest){
        boolean[] comp=new boolean[3];
        takamount=3;
        if(this.kill[0]>this.kill[1]) {
            this.kill[1] += kill;
            if(this.kill[0]<=this.kill[1]){
                takamount-=1;
                comp[0]=true;
            }
        } else{
            takamount-=1;
            comp[0]=true;
        }
        if(this.collect[0]>this.collect[1]) {
            this.collect[1] += collect;
            iscompleted=false;
            if(this.collect[0]<=this.collect[1]){
                takamount-=1;
                comp[1]=true;
            }
        } else {
            takamount-=1;
            comp[1]=true;
        }
        if(this.tasks!=null && tasks!=null) {
            if (this.tasks[0][0] > this.tasks[0][1]) {
                if(this.tasks[0][2] == tasks[1]){
                    this.tasks[0][1] += tasks[0];
                }
                iscompleted = false;
                if(this.tasks[0][0] <= this.tasks[0][1]){
                    takamount-=1;
                    comp[2]=true;
                }
            } else{
                takamount-=1;
                comp[2]=true;
            }
        } else {
            takamount-=1;
            comp[2]=true;
        }
        iscompleted=comp[0]&&comp[1]&&comp[2]&&isdest;
    }
    public void setTask_name(String task_name){
        this.task_name=task_name;
    }
    public boolean getIscompleted(){
        return iscompleted;
    }
    public Vector3 getDestination(){
        return destination;
    }
    public int[] getKill(){
        return kill;
    }
    public int[] getCollect() {
        return collect;
    }
    public boolean expire(){
        return false;
    }

    public int[][] getTasks() {
        return tasks;
    }


}
