package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GuardTowerTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int INFANTRY_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("INFANTRY_INITIAL_HEALTH");
    private static final int HARVESTER_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("HARVESTER_INITIAL_HEALTH");
    private static final int BOSS_UNIT_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_INITIAL_HEALTH");

    @Test
    void testTargetWithInvalidArguments() {
        var guardTower = getNewRandomGuardTower();
        Assertions.assertThrows(IllegalArgumentException.class, () -> guardTower.target(null, false));
    }

    @Test
    void testTargetWithoutKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var guardTower = new GuardTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero)
        }).toList();

        Assertions.assertFalse(guardTower.target(hostileUnits, false));

        Assertions.assertEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(0)), 0f);
        Assertions.assertEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(1)), 0f);
        Assertions.assertEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(2)), 0f);
    }

    @Test
    void testTargetWithKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var guardTower = new GuardTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero)
        }).toList();

        Assertions.assertTrue(guardTower.target(hostileUnits, true));

        Assertions.assertNotEquals(INFANTRY_INITIAL_HEALTH, getHealth(hostileUnits.get(0)), 0f);
        Assertions.assertEquals(HARVESTER_INITIAL_HEALTH, getHealth(hostileUnits.get(1)), 0f);
        Assertions.assertEquals(BOSS_UNIT_INITIAL_HEALTH, getHealth(hostileUnits.get(2)), 0f);
    }

    GuardTower getNewRandomGuardTower() {
        return new GuardTower(new Random().nextInt(), new Random().nextInt());
    }

    int getHealth(HostileUnit hostileUnit) throws NoSuchFieldException, IllegalAccessException {
        var field = HostileUnit.class.getDeclaredField("health");
        field.setAccessible(true);
        return (int) field.get(hostileUnit);
    }
}
