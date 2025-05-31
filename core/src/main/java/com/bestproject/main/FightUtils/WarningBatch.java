package com.bestproject.main.FightUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.bestproject.main.CostumeClasses.CircularWarning;
import com.bestproject.main.CostumeClasses.GradientDecalWarning;

public class WarningBatch {
    CircularWarning circularWarning=new CircularWarning(Color.RED,Color.RED);
    GradientDecalWarning gradientDecalWarning=new GradientDecalWarning(Color.RED,Color.RED);
    Decal[] circular;
    Decal[] linear;
    int currentLinear, currentCircular;
    public WarningBatch(){
        currentLinear=0;
        currentCircular=0;
        circular=new Decal[10];
        linear=new Decal[10];
        for(int i=0; i<circular.length; i++){
            circular[i]=Decal.newDecal(new TextureRegion(circularWarning.getGradientTexture()),true);
            Color newColor=new Color(circular[i].getColor());
            newColor.a=0.5f;
            circular[i].setColor(newColor);
        }
        for(int i=0; i<linear.length; i++){
            linear[i]=Decal.newDecal(new TextureRegion(gradientDecalWarning.getGradientTexture()),true);
            Color newColor=new Color(linear[i].getColor());
            newColor.a=0.5f;
            linear[i].setColor(newColor);
        }
    }
    public void render(DecalBatch decalBatch, InstanceOfWarning instance){
        Decal decal;
        currentLinear+=1;
        currentCircular+=1;
        currentCircular%=10;
        currentLinear%=10;
        if (instance.type == InstanceOfWarning.Type.CIRCULAR) {
            if (currentCircular >= circular.length) return; // no available decals
            decal = circular[currentCircular++];
        } else {
            if (currentLinear >= linear.length) return; // no available decals
            decal = linear[currentLinear++];
        }

        // Apply position, size, and rotation
        decal.setPosition(instance.position);
        decal.setRotationZ(instance.rotation);
        decal.setScale(instance.width / decal.getWidth(), instance.height / decal.getHeight());

        decalBatch.add(decal);

    }

}
