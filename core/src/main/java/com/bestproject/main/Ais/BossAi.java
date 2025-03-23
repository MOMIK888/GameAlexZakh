package com.bestproject.main.Ais;

import com.badlogic.gdx.math.MathUtils;
import com.bestproject.main.Enemies.FlameBoss;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;

public class BossAi extends AI{
    float attackCooldown=4f;
    int attack=0;
    public void analyze(FlameBoss flameBoss) {
        if(attackCooldown<=0){
            attack= MathUtils.random(2);
            flameBoss.setAttack(attack);

            if(attack==0){
                attackCooldown=5f;
            } else if(attack==1){
                attackCooldown=3f;
            }
        }
    }

    public void update() {
        attackCooldown-= StaticQuickMAth.move(GameCore.deltatime);
    }
}
