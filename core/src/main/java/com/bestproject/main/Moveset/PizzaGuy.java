package com.bestproject.main.Moveset;

import static com.bestproject.main.Game.GameCore.camera;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bestproject.main.CharacterUtils.Elytra;
import com.bestproject.main.CharacterUtils.RocketLauncher;
import com.bestproject.main.CharacterUtils.Slingshot;
import com.bestproject.main.CostumeClasses.ImageButton;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.MovingObjects.Player;
import com.bestproject.main.StaticBuffer;
import com.bestproject.main.StaticQuickMAth;
import com.bestproject.main.Weapons.Rocket;

public class PizzaGuy  extends Moveset{
    Color[] colors; //undisposable
    public boolean jump=false;
    public boolean dash;
    float speedMul=1f;
    public boolean isUlting=false;
    public boolean flying=false;
    public boolean isOnground=false;
    public boolean airclick=false;
    Texture[] Image_variations;
    Elytra elytra;
    Slingshot slingshot;
    RocketLauncher rocketLauncher;

    public PizzaGuy(){
        super();
        charinfo=1;
        rocketLauncher=new RocketLauncher();
        colors=new Color[]{new Color(Color.rgba8888(0.4f, 0.2f, 0.6f,0.6f)),new Color(Color.rgba8888(0f, 0.8f, 1f,0.6f))};
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1725,75,175,175));
        buttons.add(new ImageButton("Images/ButtonIcons/swap.png",1900,175,225,225));
        buttons.add(new ImageButton("Images/ButtonIcons/ult1.png",1600,50,120,120));
        buttons.add(new ImageButton("Images/ButtonIcons/dash.png",2150,280,170,170));
        buttons.add(new ImageButton("Images/ButtonIcons/dash.png",2150,100,100,100));
        StaticBuffer.constantAssets.load("Sounds/Music/LMC.mp3", Music.class);
        StaticBuffer.constantAssets.load("Models/Attacks/blast.g3dj",Model.class);
        StaticBuffer.constantAssets.load("Models/Char4/PizzaGuy.g3dj", Model.class);
        StaticBuffer.constantAssets.load("Models/Minor_models/flintlock.g3dj", Model.class);
        StaticBuffer.constantAssets.finishLoading();
        characterModel=StaticBuffer.constantAssets.get("Models/Char4/PizzaGuy.g3dj", Model.class);
        LMC=StaticBuffer.constantAssets.get("Sounds/Music/LMC.mp3");
        attacks=null;
        hp=100f;
        elytra=new Elytra();
        slingshot=new Slingshot();
        maxHp=100f;
        stamina=100;
        modelInstance=new ModelInstance(characterModel);
        modelInstance.transform.rotate(0f,1f,0f,90);
        modelInstance.transform.scale(0.08f,0.08f,0.08f);
        controllers=new AnimationController[]{new AnimationController(modelInstance),new AnimationController(modelInstance),new AnimationController(modelInstance)};
        Image_variations=new Texture[]{new Texture("Images/ButtonIcons/coinToss.png"), new Texture("Images/ButtonIcons/flintlock.png") };

    }

    @Override
    public void dispose() {
        super.dispose();
        if(Image_variations!=null) {
            for (int i = 0; i < Image_variations.length; i++) {
                Image_variations[i].dispose();
            }
        }
    }

    @Override
    public void update() {
        super.update();
        ability_cooldown=ability_cooldown-(StaticQuickMAth.move(GameEngine.getGameCore().deltatime));
        charge[0]+=(StaticQuickMAth.move(GameEngine.getGameCore().deltatime)*80);
        cd-=StaticQuickMAth.move(GameEngine.getGameCore().deltatime);
        for(int i=0; i<cooldowns.length; i++){
            if(cooldowns[i]>=0){
                cooldowns[i]-=GameEngine.getGameCore().deltatime;
            }
        }
        if (cd<0){
            if (!simoltanious_buttons.isEmpty()) {
                goThrouh();
            }
        }
        if(buttons.get(1).getPointer()!=-1){
            simoltanious_buttons.add(1);
            isPunch=true;
        }
        if(current_state==5){
            if(!airclick) {
                jump = buttons.get(4).getPointer() != -1;
                if (!jump) {
                    current_state = 0;
                } else {
                    current_state = 5;
                }
            } else{
                flying = buttons.get(4).getPointer() != -1;
                if (!flying) {
                    current_state = 0;
                    airclick=false;
                } else {
                    current_state = 5;
                }
            }
        }
        if(current_state==4){
            dash=buttons.get(3).getPointer()!=-1;
            if(!dash){
                current_state=0;
            } else{
                current_state=4;
            }
        }
    }
    @Override
    public void draw(SpriteBatch spriteBatch){
        for(int i=0; i<buttons.size(); i++){
            buttons.get(i).draw(spriteBatch, 0.6f);
        }
    }

    @Override
    public void drawButtonPaddings(ShapeRenderer shapeRenderer) {
        buttons.get(0).drawButton(shapeRenderer,buttons.get(0).bounds.x,buttons.get(0).bounds.y,buttons.get(0).getRadius()*2,buttons.get(0).getRadius()*2,buttons.get(0).getRadius(),colors[1],colors[0], charge[0]);
        buttons.get(1).drawButton(shapeRenderer,buttons.get(1).bounds.x,buttons.get(1).bounds.y,buttons.get(1).getRadius()*2,buttons.get(1).getRadius()*2,buttons.get(1).getRadius(),colors[1],colors[0], 100f-cooldowns[0]*300f);
        buttons.get(2).drawButton(shapeRenderer,buttons.get(2).bounds.x,buttons.get(2).bounds.y,buttons.get(2).getRadius()*2,buttons.get(2).getRadius()*2,buttons.get(2).getRadius(),colors[1],colors[0], charge[1]);
        buttons.get(3).drawButton(shapeRenderer,buttons.get(3).bounds.x,buttons.get(3).bounds.y,buttons.get(3).getRadius()*2,buttons.get(3).getRadius()*2,buttons.get(3).getRadius(),colors[1],colors[0],100f-cooldowns[1]*300f);
        buttons.get(4).drawButton(shapeRenderer,buttons.get(4).bounds.x,buttons.get(4).bounds.y,buttons.get(4).getRadius()*2,buttons.get(4).getRadius()*2,buttons.get(4).getRadius(),colors[1],colors[0],100f-cooldowns[3]*300f);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(1000,80,maxHp*4,80);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(1000,80,hp*4,80);
    }

    @Override
    public boolean OnTouch(float touchX, float touchY, int pointer) {
        for(int i=0; i<buttons.size(); i++){
            if(buttons.get(i).onTouch(touchX,touchY,pointer)){
                button_suggestion(i);
                buttons.get(i).setPointer(pointer);
                return true;
            };
        }

        return false;
    }

    @Override
    public void button_suggestion(int act) {
        if(!simoltanious_buttons.contains(act)){
            if (act==0) {
                if (charge[0]>99) {
                    simoltanious_buttons.add(act);
                    cd=0.18f;
                    ability_cooldown=0.1f;
                    //Ракета
                    current_state=2;
                }
            } else if (act == 2) {
                if(charge[1]>99) {
                    simoltanious_buttons.add(act);
                    cd = 0.2f;
                    current_state=3;
                    isUlting=true;
                }
                if(isUlting){
                    simoltanious_buttons.add(act);
                }
            } else if(act==1){
                simoltanious_buttons.add(1);
                isPunch=true;
                cd=0.1f;

                current_state=1;
            } else if (act == 3) {
                current_state=4;
            } else if(act==4){
                current_state=5;
                if(isOnground) {
                    jump = true;
                    airclick=false;
                } else {
                    flying=true;
                    airclick=true;
                    controllers[0].setAnimation("metarig.002|fly",-1);
                }

            }
        }
    }

    @Override
    public void PlayerInterrractions(Player player) {
        modelInstance.calculateTransforms();
        float deltatime = GameEngine.getGameCore().getDeltatime();
        Vector2 vec=GameEngine.getGameCore().getJoystick().getDirection();
        player.unnormalizedMovement.set(StaticQuickMAth.move(vec.x*deltatime*player.speed*speedMul),0, StaticQuickMAth.move(-vec.y*deltatime*player.speed*speedMul));
        float angle = vec.angle();
        float multiplier=1f;
        player.hitboxes[0].setHeight(1.4f);
        int current_state=player.current_state;
        if (!player.unnormalizedMovement.isZero() && StaticBuffer.ui.getState()!=4) {
            player.lastdir=player.lastdir.set(player.movement.x,0,player.movement.z);
            if (current_state != 0 && StaticBuffer.ui.getState() == 0) {
                player.current_state = 0;
                controllers[0].setAnimation("metarig.002|walk", -1);
                controllers[1].setAnimation("metarig.002|WalkArms",-1);
            }

            Vector3 camDir = new Vector3(player.unnormalizedMovement).nor();
            Vector3 characterForward = new Vector3(0, 0, -1);
            Quaternion rotation = new Quaternion().setFromCross(characterForward, camDir);
            Vector3 flatCamDir = new Vector3(-camDir.x, 0, -camDir.z).nor();
            rotation.setFromCross(characterForward, flatCamDir);
            modelInstance.transform.idt();
            modelInstance.transform.rotate(rotation);
            modelInstance.transform.scale(0.08f, 0.08f, 0.08f);
            player.lastangle = angle;
        }else if(StaticBuffer.ui.getState()==0){
            player.speed=1f;
            if(current_state!=1 ){
                player.current_state=1;
            }
        }
        if(StaticBuffer.ui.getState()==2){
            if(current_state!=2){
                player.current_state=2;
                //ani,
            }
            multiplier=2f;
        }else if(StaticBuffer.ui.getState()==1){
            if(current_state!=3){
                player.current_state=3;
                //anim
            }
        }else if (StaticBuffer.ui.getState()==4) {
            player.speed=10f;
            player.hitboxes[0].setHeight(0.1f);
            modelInstance.transform.rotate(0, 1, 0, angle - player.lastangle);
            player.lastangle = angle;
            player.current_state=4;
        }
        if(this.current_state==1){
            if(!controllers[0].inAction){
                this.current_state=0;
            }
        }
        controllers[0].update(StaticQuickMAth.move(deltatime)*multiplier);
        controllers[1].update(StaticQuickMAth.move(deltatime)*multiplier);
        controllers[2].update(StaticQuickMAth.move(deltatime)*multiplier);
        player.fractureMovement(player.movement);
        player.movement.set(0,0,0);
        player.movement.add(player.unnormalizedMovement);
        if(player.speed>3f){
            player.speed-=StaticQuickMAth.move(deltatime/2);
        } else{
            player.speed+=StaticQuickMAth.move(deltatime);
        }
    }

    @Override
    public boolean OnRelease(float touchX, float touchY, int pointer) {
        for(int i=0; i<buttons.size(); i++){
            if(buttons.get(i).getPointer()==pointer){
                buttons.get(i).release();
                return true;
            }
        }
        return false;
    }

    @Override
    public void goThrouh(){
        if (simoltanious_buttons.contains(2)) {
            charge[1]=0;
            simoltanious_buttons.clear();
            for(int i=0; i<StaticBuffer.ui.movesets.size(); i++){
                if(StaticBuffer.ui.movesets.get(i).getHp()>0) {
                    StaticBuffer.ui.movesets.get(i).heal(maxHp / 100f * 20f);
                }
            }

        } else if (isPunch && cooldowns[0]<=0){
            if(!isUlting) {
                controllers[1].setAnimation("metarig.002|SlingShoot",1);
                Vector3 camDir = new Vector3(camera.direction).nor();
                Vector3 characterForward = new Vector3(0, 0, -1);
                Quaternion rotation = new Quaternion().setFromCross(characterForward, camDir);
                Vector3 flatCamDir = new Vector3(-camDir.x, 0, -camDir.z).nor();
                rotation.setFromCross(characterForward, flatCamDir);
                modelInstance.transform.idt();
                modelInstance.transform.rotate(rotation);
                modelInstance.transform.scale(0.08f, 0.08f, 0.08f);
                String boneName = "slingBase";
                Node bone = modelInstance.getNode(boneName, true);
                modelInstance.calculateTransforms(); // update bone transforms
                Matrix4 boneWorldMatrix = new Matrix4(modelInstance.transform).mul(bone.globalTransform);
                Vector3 boneWorldPos = new Vector3();
                boneWorldMatrix.getTranslation(boneWorldPos);
                slingshot.Shoot(camera.direction, boneWorldPos.add(StaticBuffer.getPlayerCooordinates()));
                cooldowns[0] = 0.1f;
                cd=0.11f;
                isPunch=false;
                simoltanious_buttons.clear();
            } else{
                isUlting=false;
            }
        }else if (simoltanious_buttons.contains(0)) {
            if(isUlting){
                simoltanious_buttons.clear();
                return;
            }
            controllers[1].setAnimation("metarig.002|rocketLaunch",1);
            rocketLauncher.Shoot(StaticBuffer.getPlayerCooordinates(), camera.direction);
            charge[0]=0;
            simoltanious_buttons.clear();
        } else if(simoltanious_buttons.contains(3) && cooldowns[1]<=0){
            cooldowns[1]=1.4f;
            cd=0.6f;
            simoltanious_buttons.clear();
        }
        simoltanious_buttons.clear();
        current_state=0;
    }
    @Override
    public void ActivateLms(){
        super.ActivateLms();
        damageMultiplier=1.5f;
        speedMul=2f;
    }
    @Override
    public void forceNullification(Player player){
        if(!flying) {
            float glvevel = GameEngine.getGameCore().getMap().GetGroundLevel(new Vector3((float) player.hitboxes[0].x, (float) player.hitboxes[0].z, (float) player.hitboxes[0].y));
            player.movement.add(StaticQuickMAth.getMultipVec(player.force,StaticQuickMAth.move(GameCore.deltatime)));
            if (player.hitboxes[0].getBottom()>glvevel+0.03f) {
                if (player.force.x > 0) {
                    player.force.x -= StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.x < 0) {
                        player.force.x = 0;
                    }
                } else {
                    player.force.x += StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.x > 0) {
                        player.force.x = 0;
                    }
                }
                if (player.force.z > 0) {
                    player.force.z -= StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.z < 0) {
                        player.force.z = 0;
                    }
                } else {
                    player.force.z += StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.z > 0) {
                        player.force.z = 0;
                    }
                }
                if (player.isGravityAffected) {
                    player.force.y -= StaticQuickMAth.getGravityAcceleration() * player.gravity_multip*8;
                    isOnground=false;
                } else if (player.force.y < 0) {
                    player.force.y = 0;
                    isOnground=true;
                }
                if (player.hitboxes[0].getBottom() < glvevel) {
                    player.position.set(new Vector3(player.position.x, (float) (glvevel + (player.hitboxes[0].height / 2.01f)), player.position.z));
                    player.gravity_multip = 1f;
                    player.isGravityAffected=false;
                }
            } else {
                if (player.force.x > 0) {
                    player.force.x -= StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.x < 0) {
                        player.force.x = 0;
                    }
                } else {
                    player.force.x += StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.x > 0) {
                        player.force.x = 0;
                    }
                }
                if (player.force.z > 0) {
                    player.force.z -= StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.z < 0) {
                        player.force.z = 0;
                    }
                } else {
                    player.force.z += StaticQuickMAth.move(3f) * GameCore.deltatime;
                    if (player.force.z > 0) {
                        player.force.z = 0;
                    }
                }
                if (player.force.y < 0) {
                    player.force.y = 0;
                    isOnground=true;

                }
                if (player.hitboxes[0].getBottom() < glvevel+0.05) {
                    if(player.force.y<0.1) {
                        player.position.set(new Vector3(player.position.x, (float) (glvevel + (player.hitboxes[0].height / 2.01f)), player.position.z));
                    }
                    player.gravity_multip = 1f;
                    isOnground=true;
                    player.isGravityAffected=false;
                }
            }
            if(!player.isGravityAffected) {
                if (jump) {
                    buttons.get(4).release();
                    current_state = 0;
                    jump = false;
                    player.force.y += 3f;
                    player.isGravityAffected=true;
                    isOnground=false;
                }
            }
            controllers[2].setAnimation("metarig.002|wingsFold");
        } else{

            elytra.update(player.force,-GameCore.cameraRoationm-90,GameCore.cameraRoationX,StaticQuickMAth.move(GameCore.deltatime));
            player.force=elytra.getVelocity();
            player.movement.add(StaticQuickMAth.getMultipVec(player.force,StaticQuickMAth.move(GameCore.deltatime)));
            isOnground=!player.isGravityAffected;
            if(isOnground){
                flying=false;
                buttons.get(4).release();
            }
            float glvevel = GameEngine.getGameCore().getMap().GetGroundLevel(new Vector3((float) player.hitboxes[0].x, (float) player.hitboxes[0].z, (float) player.hitboxes[0].y));
            if(player.hitboxes[0].getBottom()<glvevel+0.03){
                flying=false;
                isOnground=true;
                buttons.get(4).release();
            }

        }
    }
}
