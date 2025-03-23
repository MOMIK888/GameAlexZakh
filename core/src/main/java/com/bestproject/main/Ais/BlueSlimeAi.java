package com.bestproject.main.Ais;

public class BlueSlimeAi extends AI{
    float maxFrames;
    public BlueSlimeAi(float MaxFrames){
        maxFrames=MaxFrames;
    }
    @Override
    public boolean check(float cnt){
        return (cnt>=maxFrames);
    }
}
