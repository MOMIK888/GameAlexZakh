package com.bestproject.main.CreativeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MainGame;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.StaticBuffer;

public class CreativeMode implements Disposable {
    ShowHitbox showHitbox;
    Array<HITBOX> temporaryHitboxBuffer = new Array<>();
    int currentHitboxIndex = 0;
    float precision=1f;
    int currentMapIndex=3;
    int inversion=1;
    Array<Button> buttons = new Array<>();

    public CreativeMode() {
        this.temporaryHitboxBuffer=unpack(MainGame.databaseInterface[0].getInfo(currentMapIndex));
        System.out.println(MainGame.databaseInterface[0].getInfo(currentMapIndex));
        showHitbox=new ShowHitbox();
        showHitbox.hbRenderer.setGradientTexture(TextureUtils.createBlueFrameTexture(64,64));
        buttons.add(new Button("AddHb +", 50, 800) {
            @Override
            public void onPress() {
                temporaryHitboxBuffer.add(new HITBOX(1f,1f,1f,1f,1f,1f));
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
        buttons.add(new Button("Copy", 100, 900) {
            @Override
            public void onPress() {
                if(temporaryHitboxBuffer.size>0){
                temporaryHitboxBuffer.add(new HITBOX(temporaryHitboxBuffer.get(currentHitboxIndex).x,temporaryHitboxBuffer.get(currentHitboxIndex).y,temporaryHitboxBuffer.get(currentHitboxIndex).z,temporaryHitboxBuffer.get(currentHitboxIndex).width,temporaryHitboxBuffer.get(currentHitboxIndex).thickness,temporaryHitboxBuffer.get(currentHitboxIndex).height));
                currentHitboxIndex=temporaryHitboxBuffer.size-1;
                }
            }
        });
        buttons.add(new Button("Rotate", 250, 900) {
            @Override
            public void onPress() {
                if(temporaryHitboxBuffer.size>0){
                    float th=(float)temporaryHitboxBuffer.get(currentHitboxIndex).thickness;
                    temporaryHitboxBuffer.get(currentHitboxIndex).thickness=temporaryHitboxBuffer.get(currentHitboxIndex).width;
                    temporaryHitboxBuffer.get(currentHitboxIndex).width=th;
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
                if(precision-0.1f>0) {
                    precision -= 0.1f * inversion;
                } else{
                    if(inversion==1){
                        precision/=2;
                    } else{
                        precision*=2;
                    }
                }
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
                saveConfiguration(ConvertHitboxesIntoString(temporaryHitboxBuffer));
            }
        });
    }

    public void draw() {
        for (Button button : buttons) {
            button.draw();
        }
    }

    public void onTouch(int pointer, float touchx, float touchy) {
        for (Button button : buttons) {
            if (button.onTouch(touchx, touchy)) {
                button.onPress();
                break;
            }
        }
        buttons.get(7).label="ChangePresizion" + String.valueOf(precision);
    }

    public void onDrag(int pointer, float touchx, float touchy) {
    }

    public void saveConfiguration(String info) {
        MainGame.databaseInterface[0].setInfo(currentMapIndex,info);
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
    public Array<HITBOX> unpack(String packedInfo) {
        Array<HITBOX> hitboxArray = new Array<>();

        if (packedInfo == null || packedInfo.isEmpty()) return hitboxArray;

        String[] hitboxEntries = packedInfo.split("\\^");
        for (String entry : hitboxEntries) {
            if (entry.trim().isEmpty()) continue;

            String[] parts = entry.split("&");
            if (parts.length < 3) continue;

            String[] pos = parts[0].split("\\$");
            String[] size = parts[1].split("\\$");
            String type = parts[2];

            if (pos.length < 3 || size.length < 3) continue;

            HITBOX hitbox = new HITBOX(0,0,0,0,0,0);
            hitbox.x = Float.parseFloat(pos[0]);
            hitbox.y = Float.parseFloat(pos[1]);
            hitbox.z = Float.parseFloat(pos[2]);

            hitbox.width = Math.abs(Float.parseFloat(size[0]));
            hitbox.thickness = Math.abs(Float.parseFloat(size[1]));
            hitbox.height = Math.abs(Float.parseFloat(size[2]));

            hitbox.type = Integer.valueOf(type);

            hitboxArray.add(hitbox);
        }

        return hitboxArray;
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
