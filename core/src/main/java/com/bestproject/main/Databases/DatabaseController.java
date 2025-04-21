package com.bestproject.main.Databases;

import com.bestproject.main.Databases.MapInfo.MapInfo;
import com.bestproject.main.Moveset.PlayerInfoBreakdown;

public class DatabaseController {
    PlayerInfoDatabase playerInfoDatabase;
    WorldDatabse worldDatabase;
    QuestDatabase questDatabase;
    MapInfo mapInfo;
    public  DatabaseController(){
        playerInfoDatabase=new PlayerInfoDatabase();
        worldDatabase=new WorldDatabse();
        questDatabase=new QuestDatabase();
        mapInfo=new MapInfo();
    }
    public void updateCharacterDb(int charid, String info){
        playerInfoDatabase.updateCharacterInfo(charid, info);
    }
    public PlayerInfoBreakdown getPlayerInfo(int charIndex){
        return new PlayerInfoBreakdown(playerInfoDatabase.getCharacterInfo(charIndex));
    }
    public void updateMapDb(int id, String info){
        mapInfo.updateCharacterInfo(id, info);
    }
    public String getMapInfo(int Index){
        return mapInfo.getCharacterInfo(Index);
    }

}
