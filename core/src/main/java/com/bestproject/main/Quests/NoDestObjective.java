package com.bestproject.main.Quests;

public class NoDestObjective extends Objective{
    public NoDestObjective(int[] kill, int[] collect, int[][] tasks, boolean iscompleted, int[] destination, int range) {
        super(kill, collect, tasks, iscompleted, destination, range);
    }

    @Override
    public void setComplete(int kill, int collect, int[] tasks, boolean isdest) {
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
        iscompleted=comp[0]&&comp[1]&&comp[2];
    }
}

