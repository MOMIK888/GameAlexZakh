package com.bestproject.main.ObjectFragment;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class HITBOX {
    public double x, y, z;
    public double width, thickness, height;
    public double rotation;
    public int type=0;
    public static enum HITBOXTYPES {
        DEFAULT(0),
        CLIMBABLE(1),
        FLOOR(2),
        ENEMY_OR_UNCLIMBABLE(3);

        private final int value;
        private HITBOXTYPES(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public HITBOX(double x, double y, double z, double width, double thickness, double height) {

        this.x = x;
        this.y = y;
        this.z=z;
        this.width = width;
        this.thickness = thickness;
        this.height=height;
        this.rotation = 0.0;
    }
    public void resize(float x, float y, float z){
        this.width+=x;
        this.thickness+=y;
        this.height+=z;
    }
    public void move(float x, float y, float z){
        this.x+=x;
        this.y+=y;
        this.z+=z;
    }
    public void setType(int type){
        this.type=type;
    }
    public int geType(){
        return type;
    }
    public void setX(double x){
        this.x=x;
    }
    public void setY(double y){
        this.y=y;
    }
    public double getBottom(){
        return z-height/2;
    }
    public double getTop(){
        return z+height/2;
    }
    public void setZ(double z){
        this.z=z;
    }
    public void rotate(double degrees) {
        rotation = (rotation + degrees) % 360.0;
    }
    public Point[] getCorners() {
        double halfWidth = width / 2;
        double halfHeight = thickness / 2;
        Point[] corners = {
            new Point(-halfWidth, -halfHeight),
            new Point(halfWidth, -halfHeight),
            new Point(halfWidth, halfHeight),
            new Point(-halfWidth, halfHeight)
        };
        double radians = toRadians(rotation);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        for (int i = 0; i < corners.length; i++) {
            double x = corners[i].x * cos - corners[i].y * sin;
            double y = corners[i].x * sin + corners[i].y * cos;
            corners[i] = new Point(x + this.x, y + this.y);
        }

        return corners;
    }
    public float[][] getCorners2() {
        double halfWidth = width / 2;
        double halfHeight = thickness / 2;
        Point[] corners = {
            new Point(-halfWidth, -halfHeight),
            new Point(halfWidth, -halfHeight),
            new Point(halfWidth, halfHeight),
            new Point(-halfWidth, halfHeight)
        };
        double radians = toRadians(rotation);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        for (int i = 0; i < corners.length; i++) {
            double x = corners[i].x * cos - corners[i].y * sin;
            double y = corners[i].x * sin + corners[i].y * cos;
            corners[i] = new Point(x + this.x, y + this.y);
        }
        float[][] float_corners;
        float_corners = new float[4][2];
        for(int i=0; i<4; i++){
            float_corners[i][0]= (float) corners[i].x;
            float_corners[i][1]= (float) corners[i].y;
        }
        return float_corners;
    }
    public Point[] getAxes() {
        Point[] axes = new Point[4];
        Point[] corners = getCorners();
        Point[] edges = new Point[4];
        for (int i = 0; i < 4; i++) {
            edges[i] = new Point(
                corners[(i + 1) % 4].x - corners[i].x,
                corners[(i + 1) % 4].y - corners[i].y
            );
        }
        for (int i = 0; i < 4; i++) {
            axes[i] = new Point(-edges[i].y, edges[i].x);
            axes[i] = normalize(axes[i]);
        }

        return axes;
    }
    public void setCoordinatesFromHITBOX(HITBOX other){
        this.x=other.x;
        this.y=other.y;
        this.z=other.z;
    }
    public void setCoordinatesFromVECTOR(Vector3 other){
        this.x=other.x;
        this.y=other.z;
        this.z=other.y;
    }
    public void addCoordinatesFromVECTOR(Vector3 other){
        this.x+=other.x;
        this.y+=other.z;
        this.z+=other.y;
    }
    public Vector3 getVectorPosition(){
        return new Vector3((float)x,(float)z,(float)y);
    }
    public boolean colliderectangles(HITBOX other) {
        if(other.getBottom()>getTop() || getBottom()>other.getTop() || getTop()<other.getBottom() || other.getTop()<getBottom()){
            return false;
        }
        Point[] axes = getAxes();
        Point[] otherAxes = other.getAxes();
        Point[] allAxes = new Point[axes.length + otherAxes.length];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        System.arraycopy(otherAxes, 0, allAxes, axes.length, otherAxes.length);
        for (Point axis : allAxes) {
            Point[] thisCorners = getCorners();
            double thisMin, thisMax;
            {
                double[] projections = new double[thisCorners.length];
                for (int i = 0; i < thisCorners.length; i++) {
                    projections[i] = thisCorners[i].x * axis.x + thisCorners[i].y * axis.y;
                }
                thisMin = Math.min(Math.min(projections[0], projections[1]), Math.min(projections[2], projections[3]));
                thisMax = Math.max(Math.max(projections[0], projections[1]), Math.max(projections[2], projections[3]));
            }
            Point[] otherCorners = other.getCorners();
            double otherMin, otherMax;
            {
                double[] projections = new double[otherCorners.length];
                for (int i = 0; i < otherCorners.length; i++) {
                    projections[i] = otherCorners[i].x * axis.x + otherCorners[i].y * axis.y;
                }
                otherMin = Math.min(Math.min(projections[0], projections[1]), Math.min(projections[2], projections[3]));
                otherMax = Math.max(Math.max(projections[0], projections[1]), Math.max(projections[2], projections[3]));
            }
            if (!overlap(thisMin, thisMax, otherMin, otherMax,1e-6)) {
                return false;
            }
        }

        return true;
    }
    public boolean clipUnrotatedRects(HITBOX hb){
        return clipRects(width, thickness,this.x,this.y, hb.width, hb.thickness, hb.x,hb.y);
    }
    public boolean clipRects(double Width1, double Height1, double Xcenter1, double Ycenter1,
                             double Width2, double Height2, double Xcenter2, double Ycenter2) {
        int left1 = (int) (Xcenter1 - Width1 / 2);
        int right1 = (int) (Xcenter1 + Width1 / 2);
        int top1 = (int) (Ycenter1 + Height1 / 2);
        int bottom1 = (int) (Ycenter1 - Height1 / 2);
        int left2 = (int) (Xcenter2 - Width2 / 2);
        int right2 = (int) (Xcenter2 + Width2 / 2);
        int top2 = (int) (Ycenter2 + Height2 / 2);
        int bottom2 = (int) (Ycenter2 - Height2 / 2);
        if (right1 < left2 || left1 > right2 || bottom1 > top2 || top1 < bottom2) {
            return false;
        }

        return true;
    }
    protected boolean overlap(double aMin, double aMax, double bMin, double bMax, double epsilon) {
        return Math.max(aMin, bMin) - epsilon <= Math.min(aMax, bMax) + epsilon;
    }
    public boolean getTopBottomMargin(HITBOX other){
        float top=(float)getTop()+0.1f;
        float bottom= (float) other.getBottom();
        if(getTop()+0.005f<=bottom && bottom<=top){
            return colliderect2d(other);
        } else{
            return false;
        }
    }
    private double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }
    private Point normalize(Point vector) {
        double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        if (length == 0) {
            return new Point(0, 0);
        }
        return new Point(vector.x / length, vector.y / length);
    }
    protected static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    public int calculateAngle(HITBOX other) {
        double deltaX = other.x - this.x;
        double deltaY = other.y - this.y;
        double angleRadians = Math.atan2(deltaY, deltaX);
        int angleDegrees = (int) Math.toDegrees(angleRadians);

        return angleDegrees;
    }
    public boolean colliderect2d(HITBOX other){
        Point[] axes = getAxes();
        Point[] otherAxes = other.getAxes();
        Point[] allAxes = new Point[axes.length + otherAxes.length];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        System.arraycopy(otherAxes, 0, allAxes, axes.length, otherAxes.length);
        for (Point axis : allAxes) {
            Point[] thisCorners = getCorners();
            double thisMin, thisMax;
            {
                double[] projections = new double[thisCorners.length];
                for (int i = 0; i < thisCorners.length; i++) {
                    projections[i] = thisCorners[i].x * axis.x + thisCorners[i].y * axis.y;
                }
                thisMin = Math.min(Math.min(projections[0], projections[1]), Math.min(projections[2], projections[3]));
                thisMax = Math.max(Math.max(projections[0], projections[1]), Math.max(projections[2], projections[3]));
            }
            Point[] otherCorners = other.getCorners();
            double otherMin, otherMax;
            {
                double[] projections = new double[otherCorners.length];
                for (int i = 0; i < otherCorners.length; i++) {
                    projections[i] = otherCorners[i].x * axis.x + otherCorners[i].y * axis.y;
                }
                otherMin = Math.min(Math.min(projections[0], projections[1]), Math.min(projections[2], projections[3]));
                otherMax = Math.max(Math.max(projections[0], projections[1]), Math.max(projections[2], projections[3]));
            }
            if (!overlap(thisMin, thisMax, otherMin, otherMax,1e-6)) {
                return false;
            }
        }

        return true;
    }
    public Vector3 blockMovementAfter(Vector3 playerPosition, Vector3 movement, HITBOX other, Vector3 force) {
        Vector3 prevPos=other.getVectorPosition();
        other.addCoordinatesFromVECTOR(movement);
        other.setCoordinatesFromVECTOR(prevPos);
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        other.addCoordinatesFromVECTOR(movement);
        if (overlapX < overlapY) {
            movement.x = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.x < other.x) {
                playerPosition.x += 0.01f;
            } else {
                playerPosition.x -= 0.01f;
            }
        } else {
            movement.z = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.y < other.y) {
                playerPosition.z += 0.01f;
            } else {
                playerPosition.z -= 0.01f;
            }
        }

        return movement;
    }
    public Vector3 blockMovement(Vector3 playerPosition, Vector3 movement, HITBOX other, Vector3 force) {
        Vector3 prevPos=other.getVectorPosition();
        other.addCoordinatesFromVECTOR(movement);
        if (!colliderectangles(other)) {
            return movement;
        }
        other.setCoordinatesFromVECTOR(prevPos);
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        other.addCoordinatesFromVECTOR(movement);
        if(getBottomTopOverlap(other)){
            movement.y=0;
            if(force.y<0){
                force.y=0;
            }
        } else if (overlapX < overlapY) {
            movement.x = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.x < other.x) {
                playerPosition.x += 0.01f;
            } else {
                playerPosition.x -= 0.01f;
            }
        } else {
            movement.z = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.y < other.y) {
                playerPosition.z += 0.01f;
            } else {
                playerPosition.z -= 0.01f;
            }
        }

        return movement;
    }
    public boolean[] blockMovementPlus(Vector3 playerPosition, Vector2 movement, HITBOX other) {
        if (!colliderectangles(other)) {
            return new boolean[]{false,false};
        }
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        boolean ans=overlapX < overlapY;
        if (ans) {
            if (this.x < other.x) {
                playerPosition.x += overlapX*1.1f;
            } else {
                playerPosition.x -= overlapX*1.1f;
            }
        }

        return new boolean[]{true,ans};
    }
    public Vector2 blockMovementEnemy(Vector3 playerPosition, Vector2 movement, HITBOX other) {
        if (!colliderectangles(other)) {
            return movement;
        }
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        if (overlapX < overlapY) {
            if (this.x < other.x) {
                movement.x =movement.x*1.1f;
            } else {
                movement.x = -movement.x*1.1f;
            }
        } else {
            if (this.y < other.y) {
                movement.y = movement.y*1.1f;
            } else {
                movement.y = -movement.y*1.1f;
            }
        }

        return movement;
    }
    public boolean getBottomTopOverlap(HITBOX other){
        return (getTop()-0.1<=other.getBottom() && other.getBottom()<=getTop() || getBottom()+0.1>=other.getTop() && other.getTop()>=getBottom());
    }
    public boolean Bounce(HITBOX other, float[] normals) {
        if (!colliderectangles(other)) {
            return false;
        }
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        if(getBottomTopOverlap(other)){
            normals[1]*=-0.75f;
        }else if (overlapX < overlapY) {
            normals[0]*=-0.75f;
        } else {
            normals[2]*=-0.75f;
        }

        return true;
    }
    public int[] blockMovementSlide(Vector3 playerPosition, Vector3 movement, HITBOX other, Vector3 force) {
        Vector3 prevPos=other.getVectorPosition();
        other.addCoordinatesFromVECTOR(movement);
        if (!colliderectangles(other)) {
            return new int[]{0,0,0};
        }
        other.setCoordinatesFromVECTOR(prevPos);
        double overlapX = Math.min(this.x + this.width / 2, other.x + other.width / 2) - Math.max(this.x - this.width / 2, other.x - other.width / 2);
        double overlapY = Math.min(this.y + this.thickness / 2, other.y + other.thickness / 2) - Math.max(this.y - this.thickness / 2, other.y - other.thickness / 2);
        other.addCoordinatesFromVECTOR(movement);
        if(getBottomTopOverlap(other)){
            movement.y=0;
            if(force.y<0){
                force.y=0;
            }
            return new int[]{1,0,0};
        } else if (overlapX < overlapY) {
            movement.x = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.x < other.x) {
                return new int[]{0,0,1};
            } else {
                return new int[]{0,0,-1};
            }
        } else {
            movement.z = 0;
            other.setCoordinatesFromVECTOR(prevPos);
            if (this.y < other.y) {
                return new int[]{0,1,0};
            } else {
                return new int[]{0,-1,0};
            }
        }
    }
    public void setHeight(float height){
        this.height=height;
    }
    public float getHeight(){
        return (float) height;
    }
}

