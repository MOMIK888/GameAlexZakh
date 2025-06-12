package com.bestproject.main.UiParts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Quests.Quest;
import com.bestproject.main.Quests.QuestManager;
import com.bestproject.main.StaticBuffer;


public class QuestingUi {
    Rectangle baseRect;
    public int currentQuestIndex;
    BitmapFont font;

    public QuestingUi(BitmapFont bitmapFont, QuestManager questManager) {
        float scale = StaticBuffer.getScaleX();
        baseRect = new Rectangle(50 * scale, 100 * scale, 400 * scale, 100 * scale);
        font = bitmapFont;
        currentQuestIndex=questManager.current_quest;
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, QuestManager questManager) {
        font.getData().setScale(1f);
        float scale = StaticBuffer.getScaleX();
        float spacing = 20 * scale;
        float rectWidth = baseRect.width;
        float rectHeight = baseRect.height;
        float x = baseRect.x;
        float y = baseRect.y;

        for (int i = 0; i < questManager.quests.size(); i++) {
            float offsetY = i * (rectHeight + spacing);
            Rectangle rect = new Rectangle(x, y - offsetY, rectWidth, rectHeight);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(i == currentQuestIndex ? Color.WHITE : Color.BLACK);
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        shapeRenderer.end();
        shapeRenderer.begin();
        spriteBatch.setColor(new Color(Color.WHITE));
        font.setColor(Color.WHITE);
        for (int i = 0; i < questManager.quests.size(); i++) {
            float offsetY = i * (rectHeight + spacing);
            float textX = x + 10 * scale;
            float textY = y - offsetY + rectHeight / 2+10*scale;
            Quest quest = questManager.quests.get(i);
            font.draw(spriteBatch, quest.name, textX, textY);
            System.out.println(quest.name);
        }
    }

    public void OnTouch(QuestManager questManager, float touchx, float touchy, int pointer) {
        float scale = StaticBuffer.getScaleX();
        float spacing = 20 * scale;
        float rectHeight = baseRect.height;

        for (int i = 0; i < questManager.quests.size(); i++) {
            float offsetY = i * (rectHeight + spacing);
            Rectangle rect = new Rectangle(baseRect.x, baseRect.y - offsetY, baseRect.width, rectHeight);
            if (rect.contains(touchx, touchy)) {
                currentQuestIndex = i;
                questManager.current_quest=i;
                break;
            }
        }
    }

    public void OnDrag(QuestManager questManager, float touchx, float touchy, int pointer) {
    }

    public void OnUp(QuestManager questManager, float touchx, float touchy, int pointer) {
    }
}

