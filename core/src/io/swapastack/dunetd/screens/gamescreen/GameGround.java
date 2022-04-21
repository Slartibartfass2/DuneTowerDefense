package io.swapastack.dunetd.screens.gamescreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.assets.GameModelSingle;
import io.swapastack.dunetd.assets.GroundTileEnum;
import io.swapastack.dunetd.game.CardinalDirection;
import io.swapastack.dunetd.pathfinding.Path;
import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.SceneManager;

/**
 * Class which manages the ground tiles of the game, including path tiles to represent the path for HostileUnits.
 */
public final class GameGround {
    
    private final GameModelSingle[][] groundGrid;
    private final SceneManager sceneManager;
    private Path path;
    private final AssetLoader assetLoader;
    
    public GameGround(int gridWidth, int gridHeight, @NonNull SceneManager sceneManager, @NonNull AssetLoader assetLoader) {
        if (gridWidth < 1 || gridHeight < 1)
            throw new IllegalArgumentException("gridWidth and gridHeight must be greater than one");
        
        this.groundGrid = new GameModelSingle[gridWidth][gridHeight];
        this.sceneManager = sceneManager;
        this.assetLoader = assetLoader;
    
        // Fill grid with ground tiles
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                var gridTile = assetLoader.getGroundTile(GroundTileEnum.GROUND_TILE);
                addTileToGround(x, y, gridTile, 0f);
            }
        }
    }
    
    /**
     * Updates ground tiles if path changed.
     *
     * @param path Current path object of the game.
     */
    public void updatePath(@NonNull Path path) {
        if (this.path != null && this.path.equals(path))
            return;
    
        // Delete old path
        if (this.path != null) {
            for (Vector2 wayPoint : this.path.getWaypoints()) {
                if (wayPoint == null)
                    continue;
                int x = (int) wayPoint.x;
                int y = (int) wayPoint.y;
                if (groundGrid[x][y] != null)
                    sceneManager.removeScene(groundGrid[x][y].getScene());
                groundGrid[x][y] = null;
            }
        }
        
        this.path = path.copy();
        
        // Create new path tiles
        int wayPointLength = this.path.getWaypoints().length;
        for (int i = 0; i < wayPointLength; i++) {
            Vector2 wayPoint = this.path.getWaypoint(i);
    
            int x = (int) wayPoint.x;
            int y = (int) wayPoint.y;
    
            if (groundGrid[x][y] != null)
                sceneManager.removeScene(groundGrid[x][y].getScene());
    
            GameModelSingle newTile;
            float rotation = 0f;
    
            // If tiles are portals set portale tiles
            if (i == 0 || i == wayPointLength - 1) {
                newTile = assetLoader.getGroundTile(GroundTileEnum.GROUND_TILE);
            } else {
                    // Path tiles
                    Vector2 wayPointBefore = this.path.getWaypoint(i - 1);
                    Vector2 wayPointAfter = this.path.getWaypoint(i + 1);
                    
                    // Get direction vectors
                    wayPointAfter.sub(wayPoint);
                    wayPoint.sub(wayPointBefore);
                    
                    // If vectors are equal the path tile is straight
                    if (wayPoint.equals(wayPointAfter)) {
                        CardinalDirection tileOrientation = CardinalDirection.fromDirection(wayPoint);
                        newTile = assetLoader.getGroundTile(GroundTileEnum.PATH_STRAIGHT);
                        rotation = tileOrientation.getDegrees();
                    } else {
                        // Curve tile -> calculate turn angle
                        float rotationBefore = CardinalDirection.fromDirection(wayPoint).getDegrees();
                        float rotationAfter = CardinalDirection.fromDirection(wayPointAfter).getDegrees();
                        float rotationChange = (rotationBefore - rotationAfter + 360) % 360;
                        
                        // if rotationChange == 90 then it's a turn right otherwise it's a turn left
                        if (rotationChange == 90)
                            rotation = rotationBefore;
                        else if (rotationChange == 270)
                            rotation = (rotationBefore + 270) % 360;
                        
                        newTile = assetLoader.getGroundTile(GroundTileEnum.PATH_CURVE);
                    }
                }
            
            addTileToGround(x, y, newTile, rotation);
        }
    
        // Fill wholes in grid with ground tiles
        for (int x = 0; x < groundGrid.length; x++) {
            for (int y = 0; y < groundGrid[x].length; y++) {
                if (groundGrid[x][y] != null)
                    continue;
                var gridTile = assetLoader.getGroundTile(GroundTileEnum.GROUND_TILE);
                addTileToGround(x, y, gridTile, 0f);
            }
        }
    }
    
    /**
     * Adds a tile as scene object to the SceneManager and groundGrid at specified position with specified rotation.
     *
     * @param x X coordinate of tile
     * @param y Y coordinate of tile
     * @param gridTile Scene object to add to the SceneManager
     * @param rotation Rotation of the scene object
     */
    private void addTileToGround(int x, int y, @NonNull GameModelSingle gridTile, float rotation) {
        groundGrid[x][y] = gridTile;
        BoundingBox boundingBox = new BoundingBox();
        gridTile.getScene().modelInstance.calculateBoundingBox(boundingBox);
        Vector3 modelDimensions = new Vector3();
        boundingBox.getDimensions(modelDimensions);
        modelDimensions.scl(0.5f);
        gridTile.rePositionAndRotate(
                new Vector3(x * modelDimensions.x, -modelDimensions.y, y * modelDimensions.z),
                (rotation + 180) % 360
        );
        sceneManager.addScene(gridTile.getScene());
    }
}
