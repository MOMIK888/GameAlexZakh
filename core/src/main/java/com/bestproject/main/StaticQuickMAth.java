package com.bestproject.main;

import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;

public class StaticQuickMAth {
    public static float TimeMultiplier=1f;
    public static float time=0f;
    public static float counter;
    public static float gravityConstant=1.3f;
    public static float TrueGravity=-9.84f;
    public static float distance(float[] point1, float[] point2){

        float sum = 0.0f;
        for (int i = 0; i < point1.length; i++) {
            float diff = point1[i] - point2[i];
            sum += diff * diff;
        }

        return (float) Math.sqrt(sum);
    }
    public static float LinearFloatInterPolation(float value, float tagetValue, float coef){
        return (value - (value-tagetValue)*coef);
    }
    public static float getGravityAcceleration(){
        return gravityConstant*TimeMultiplier*GameCore.deltatime;
    }
    public static float distance(Vector3 point1, Vector3 point2){
        float sum = 0.0f;
        float diff = point1.x - point2.x;
        sum += diff * diff;
        diff = point1.z - point2.z;
        sum += diff * diff;
        return (float) Math.sqrt(sum);
    }
    public static float distance3d(Vector3 point1, Vector3 point2){
        float sum = 0.0f;
        float diff = point1.x - point2.x;
        sum += diff * diff;
        diff = point1.z - point2.z;
        sum += diff * diff;
        sum= (float) Math.sqrt(sum);
        diff=point1.y - point2.y;
        sum= (float) Math.sqrt(sum*sum+diff*diff);
        return sum;
    }
    public static float move(float value){
        return value*TimeMultiplier;
    }
    public static float getAngle(Vector3 one, Vector3 two){
        float[] sincos=StaticBuffer.getSin_Cos(one,two);
        return (float) Math.toDegrees(Math.atan2(sincos[0],sincos[1]));
    }
    public static void setTimeFlow(float newTimeFlow, float cnt){
        if(counter<=0){
            TimeMultiplier=newTimeFlow;
            counter=cnt;
        }
    }
    public static void setTimeFlow(float newTimeFlow, float cnt, boolean force) {
        TimeMultiplier = newTimeFlow;
        counter = cnt;
    }
    public static void updateTime(){
        time+=0.005f*TimeMultiplier;
        time=time%24;
        setTimeFlow(1f, 0);
        counter-= GameEngine.getGameCore().deltatime;
    }
    public static Vector3 RotateAroundPivot(Vector3 point, Vector3 pivot, Vector3 axis, float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float distance=distance(point, pivot);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float x = point.x+distance*sin-distance;
        float z = point.z+distance*cos;
        point.x = x;
        point.z = z;

        return point;
    }

    public static Vector3 RotateAroundPivotX(Vector3 point, Vector3 pivot, float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float distance=distance(point, pivot);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        point.y+=distance*sin;
        return  point;
    }
    public static void normalizeSpeed(Vector3 direction) {
        float x = direction.x;
        float y = direction.y;
        float z = direction.z;
        float xzMagnitude = (float) Math.sqrt(x * x + z * z);
        float desiredXZMagnitude = (float) Math.sqrt(1 - y * y);
        if (xzMagnitude != 0) {
            float scale = desiredXZMagnitude / xzMagnitude;
            direction.x = x * scale;
            direction.z = z * scale;
        }
    }
    public static int MaxIndex(Integer[] array){
        int max=array[0];
        int maxIndex=0;
        for(int i=1; i<array.length; i++){
            if(array[i]>max){
                max=array[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }
    public static int MinIndex(Integer[] array){
        int min=array[0];
        int minIndex=0;
        for(int i=1; i<array.length; i++){
            if(array[i]<min){
                min=array[i];
                minIndex=i;
            }
        }
        return minIndex;
    }
    public static Vector3 getMultipVec(Vector3 vector, float value){
        return new Vector3(vector.x*value,vector.y*value,vector.z*value);
    }
}
