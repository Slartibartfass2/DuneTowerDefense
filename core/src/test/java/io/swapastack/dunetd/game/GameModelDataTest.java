package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameModelDataTest {

    @Test
    void testGameModelData() {
        var gameModelData = new GameModelData(0f, Vector2.Zero);
        Assertions.assertNotNull(gameModelData);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new GameModelData(0f, null));
    }
}
