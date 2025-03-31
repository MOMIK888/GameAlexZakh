package com.bestproject.main.SoundManagement;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class CostumeSound implements Disposable {
    private final Music sound;
    private final boolean isMuffled;
    private final float maxVolume;
    private long soundId;

    public CostumeSound(Music sound, boolean isMuffled, float maxVolume) {
        this.sound = sound;
        this.isMuffled = isMuffled;
        this.maxVolume = maxVolume;
        sound.setPosition(80);
    }

    public void play() {
        sound.play();
        System.out.println(soundId + "SOUNDID");
    }
    public void update(Vector3 playerPosition, Vector3 soundPosition){
        if(isMuffled) {
            float distance = playerPosition.dst(soundPosition);
            float volume = isMuffled ? Math.max(0, 1 - (distance / maxVolume)) : 1;
            sound.setVolume(volume);
        }
    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}

