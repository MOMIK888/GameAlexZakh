package com.bestproject.main.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bestproject.main.Game.DatabaseInterface;
import com.bestproject.main.Main;
import com.bestproject.main.MainGame;
import com.bestproject.main.android.Databases.MapInfo;
import com.bestproject.main.android.Databases.PlayerInfo;
import com.bestproject.main.android.Databases.QuestDatabase;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;
        initialize(new MainGame(new DatabaseInterface[]{new MapInfo(this.getApplicationContext()),new QuestDatabase(this.getApplicationContext()),new PlayerInfo(this.getApplicationContext() )}), configuration);
    }
}
