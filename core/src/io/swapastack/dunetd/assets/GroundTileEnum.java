package io.swapastack.dunetd.assets;

/**
 * Selection of tiles used in the AssetLoader class to get the right 3d model object.<br>
 * - GROUND_TILE: The default tile, towers are built upon it <br>
 * - PATH_CURVE: A curve path tile to represent a part of the path <br>
 * - PATH_STRAIGHT: A straight path tile to represent a part of the path
 */
public enum GroundTileEnum {
    GROUND_TILE,
    PATH_CURVE,
    PATH_STRAIGHT
}
