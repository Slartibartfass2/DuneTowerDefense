package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class GameModelDataTest {
    
    @Test
    public void testGameModelData() {
        var gameModelData = new GameModelData(0f, Vector2.Zero);
        assertNotNull(gameModelData);
        
        assertThrows(IllegalArgumentException.class, () -> new GameModelData(0f, null));
    }
}