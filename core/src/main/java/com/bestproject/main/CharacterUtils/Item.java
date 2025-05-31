package com.bestproject.main.CharacterUtils;

public class Item {
    int quanity;

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public int getUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(int uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int uniqueIndex;
    String name;
    public static enum Items {
        TESTITEM(0),
        WOOD(1),
        KEY(2),
        VOIDHEART(3),
        APPLE(4),
        CRYSTAL(5);

        private int value;
        private Items(int value) {
            this.value = value;
        }
        public int getPreset() {
            return value;
        }
    }
    public Item(int uniqueIndex, int quanity, String name){
        this.uniqueIndex=uniqueIndex;
        this.quanity=quanity;
        this.name=name;
    }

}
