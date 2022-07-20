package io.swapastack.dunetd.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.VisUI;
import io.swapastack.dunetd.entities.portals.Portal;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.hostileunits.HostileUnitEnum;
import io.swapastack.dunetd.math.PixelsConverter;
import lombok.Getter;
import lombok.NonNull;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static io.swapastack.dunetd.hostileunits.HostileUnitEnum.HARVESTER;
import static io.swapastack.dunetd.hostileunits.HostileUnitEnum.INFANTRY;

public final class AssetLoader implements Disposable {
    
    private static final String GAME_ASSETS_NOT_LOADED_MESSAGE = "Game assets weren't loaded";
    
    // Hostile unit information
    private static final String INFANTRY_MODEL_PATH = "cute_cyborg/scene.gltf";
    private static final Vector3 INFANTRY_MODEL_SCALE = new Vector3(0.02f, 0.04f, 0.03f);
    public static final String INFANTRY_WALK_ANIMATION = "RUN";
    
    private static final String HARVESTER_MODEL_PATH = "spaceship_orion/scene.gltf";
    private static final Vector3 HARVESTER_MODEL_SCALE = new Vector3(0.2f, 0.2f, 0.2f);
    public static final String HARVESTER_WALK_ANIMATION = "Action";
    
    private static final String BOSS_UNIT_MODEL_PATH = "faceted_character/scene.gltf";
    private static final Vector3 BOSS_UNIT_MODEL_SCALE = new Vector3(0.005f, 0.005f, 0.005f);
    public static final String BOSS_UNIT_WALK_ANIMATION = "Armature|Walk";
    
    // Shai hulud
    private static final String SHAI_HULUD_MODEL_PATH = "donut/donut.glb";
    
    // Towers (from kenney tower defense kit)
    private static final String TOWER_BASE_PATH = "towerSquare_bottomB.glb";
    private static final String TOWER_DEBRIS_PATH = "detail_rocks.glb";
    private static final String GUARD_TOWER_PATH = "weapon_blaster.glb";
    private static final String BOMB_TOWER_PATH = "weapon_cannon.glb";
    private static final String SOUND_TOWER_PATH = "towerRound_crystals.glb";
    private static final String PORTAL_PATH = "towerRound_base.glb";

    // Ground tiles (from kaykit Medieval Builder Pack)
    private static final String GROUND_TILE_PATH = "square_sand_detail.gltf.glb";
    private static final String PATH_TILE_STRAIGHT_PATH = "square_sand_roadB_detail.gltf.glb";
    private static final String PATH_TILE_CURVE_PATH = "square_sand_roadC_detail.gltf.glb";
    
    // Drawables
    public static final String DRAWABLE_BACKGROUND_NAME = "background";
    public static final String DRAWABLE_SELECTION_NAME = "selection";
    public static final String DRAWABLE_GUARD_TOWER_NAME = "guardTower";
    public static final String DRAWABLE_BOMB_TOWER_NAME = "bombTower";
    public static final String DRAWABLE_SOUND_TOWER_NAME = "soundTower";
    public static final String DRAWABLE_SHAI_HULUD_NAME = "shaiHulud";
    private Drawable background;
    private Drawable selection;
    private static final String GUARD_TOWER_TEXTURE_PATH = "guardTower.png";
    private static final String BOMB_TOWER_TEXTURE_PATH = "bombTower.png";
    private static final String SOUND_TOWER_TEXTURE_PATH = "soundTower.png";
    private static final String SHAI_HULUD_TEXTURE_PATH = "shaiHulud.png";
    private static final float TOWER_RESIZE_FACTOR = 0.14f;
    
    @Getter
    private Texture mainMenuBackgroundImage;
    
    // Paths
    private static final String KENNEY_BASE_PATH = "kenney_gltf/";
    private static final String KENNEY_ASSET_FILE = "kenney_assets.txt";
    private String[] kenneyModels;
    private static final String KAY_KIT_BASE_PATH = "kaykit_gltf/";
    private static final String KAY_KIT_ASSET_FILE = "kaykit_assets.txt";
    private String[] kayKitModels;
    
    // The AssetManager is used to load game assets like 3D gltf models or pngs
    @Getter
    private final AssetManager assetManager;
    
    // 3D models
    @Getter
    private final HashMap<String, SceneAsset> sceneAssetHashMap;
    
    public AssetLoader() {
        assetManager = new AssetManager();
        sceneAssetHashMap = new HashMap<>();
    }
    
    public void loadGameAssets() {
        mainMenuBackgroundImage = new Texture("mainmenubackground.jpg");
        
        // Configure asset manager to work with gdx gltf
        assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
        assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
        
        // Load all 3D models listed in kenney_assets.txt file in blocking mode
        var kenneyAssetsHandle = Gdx.files.internal(KENNEY_ASSET_FILE);
        var kenneyFileContent = kenneyAssetsHandle.readString();
        kenneyModels = kenneyFileContent.split("\\r?\\n");
        for (var kenneyModel : kenneyModels) {
            assetManager.load(KENNEY_BASE_PATH + kenneyModel, SceneAsset.class);
        }
        
        var kayKitAssetsHandle = Gdx.files.internal(KAY_KIT_ASSET_FILE);
        var kayKitFileContent = kayKitAssetsHandle.readString();
        kayKitModels = kayKitFileContent.split("\\r?\\n");
        for (var kayKitModel : kayKitModels) {
            assetManager.load(KAY_KIT_BASE_PATH + kayKitModel, SceneAsset.class);
        }
        
        // Load example enemy models
        assetManager.load(BOSS_UNIT_MODEL_PATH, SceneAsset.class);
        assetManager.load(INFANTRY_MODEL_PATH, SceneAsset.class);
        assetManager.load(HARVESTER_MODEL_PATH, SceneAsset.class);
        assetManager.load(SHAI_HULUD_MODEL_PATH, SceneAsset.class);
        
        // Load VisUI
        VisUI.load(VisUI.SkinScale.X2);
        background = VisUI.getSkin().getDrawable("window-bg");
        selection = VisUI.getSkin().getDrawable("list-selection");
    }
    
    public void createSceneAssets() {
        // Create scene assets for all loaded models
        for (var kenneyModel : kenneyModels) {
            var sceneAsset = assetManager.get(KENNEY_BASE_PATH + kenneyModel, SceneAsset.class);
            sceneAssetHashMap.put(kenneyModel, sceneAsset);
        }
        for (var kayKitModel : kayKitModels) {
            var sceneAsset = assetManager.get(KAY_KIT_BASE_PATH + kayKitModel, SceneAsset.class);
            sceneAssetHashMap.put(kayKitModel, sceneAsset);
        }
        
        // Create scene assets for hostile units
        sceneAssetHashMap.put(BOSS_UNIT_MODEL_PATH, assetManager.get(BOSS_UNIT_MODEL_PATH));
        sceneAssetHashMap.put(INFANTRY_MODEL_PATH, assetManager.get(INFANTRY_MODEL_PATH));
        sceneAssetHashMap.put(HARVESTER_MODEL_PATH, assetManager.get(HARVESTER_MODEL_PATH));
        sceneAssetHashMap.put(SHAI_HULUD_MODEL_PATH, assetManager.get(SHAI_HULUD_MODEL_PATH));
    }
    
    /** Releases all resources of this object. */
    @Override
    public void dispose() {
        assetManager.dispose();
        mainMenuBackgroundImage.dispose();
        VisUI.dispose();
    }
    
    public Drawable getDrawable(@NonNull String drawableName) {
        return switch (drawableName) {
            case DRAWABLE_BACKGROUND_NAME -> background;
            case DRAWABLE_SELECTION_NAME -> selection;
            case DRAWABLE_GUARD_TOWER_NAME -> getResizedDrawable(GUARD_TOWER_TEXTURE_PATH);
            case DRAWABLE_BOMB_TOWER_NAME -> getResizedDrawable(BOMB_TOWER_TEXTURE_PATH);
            case DRAWABLE_SOUND_TOWER_NAME -> getResizedDrawable(SOUND_TOWER_TEXTURE_PATH);
            case DRAWABLE_SHAI_HULUD_NAME -> getResizedDrawable(SHAI_HULUD_TEXTURE_PATH);
            default -> throw new IllegalArgumentException("There's no drawable with the name " + drawableName);
        };
    }
    
    private Drawable getResizedDrawable(@NonNull String texturePath) {
        var pixmapOriginal = new Pixmap(Gdx.files.internal(texturePath));
        int size = (int) Math.min(PixelsConverter.getX(TOWER_RESIZE_FACTOR), PixelsConverter.getY(TOWER_RESIZE_FACTOR));
        var pixmapResized = new Pixmap(size, size, pixmapOriginal.getFormat());
        pixmapResized.drawPixmap(pixmapOriginal,
                0, 0, pixmapOriginal.getWidth(), pixmapOriginal.getHeight(),
                0, 0, pixmapResized.getWidth(), pixmapResized.getHeight());
        var drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapResized)));
        pixmapOriginal.dispose();
        pixmapResized.dispose();
        return drawable;
    }
    
    public @NotNull GameModelTower getTowerGameModel(@NonNull TowerEnum towerEnum) {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        String towerPath = switch (towerEnum) {
            case GUARD_TOWER -> GUARD_TOWER_PATH;
            case BOMB_TOWER -> BOMB_TOWER_PATH;
            case SOUND_TOWER -> SOUND_TOWER_PATH;
        };
        
        return new GameModelTower(
                new GameModelPart(sceneAssetHashMap.get(TOWER_BASE_PATH).scene),
                new GameModelPart(sceneAssetHashMap.get(towerPath).scene)
        );
    }
    
    public @NotNull GameModelSingle getPortalGameModel(@NonNull Portal portal) {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        return new GameModelSingle(
                new GameModelPart(sceneAssetHashMap.get(PORTAL_PATH).scene, new Vector3(1f, 1f, 1f),
                        0f, portal.getGridPosition3d())
        );
    }
    
    public @NotNull GameModelSingle getShaiHuludGameModel() {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        return new GameModelSingle(
                new GameModelPart(sceneAssetHashMap.get(SHAI_HULUD_MODEL_PATH).scene,
                        new Vector3(10f, 10f, 10f), 0f, Vector3.Zero)
        );
    }
    
    public @NotNull GameModelPart getTowerDebrisGameModelPart(@NonNull Vector3 scale, float rotation,
                                                              @NonNull Vector3 position) {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        return new GameModelPart(sceneAssetHashMap.get(TOWER_DEBRIS_PATH).scene,
                scale, rotation, position);
    }
    
    public @NotNull GameModelSingle getHostileUnitGameModel(@NonNull HostileUnitEnum hostileUnitEnum) {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        var hostileUnitModelPath = switch (hostileUnitEnum) {
            case INFANTRY -> INFANTRY_MODEL_PATH;
            case HARVESTER -> HARVESTER_MODEL_PATH;
            case BOSS_UNIT -> BOSS_UNIT_MODEL_PATH;
        };
        
        var hostileUnitModelScale = switch (hostileUnitEnum) {
            case INFANTRY -> INFANTRY_MODEL_SCALE;
            case HARVESTER -> HARVESTER_MODEL_SCALE;
            case BOSS_UNIT -> BOSS_UNIT_MODEL_SCALE;
        };
        
        float offsetY = 0f;
        if (hostileUnitEnum == HARVESTER)
            offsetY = 0.3f;
        
        float offsetRotation = 0f;
        if (hostileUnitEnum == INFANTRY)
            offsetRotation = 180f;
        
        return new GameModelSingle(
                new GameModelPart(sceneAssetHashMap.get(hostileUnitModelPath).scene,
                        hostileUnitModelScale, 0f, Vector3.Zero,
                        offsetRotation, new Vector3(0f, offsetY, 0f))
        );
    }
    
    public @NotNull GameModelSingle getGroundTile(@NonNull GroundTileEnum groundTileEnum) {
        if (sceneAssetHashMap.size() == 0)
            throw new IllegalStateException(GAME_ASSETS_NOT_LOADED_MESSAGE);
        
        String scenePath = switch (groundTileEnum) {
            case GROUND_TILE -> GROUND_TILE_PATH;
            case PATH_STRAIGHT -> PATH_TILE_STRAIGHT_PATH;
            case PATH_CURVE -> PATH_TILE_CURVE_PATH;
        };
        
        return new GameModelSingle(
                new GameModelPart(sceneAssetHashMap.get(scenePath).scene,
                        new Vector3(0.5f, 0.5f, 0.5f), 0f, Vector3.Zero)
        );
    }
    
}
