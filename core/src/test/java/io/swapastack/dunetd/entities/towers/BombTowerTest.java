package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BombTowerTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int INFANTRY_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("INFANTRY_INITIAL_HEALTH");
    private static final int HARVESTER_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("HARVESTER_INITIAL_HEALTH");
    private static final int BOSS_UNIT_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_INITIAL_HEALTH");
    private static final float BOMB_TOWER_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_RANGE");
    private static final float BOMB_TOWER_AREA_DAMAGE_RANGE = Configuration.getInstance()
            .getFloatProperty("BOMB_TOWER_AREA_DAMAGE_RANGE");

    @Test
    void testTargetWithInvalidArguments() {
        var bombTower = getNewRandomBombTower();
        assertThrows(IllegalArgumentException.class, () -> bombTower.target(null, false));
    }

    @Test
    void testTargetWithoutKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var bombTower = new BombTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero)
        }).toList();

        assertFalse(bombTower.target(hostileUnits, false));

        assertEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(0)), 0f);
        assertEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(1)), 0f);
        assertEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(2)), 0f);
    }

    @Test
    void testTargetWithKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var bombTower = new BombTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero),
            new Infantry(new Vector2(BOMB_TOWER_AREA_DAMAGE_RANGE, 0f)),
            new Harvester(new Vector2(BOMB_TOWER_AREA_DAMAGE_RANGE, 0f)),
            new BossUnit(new Vector2(BOMB_TOWER_AREA_DAMAGE_RANGE, 0f)),
            new Infantry(new Vector2(BOMB_TOWER_RANGE + 1, 0f)),
            new Harvester(new Vector2(BOMB_TOWER_RANGE + 1, 0f)),
            new BossUnit(new Vector2(BOMB_TOWER_RANGE + 1, 0f))
        }).toList();

        assertTrue(bombTower.target(hostileUnits, true));

        assertNotEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(0)), 0f);
        assertNotEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(1)), 0f);
        assertNotEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(2)), 0f);
        assertNotEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(3)), 0f);
        assertNotEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(4)), 0f);
        assertNotEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(5)), 0f);
        assertEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(6)), 0f);
        assertEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(7)), 0f);
        assertEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(8)), 0f);
    }

    BombTower getNewRandomBombTower() {
        return new BombTower(new Random().nextInt(), new Random().nextInt());
    }

    int getHealth(HostileUnit hostileUnit) throws NoSuchFieldException, IllegalAccessException {
        var field = HostileUnit.class.getDeclaredField("health");
        field.setAccessible(true);
        return (int) field.get(hostileUnit);
    }
}
