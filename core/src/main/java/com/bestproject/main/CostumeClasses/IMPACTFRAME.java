package com.bestproject.main.CostumeClasses;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class IMPACTFRAME {
    Color color = Color.WHITE;
    Color secondaryColor = Color.RED; // Second color for lines from the direction vector

    public void renderLines(Vector2 impactPosition, Vector2 direction, ShapeRenderer shapeRenderer, float screenWidth, float screenHeight) {
        int numberOfLines = 300; // Number of lines to render
        float baseLength = 400f; // Base length of the lines
        float maxThickness = 10f; // Maximum thickness of the lines
        float maxTilt = 30f; // Maximum tilt angle in degrees

        // Calculate the angle between impactPosition and direction vectors
        Vector2 delta = direction.cpy().sub(impactPosition);
        float angle = delta.angleRad(); // Angle in radians

        // Calculate the perpendicular angle (90 degrees or PI/2 radians)
        float perpendicularAngle = angle + MathUtils.PI / 2;

        // Draw lines perpendicular to the angle between impactPosition and direction
        shapeRenderer.setColor(color);
        drawLines(shapeRenderer, screenWidth, screenHeight, numberOfLines, baseLength, maxThickness, maxTilt, perpendicularAngle);
        numberOfLines = 200;
        maxThickness = 3f;
        // Draw lines from the direction vector (same angle as between impactPosition and direction)
    }

    private void drawLines(ShapeRenderer shapeRenderer, float screenWidth, float screenHeight, int numberOfLines, float baseLength, float maxThickness, float maxTilt, float angle) {
        for (int i = 0; i < numberOfLines; i++) {
            // Randomize the position of the line on the screen
            float startX = MathUtils.random(0, screenWidth);
            float startY = MathUtils.random(0, screenHeight);

            // Randomize the length of the line
            float length = baseLength * MathUtils.random(0.5f, 1.5f);

            // Randomize the thickness of the line
            float thickness = MathUtils.random(1f, maxThickness);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

            // Randomize the tilt of the line (slight deviation from the main angle)
            float tilt = MathUtils.random(-maxTilt, maxTilt);
            float tiltRad = tilt * MathUtils.degreesToRadians;

            // Calculate the end position of the line based on the angle and tilt
            float endX = startX + length * MathUtils.cos(angle + tiltRad);
            float endY = startY + length * MathUtils.sin(angle + tiltRad);
            shapeRenderer.rectLine(startX, startY, endX, endY, thickness);
            int iterations=MathUtils.random(30)+10;
            for(int j=0; j<iterations; j++){

                float[][] vertexes=new float[][]{
                    {startX+MathUtils.random(5f)+length/iterations*i * MathUtils.cos(angle + tiltRad),startY+MathUtils.random(5f)+length/iterations*i * MathUtils.sin(angle + tiltRad)},
                    {startX-MathUtils.random(5f)+length/iterations*i * MathUtils.cos(angle + tiltRad),startY-MathUtils.random(5f)+length/iterations*i * MathUtils.sin(angle + tiltRad)},
                    {startX+MathUtils.random(-5f,5f)+length/iterations*i * MathUtils.cos(angle + tiltRad),startY-MathUtils.random(-5,5f)+length/iterations*i * MathUtils.sin(angle + tiltRad)},
                    {startX+MathUtils.random(-5f,5f)+length/iterations*i * MathUtils.cos(angle + tiltRad),startY-MathUtils.random(-5,5f)+length/iterations*i * MathUtils.sin(angle + tiltRad)}
                };
                shapeRenderer.triangle(vertexes[0][0],vertexes[0][1],vertexes[1][0],vertexes[1][1],vertexes[2][0],vertexes[2][1]);
                shapeRenderer.triangle(vertexes[0][0],vertexes[0][1],vertexes[1][0],vertexes[1][1],vertexes[3][0],vertexes[3][1]);
            }
        }
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor.set(secondaryColor);
    }
}
