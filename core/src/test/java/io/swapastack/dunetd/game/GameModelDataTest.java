package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameModelDataTest {

    @Test
    void testGameModelData() {
        var gameModelData = new GameModelData(0f, Vector2.Zero);
        assertNotNull(gameModelData);

        assertThrows(IllegalArgumentException.class, () -> new GameModelData(0f, null));
    }
}
