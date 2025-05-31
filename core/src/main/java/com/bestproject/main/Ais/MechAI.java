package com.bestproject.main.Ais;

import com.badlogic.gdx.math.MathUtils;
import com.bestproject.main.Enemies.FlameBoss;
import com.bestproject.main.Enemies.Mech;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticQuickMAth;
public class MechAI extends AI{
    float attackCooldown=4f;
    int attack=0;
    float UltCount=30;
    public void analyze(Mech mech) {
        if(attackCooldown<=0){
            attack= MathUtils.random(0);
            if(UltCount<=0){
                UltCount=30+MathUtils.random(0,20);
                attack=-1;
            }

            if(attack==0){
                attackCooldown=15f;
            } else if(attack==1){
                attackCooldown=3f;
            }
            mech.setAttack(attack);
        }
    }

    public void update() {
        attackCooldown-= StaticQuickMAth.move(GameCore.deltatime);
        UltCount-=StaticQuickMAth.move(GameCore.deltatime);
    }
}
