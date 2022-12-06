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

    public GameGround(int gridWidth, int gridHeight, @NonNull SceneManager sceneManager,
                      @NonNull AssetLoader assetLoader)
            throws IllegalArgumentException {
        if (gridWidth < 1 || gridHeight < 1) {
            throw new IllegalArgumentException("gridWidth and gridHeight must be greater than one");
        }

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
        if (this.path != null && this.path.equals(path)) {
            return;
        }

        if (this.path != null) {
            deleteOldPath();
        }

        this.path = path.copy();

        // Create new path tiles
        int wayPointLength = this.path.getWaypoints().length;
        for (int i = 0; i < wayPointLength; i++) {
            Vector2 wayPoint = this.path.getWaypoint(i);

            int x = (int) wayPoint.x;
            int y = (int) wayPoint.y;

            if (groundGrid[x][y] != null) {
                sceneManager.removeScene(groundGrid[x][y].getScene());
            }

            GameModelSingle newTile;
            var rotation = 0f;

            // If tiles are portals set portale tiles
            if (i == 0 || i == wayPointLength - 1) {
                newTile = assetLoader.getGroundTile(GroundTileEnum.GROUND_TILE);
            } else {
                var tileAndRotation = determinePathTile(wayPoint, i);
                newTile = tileAndRotation.tile();
                rotation = tileAndRotation.rotation();
            }

            addTileToGround(x, y, newTile, rotation);
        }

        fillHolesInGrid();
    }

    private void deleteOldPath() {
        for (var wayPoint : path.getWaypoints()) {
            if (wayPoint == null) {
                continue;
            }
            int x = (int) wayPoint.x;
            int y = (int) wayPoint.y;
            if (groundGrid[x][y] != null) {
                sceneManager.removeScene(groundGrid[x][y].getScene());
            }
            groundGrid[x][y] = null;
        }
    }

    /**
     * Determines which path tile and rotation to chose according to the path waypoints
     */
    private TileAndRotation determinePathTile(Vector2 wayPoint, int i) {
        // Path tiles
        var wayPointBefore = path.getWaypoint(i - 1);
        var wayPointAfter = path.getWaypoint(i + 1);

        // Get direction vectors
        wayPointAfter.sub(wayPoint);
        wayPoint.sub(wayPointBefore);

        GameModelSingle newTile;
        var rotation = 0f;

        // If vectors are equal the path tile is straight
        if (wayPoint.equals(wayPointAfter)) {
            CardinalDirection tileOrientation = CardinalDirection.fromDirection(wayPoint);
            newTile = assetLoader.getGroundTile(GroundTileEnum.PATH_STRAIGHT);
            rotation = tileOrientation.getDegrees();
        } else {
            // Curve tile -> calculate turn angle
            var rotationBefore = CardinalDirection.fromDirection(wayPoint).getDegrees();
            var rotationAfter = CardinalDirection.fromDirection(wayPointAfter).getDegrees();
            var rotationChange = (rotationBefore - rotationAfter + 360) % 360;

            // if rotationChange == 90 then it's a turn right otherwise it's a turn left
            if (rotationChange == 90) {
                rotation = rotationBefore;
            } else if (rotationChange == 270) {
                rotation = (rotationBefore + 270) % 360;
            }

            newTile = assetLoader.getGroundTile(GroundTileEnum.PATH_CURVE);
        }

        return new TileAndRotation(newTile, rotation);
    }

    private record TileAndRotation(GameModelSingle tile, float rotation) {
    }

    /**
     * Fills hole in grid with ground tiles
     */
    private void fillHolesInGrid() {
        for (int x = 0; x < groundGrid.length; x++) {
            for (int y = 0; y < groundGrid[x].length; y++) {
                if (groundGrid[x][y] != null) {
                    continue;
                }
                var gridTile = assetLoader.getGroundTile(GroundTileEnum.GROUND_TILE);
                addTileToGround(x, y, gridTile, 0f);
            }
        }
    }

    /**
     * Adds a tile as scene object to the SceneManager and groundGrid at specified position with specified rotation.
     *
     * @param x        X coordinate of tile
     * @param y        Y coordinate of tile
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