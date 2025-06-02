package com.bestproject.main.SoundManagement;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements Disposable {
    private final Map<String, CostumeSound> temporarySounds = new HashMap<>();
    private final Map<String, CostumeSound> constantSounds = new HashMap<>();
    int currentStrengh=-1;

    public void addTemporarySound(String key, CostumeSound sound) {
        temporarySounds.put(key, sound);
    }

    public void addConstantSound(String key, CostumeSound sound) {
        constantSounds.put(key, sound);
        sound.play();
    }

    public void playSound(String key, Vector3 playerPosition, Vector3 soundPosition) {
        CostumeSound sound = temporarySounds.get(key);

        if (sound != null) {
            sound.play();
        } else{
            System.out.println("NULLSOUND");
        }
    }
    public void playSoundConstant(String key) {
        CostumeSound sound = constantSounds.get(key);
        if (sound != null) {
            if(sound.getStrengh()>=currentStrengh) {
                sound.play();
            }
        }else{
            System.out.println("NULLSOUND");
        }

    }
    public void StopAllCons(){
        for (CostumeSound sound : constantSounds.values()) {
            sound.stop();
        }
    }

    public void update(String key, Vector3 playerPosition, Vector3 soundPosition){
        CostumeSound sound = constantSounds.get(key);
        sound.update(playerPosition,soundPosition);
    }

    @Override
    public void dispose() {
        for (CostumeSound sound : temporarySounds.values()) {
            sound.dispose();
        }
        for (CostumeSound sound : constantSounds.values()) {
            sound.dispose();
        }
        temporarySounds.clear();
        constantSounds.clear();
    }
}
