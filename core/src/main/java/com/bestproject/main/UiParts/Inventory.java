package com.bestproject.main.UiParts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.bestproject.main.CharacterUtils.Item;
import com.bestproject.main.Main;
import com.bestproject.main.MainGame;

public class Inventory {
    Array<Item> items=new Array<>();
    Texture[] temptextureBuffer;
    public Inventory(){

    }
    public Array<Item> decipherItems(String info){
        if(info==null || info.isEmpty()){
            return new Array<>();
        }
        Array<Item> items1=new Array<>();
        String[] splitInfo=info.split("\\^");
        for(int i=0; i<splitInfo.length; i++){
            if(splitInfo[i]!=null) {
                if (!splitInfo[i].isEmpty() && splitInfo[i] != null) {
                    String[] splitsplitinfo = splitInfo[i].split("\\$");
                    items1.add(new Item(Integer.valueOf(splitsplitinfo[0]), Integer.valueOf(splitsplitinfo[1]), splitsplitinfo[2]));
                }
            }

        }
        return items1;
    }
    public String codeInfo(){
        String finalInfo="";
        for(Item i: items){
            String info="";
            info+=i.getUniqueIndex();
            info+="$";
            info+=i.getQuanity();
            info+="$";
            info+=i.getName();
            info+="^";
            finalInfo+=info;
        }
        MainGame.databaseInterface[2].setInfo(2,finalInfo);
        return finalInfo;
    }
    public void decipher(String info) {
        if(info==null || info.isEmpty() || info.length()<1){
            return;
        }
        items.clear();
        String[] itemEntries = info.split("\\^");
        for (String entry : itemEntries) {
            if (entry.isEmpty()) {
                continue;
            }
            String[] components = entry.split("\\$");
            if (components.length >= 3) {
                try {
                    int uniqueIndex = Integer.parseInt(components[0]);
                    int quantity = Integer.parseInt(components[1]);
                    String name = components[2];
                    items.add(new Item(uniqueIndex, quantity, name));
                } catch (NumberFormatException e) {
                    System.err.println("" + entry);
                }
            }
        }
    }
    public boolean checkSubtraction(int uniqueIndex, int amount){
        boolean ans=false;
        int index=-1;
        for(int i=0; i<items.size; i++){
            if(items.get(i).getUniqueIndex()==uniqueIndex){
                ans=true;
                index=i;
                break;
            }
        }
        if (!ans){
            return false;
        }

        if(items.get(index).getQuanity()<amount){
            return false;
        }
        return true;
    }
    public boolean subtract(int uniqueIndex, int amount){
        int index=-1;
        for(int i=0; i<items.size; i++){
            if(items.get(i).getUniqueIndex()==uniqueIndex){
                index=i;
                break;
            }
        }
        if(index==-1){
            return false;
        }
        items.get(index).setQuanity(items.get(index).getQuanity()-amount);
        if(items.get(index).getQuanity()<1){
            items.removeIndex(index);
        }
        return true;
    }
    public boolean addItem(Item item){
        boolean ans=false;
        for(int i=0; i<items.size; i++){
            if(items.get(i).getUniqueIndex()==item.getUniqueIndex()){
                items.get(i).setQuanity(items.get(i).getQuanity()+item.getQuanity());
                return true;
            }
        }
        items.add(item);
        return true;
    }
    public void displayInventory(SpriteBatch spriteBatch){

    }
}
