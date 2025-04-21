package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class CreativeMode implements Disposable {
    ShowHitbox showHitbox;
    Array<HITBOX> temporaryHitboxBuffer = new Array<>();
    int currentHitboxIndex = 0;
    float precision=1f;
    int currentMapIndex=1;
    int inversion=1;
    Array<Button> buttons = new Array<>();

    public CreativeMode() {
        showHitbox=new ShowHitbox();
        showHitbox.hbRenderer.setGradientTexture(TextureUtils.createBlueFrameTexture(64,64));
        buttons.add(new Button("AddHb +", 50, 800) {
            @Override
            public void onPress() {
                temporaryHitboxBuffer.add(new HITBOX(1f,1d,1f,1f,1f,1f));
            }
        });
        buttons.add(new Button("DelHb -", 150, 800) {
            @Override
            public void onPress() {
                if (temporaryHitboxBuffer.size > 0){
                    temporaryHitboxBuffer.removeIndex(currentHitboxIndex);
                    currentHitboxIndex-=1;
                    if(currentHitboxIndex<0){
                        currentHitboxIndex=0;
                    }
                }
            }
        });
        buttons.add(new Button("Hitbox -", 50, 600) {
            @Override
            public void onPress() {
                if (temporaryHitboxBuffer.size > 0)
                    currentHitboxIndex = (currentHitboxIndex - 1 + temporaryHitboxBuffer.size) % temporaryHitboxBuffer.size;
            }
        });
        buttons.add(new Button("Hitbox +", 150, 600) {
            @Override
            public void onPress() {
                if (temporaryHitboxBuffer.size > 0)
                    currentHitboxIndex = (currentHitboxIndex + 1) % temporaryHitboxBuffer.size;
            }
        });
        buttons.add(new Button("Inverse", 300, 400) {
            @Override
            public void onPress() {
                inversion*=-1;
            }
        });
        buttons.add(new Button("ChangePresizion" + String.valueOf(precision), 150, 350) {
            @Override
            public void onPress() {
                precision-=0.1f*inversion;
            }
        });

        buttons.add(new Button("Resize X", 1800, 900) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.resize(precision*inversion,0,0);
            }
        });
        buttons.add(new Button("Resize Y", 1600, 900) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.resize(0,precision*inversion,0);
            }
        });
        buttons.add(new Button("Resize Z", 1400, 900) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.resize(0,0,precision*inversion);
            }
        });

        buttons.add(new Button("Move X", 1800, 700) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.move(precision*inversion, 0, 0);
            }
        });
        buttons.add(new Button("Move Y", 1600, 700) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.move(0, precision*inversion, 0);
            }
        });
        buttons.add(new Button("Move Z", 1400, 700) {
            @Override
            public void onPress() {
                HITBOX hitbox = getCurrentHitbox();
                if (hitbox != null) hitbox.move(0, 0, precision*inversion);
            }
        });


        buttons.add(new Button("Save", 1800, 100) {
            @Override
            public void onPress() {
                saveConfiguration();
            }
        });
    }

    public void draw() {
        for (Button button : buttons) {
            button.draw();
        }
    }

    public void onTouch(int pointer, float touchx, float touchy) {
        buttons.get(5).label="ChangePresizion" + String.valueOf(precision);
        for (Button button : buttons) {
            if (button.onTouch(touchx, touchy)) {
                button.onPress();
                break;
            }
        }
    }

    public void onDrag(int pointer, float touchx, float touchy) {
    }

    public void saveConfiguration() {
        StaticBuffer.databaseController.updateMapDb(currentMapIndex,ConvertHitboxesIntoString(temporaryHitboxBuffer));
    }
    public String ConvertHitboxesIntoString(Array<HITBOX> hitboxArray){
        String finalInfo="";
        for(int i=0; i<hitboxArray.size; i++){
            String info="";
            info+=hitboxArray.get(i).x+"$"+hitboxArray.get(i).y+"$"+hitboxArray.get(i).z+"&";
            info+=hitboxArray.get(i).width+"$"+hitboxArray.get(i).thickness+"$"+hitboxArray.get(i).height+"&";
            info+=hitboxArray.get(i).type+"&";
            finalInfo+=info+"^";
        }
        return finalInfo;

    }

    private HITBOX getCurrentHitbox() {
        if (temporaryHitboxBuffer.size == 0) return null;
        return temporaryHitboxBuffer.get(currentHitboxIndex);
    }

    @Override
    public void dispose() {
    }
    private abstract class Button {
        float x, y;
        float radius = 40;
        String label;

        public Button(String label, float x, float y) {
            this.label = label;
            this.x = x;
            this.y = y;
        }

        public boolean onTouch(float touchX, float touchY) {
            float dx = touchX - x;
            float dy = touchY - y;
            return dx * dx + dy * dy <= radius * radius;
        }


        public void draw() {
            ShapeRenderer shapeRenderer = StaticBuffer.TestShapeRenderer;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.circle(x, y, radius);
            shapeRenderer.end();

            StaticBuffer.spriteBatch.begin();
            StaticBuffer.fonts[0].draw(StaticBuffer.spriteBatch, label, x - radius / 2, y + 5);
            StaticBuffer.spriteBatch.end();
            for(int i=0; i<temporaryHitboxBuffer.size; i++) {
                if(i!=currentHitboxIndex) {
                    StaticBuffer.showHitbox.cDec(StaticBuffer.decalBatch, temporaryHitboxBuffer.get(i));
                }
            }
            if(temporaryHitboxBuffer.size>0 && currentHitboxIndex<temporaryHitboxBuffer.size){
                showHitbox.cDec(StaticBuffer.decalBatch,temporaryHitboxBuffer.get(currentHitboxIndex));
            }
        }

        public abstract void onPress();
    }
}
