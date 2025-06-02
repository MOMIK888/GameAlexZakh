package com.bestproject.main;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Color;
import com.bestproject.main.CostumeClasses.CircularWarning;
import com.bestproject.main.CostumeClasses.GradientDecalWarning;
import com.bestproject.main.CostumeClasses.SpriteSheetDecal;
import com.bestproject.main.CostumeClasses.TextRendererController;
import com.bestproject.main.CreativeMode.CreativeMode;
import com.bestproject.main.CreativeMode.ShowHitbox;
import com.bestproject.main.Databases.DatabaseController;
import com.bestproject.main.EffectDecals.EffectBuffer;
import com.bestproject.main.FightUtils.DamageRenderer;
import com.bestproject.main.Game.GameCore;
import com.bestproject.main.Game.GameEngine;
import com.bestproject.main.LoadScreen.FirstLoadingScreen;
import com.bestproject.main.LoadScreen.LoadingScreen;
import com.bestproject.main.ObjectFragment.HITBOX;
import com.bestproject.main.Quests.QuestManager;
import com.bestproject.main.RenderOverride.RenderOverride;
import com.bestproject.main.SoundManagement.CostumeSound;
import com.bestproject.main.SoundManagement.SoundManager;

import java.util.ArrayList;

public class StaticBuffer implements Disposable {
    public static Model Testmodel=createSimpleCube();
    public static DatabaseController databaseController=new DatabaseController();
    public static CreativeMode creativeMode=new CreativeMode();
    public static QuestManager questManager=new QuestManager();
    public static SoundManager soundManager=new SoundManager();
    public static RenderOverride renderOverride=null;
    public static EffectBuffer effectBuffer=new EffectBuffer();
    public static SpriteSheetDecal warning=new SpriteSheetDecal(new Texture("Images/Effect2d/hit.png"),5,2,0.03f,0.01f); //disposed
    public static SpriteSheetDecal warningRed=new SpriteSheetDecal(new Texture("Images/Effect2d/redWarn.png"),4,4,0.03f,0.01f); //disposed
    public static SpriteSheetDecal dust=new SpriteSheetDecal(new Texture("Images/Effect2d/dust2.png"),8,5,0.005f,0.01f); //disposed
    public static BitmapFont[] fonts=new BitmapFont[]{new BitmapFont(Gdx.files.internal("Fonts/hinyu.fnt"), Gdx.files.internal("Fonts/hinyu.png"),false)}; //disposed
    //disposed
    static boolean isPaused, isLoading=false;
    public static GradientDecalWarning warn=new GradientDecalWarning(new Color(1f,0f,0f,0.6f),Color.RED); //disposed
    public static CircularWarning circularWarn=new CircularWarning(new Color(1f,0f,0f,0.4f),Color.RED); //disposed
    static Vector3 player_coordinates=new Vector3();
    static public float screenWidth = Gdx.graphics.getWidth();
    static public float screenHeight = Gdx.graphics.getHeight();
    static AssetManager newassets=new AssetManager(); //disposed
    static float scaleX = screenWidth / 2400f;
    static float scaleY = screenHeight / 1080f;
    public static DecalBatch decalBatch=new DecalBatch(40,new CameraGroupStrategy(GameCore.camera));
    public static ArrayList<Model> EffectBuffer=new ArrayList<>(); //disposed
    public static ArrayList<Model> current_enemies=new ArrayList<>(); //disposed
    public static ArrayList<Model> currentModels=new ArrayList<>(); //disposed
    private static int[] Player_coordinates=new int[]{0,0};
    private static Vector3 PlayerCooordinates=new Vector3();
    public static ShapeRenderer TestShapeRenderer=new ShapeRenderer(); //disposed
    public static int unique_index=0;//disposed
    public static  SpriteBatch spriteBatch=new SpriteBatch(); //disposed
    public static AssetManager assetManager=new AssetManager(); //disposed
    public static AssetManager constantAssets=new AssetManager();
    static public Color[] rarity_colors = new Color[]{new Color(Color.rgba8888(253/255f, 254/255f, 254/255f,1f)),
        new Color(Color.rgba8888(39/255f, 174/255f, 96/255f,1f)),
        new Color(Color.rgba8888(36/255f,113/255f,163/255f,1f)),
        new Color(Color.rgba8888(125/255f, 60/255f, 152/255f,1f)),
        new Color(Color.rgba8888(241/255f, 196/255f, 15/255f,1f)),
        new Color(Color.rgba8888(211/255f, 84/255f, 0,1f))};
    static public Color[] choice_colors = new Color[]{new Color(Color.rgba8888(63/255f,76/255f,92/255f,1f)),
        new Color(Color.rgba8888(236/255f,229/255f,216/255f,1f))};
    public static UI ui=new UI();//disposed
    public static TextRendererController textRenderer=new TextRendererController();
    //Утилиты для дебага и креатива!
    public static ShowHitbox showHitbox=new ShowHitbox();
    public static DamageRenderer damageRenderer=new DamageRenderer(fonts[0]);
    public static boolean isCreative=false;


    public static int[] getPlayer_coordinates() {
        return Player_coordinates;
    }

    public static void setPlayer_coordinates(int[] player_coordinates) {
        Player_coordinates = player_coordinates;
    }

    public static Vector3 getPlayerCooordinates() {
        return PlayerCooordinates;
    }

    public static void setPlayerCooordinates(Vector3 playerCooordinates) {
        PlayerCooordinates.set(playerCooordinates);
    }
    public static int getUnique_index(){
        unique_index+=1;
        return unique_index;
    }
    public static boolean getIsPaused(){
        return isPaused;
    }
    public static void setIsPaused(boolean value){
        isPaused=value;
    }
    public static void LoadEffects(){
        newassets.load("Models/Effects/surge.g3dj", Model.class);
        newassets.load("Models/Minor_models/coin.g3dj", Model.class);
        newassets.finishLoading();
        EffectBuffer.add(newassets.get("Models/Effects/surge.g3dj", Model.class));
        EffectBuffer.add(newassets.get("Models/Minor_models/coin.g3dj", Model.class));
    }
    public static ModelInstance getEffectfromBuffer(int index){
        return new ModelInstance(EffectBuffer.get(index));
    }
    public static void saveAllValues(){

    }
    public static void disposeAll() {
        soundManager.dispose();
        spriteBatch.dispose();
        TestShapeRenderer.dispose();
        warningRed.dispose();
        circularWarn.dispose();
        for(int i=0; i<EffectBuffer.size(); i++) {
            EffectBuffer.get(i).dispose();
        }
        assetManager.dispose();
        for (int i = 0; i < current_enemies.size(); i++) {
            current_enemies.get(i).dispose();
        }
        for (int i = 0; i < currentModels.size(); i++) {
            currentModels.get(i).dispose();
        }
        for(int i=0; i<fonts.length; i++){
            fonts[i].dispose();
        }
        ui.dispose();
        newassets.dispose();
        showHitbox.dispose();
        decalBatch.dispose();
        textRenderer.dispose();
        warning.dispose();
        warn.dispose();
        dust.dispose();
    }
    public static float getDistance(float x1, float y1, float x2, float y2){
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float[] getSin_Cos(Vector3 point, Vector3 point2) {
        float deltaX = point2.x - point.x;
        float deltaY = point2.z - point.z;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance == 0) {
            return new float[]{0, 1};
        }
        float sin = deltaY / distance;
        float cos = deltaX / distance;
        return new float[]{sin, cos};
    }

    public static void setIsLoading(boolean b) {
        isLoading=b;
    }

    public static void EnableCREATIVE() {
        isCreative=true;
        GameEngine.getGameCore().fps=new FirstPersonCameraController(GameCore.camera);
    }


    @Override
    public void dispose() {
        disposeAll();
    }
    public static float getScaleX(){
        return scaleX;
    }
    public static float getScaleY(){
        return scaleY;
    }
    public static void initialize_Abyss_models(){
        current_enemies.clear();
        currentModels.clear();
        String[] assets=new String[]{"Models/BluSlime/blueslime.g3dj","Models/StonneWall/stonewall.g3dj","Models/barrel.g3dj",
            "Models/StonneWall/crate.g3dj","Models/Minor_models/fire.g3dj", "Models/Minor_models/torch.g3dj","Models/StonneWall/door.g3dj"
        };
        for(String i: assets){
            assetManager.load(i, Model.class);
        }
        assetManager.finishLoading();
        String[][] manager_of_assets=new String[][]{{"Models/StonneWall/stonewall.g3dj","Models/barrel.g3dj","Models/StonneWall/crate.g3dj",
            "Models/Minor_models/fire.g3dj","Models/Minor_models/torch.g3dj","Models/StonneWall/door.g3dj"},{"Models/BluSlime/blueslime.g3dj"}};
        for(String i: manager_of_assets[0]){
            currentModels.add(assetManager.get(i));
        }
        for(String i: manager_of_assets[1]){
            current_enemies.add(assetManager.get(i));
        }
    }
    public static void initialize_BigCity_models(){
        current_enemies.clear();
        soundManager.dispose();
        currentModels.clear();
        String[] assets=new String[]{"Models/Chunks/Chunk1/chunk1.g3dj"
        };
        String[][] manager_of_assets=new String[][]{{"Models/Chunks/Chunk1/chunk1.g3dj"},{}};
        GameEngine.getGameCore().setLoadingScreen(new FirstLoadingScreen(assets,assetManager,manager_of_assets));

    }
    public static void initialize_City_models(){
        current_enemies.clear();
        assetManager.load("Sounds/Music/song1.mp3", Music.class);
        assetManager.finishLoading();
        soundManager.dispose();
        soundManager.addConstantSound("1x1x1x1x",new CostumeSound(assetManager.get("Sounds/Music/song1.mp3"), true, 20f));
        soundManager.playSoundConstant("1x1x1x1x");
        currentModels.clear();
        String[] assets=new String[]{"Models/Buildings/building.g3dj","Models/Buildings/concreteTile.g3dj","Models/Char3/character3.g3dj","Models/Buildings/bldg2.g3dj"
        };
        String[][] manager_of_assets=new String[][]{{"Models/Buildings/building.g3dj","Models/Buildings/concreteTile.g3dj","Models/Buildings/bldg2.g3dj"},{"Models/Char3/character3.g3dj"}};
        GameEngine.getGameCore().setLoadingScreen(new FirstLoadingScreen(assets,assetManager,manager_of_assets));

    }
    public static void initialize_BossArena_models(){
        current_enemies.clear();
        soundManager.dispose();
        currentModels.clear();
        String[] assets=new String[]{"Models/Chunks/Chunk1/TowBigChunk1.g3dj","Models/Enemies/dreadnaught.g3dj"
        };
        String[][] manager_of_assets=new String[][]{{"Models/Chunks/Chunk1/TowBigChunk1.g3dj"},{"Models/Enemies/dreadnaught.g3dj"}};
        GameEngine.getGameCore().setLoadingScreen(new FirstLoadingScreen(assets,assetManager,manager_of_assets));
        soundManager.dispose();
        assetManager.load("Sounds/Music/IntoTheFire.mp3", Music.class);
        assetManager.finishLoading();
        Music music=assetManager.get("Sounds/Music/IntoTheFire.mp3");
        soundManager.addConstantSound("theme",new CostumeSound(music, true, 20f));
        soundManager.playSoundConstant("theme");

    }
    public static void initialize_Village_models(){
        current_enemies.clear();
        currentModels.clear();
        String[] assets=new String[]{"Models/SingleMeshMaps/village.g3dj"
        };
        String[][] manager_of_assets=new String[][]{{"Models/SingleMeshMaps/village.g3dj"},{}};
        GameEngine.getGameCore().setLoadingScreen(new FirstLoadingScreen(assets,assetManager,manager_of_assets));
        assetManager.load("Sounds/Music/song2.mp3", Music.class);
        assetManager.finishLoading();
        soundManager.dispose();
        Music music=assetManager.get("Sounds/Music/song2.mp3");
        soundManager.addConstantSound("theme",new CostumeSound(music, true, 20f));
        soundManager.playSoundConstant("theme");
    }
    public static void initialize_Tavern_models(){
        current_enemies.clear();
        assetManager.load("Sounds/Music/song2.mp3", Music.class);
        assetManager.finishLoading();
        soundManager.dispose();
        soundManager.addConstantSound("theme",new CostumeSound(assetManager.get("Sounds/Music/song2.mp3"), false, 20f));
        soundManager.playSoundConstant("theme");
        currentModels.clear();
        String[] assets=new String[]{"Models/SingleMeshMaps/Tavern/tavern.g3dj"
        };
        String[][] manager_of_assets=new String[][]{{"Models/SingleMeshMaps/Tavern/tavern.g3dj"},{}};
        GameEngine.getGameCore().setLoadingScreen(new FirstLoadingScreen(assets,assetManager,manager_of_assets));
    }
    public static Rectangle loadTexture_plus_resize(float scale_on_x, float scale_on_y, boolean adaptx, int width, int height, int x, int y){
        return new Rectangle();
    }
    public static Rectangle getBounds(float scale, boolean adaptx, float width, float height, float x, float y){
        return new Rectangle(x * scaleX, y * scaleX, width * scaleX*scale, height * scaleX*scale);
    }
    public static Model create_plane_effect(Texture texture, int rows, int columns) {
        ModelBuilder modelBuilder = new ModelBuilder();
        float frameWidth = 1f / columns;
        float frameHeight = 1f / rows;
        modelBuilder.begin();
        Material material = new Material(TextureAttribute.createDiffuse(texture));
        MeshPartBuilder meshBuilder = modelBuilder.part("plane", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, material);
        float planeWidth = 1f;
        float planeHeight = 1f;
        MeshPartBuilder.VertexInfo v1 = new MeshPartBuilder.VertexInfo().setPos(-planeWidth / 2, -planeHeight / 2, 0).setNor(0, 0, 1).setUV(0, 0);
        MeshPartBuilder.VertexInfo v2 = new MeshPartBuilder.VertexInfo().setPos(planeWidth / 2, -planeHeight / 2, 0).setNor(0, 0, 1).setUV(frameWidth, 0);
        MeshPartBuilder.VertexInfo v3 = new MeshPartBuilder.VertexInfo().setPos(planeWidth / 2, planeHeight / 2, 0).setNor(0, 0, 1).setUV(frameWidth, frameHeight);
        MeshPartBuilder.VertexInfo v4 = new MeshPartBuilder.VertexInfo().setPos(-planeWidth / 2, planeHeight / 2, 0).setNor(0, 0, 1).setUV(0, frameHeight);
        meshBuilder.rect(v1, v2, v3, v4);
        Model planeModel = modelBuilder.end();
        planeModel.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        return planeModel;
    }
    public static boolean isIsLoading(){
        return isLoading;
    }
    public static void setPlayer_coordinates(Vector3 vector3){
        player_coordinates.set(vector3);
    }
    public static Vector3 getPlayerCoordinates() {
        return player_coordinates;
    }
    public static Array<HITBOX> decipherHitboxInfo(String packedInfo) {
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
            HITBOX hitbox = new HITBOX(0, 0, 0, 0, 0, 0);
            hitbox.x = Float.parseFloat(pos[0]);
            hitbox.y = Float.parseFloat(pos[1]);
            hitbox.z = Float.parseFloat(pos[2]);
            hitbox.width = Float.parseFloat(size[0]);
            hitbox.thickness = Float.parseFloat(size[1]);
            hitbox.height = Float.parseFloat(size[2]);
            hitbox.type = Integer.valueOf(type);
            hitboxArray.add(hitbox);
        }
        return hitboxArray;
    }
    public static Model createSimpleCube() {
        ModelBuilder modelBuilder = new ModelBuilder();
        return modelBuilder.createBox(
            1f, 1f, 1f,
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
    }
}
