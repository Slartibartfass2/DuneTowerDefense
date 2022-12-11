package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SoundTowerTest {

    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testTargetWithInvalidArguments() {
        var soundTower = getRandomSoundTower();
        Assertions.assertThrows(IllegalArgumentException.class, () -> soundTower.target(null, false));
    }

    @Test
    void testTargetWithoutKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var soundTower = new SoundTower(Vector2.ZERO, null);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.ZERO),
            new Harvester(Vector2.ZERO),
            new BossUnit(Vector2.ZERO),
        }).toList();

        Assertions.assertFalse(soundTower.target(hostileUnits, false));

        for (var hostileUnit : hostileUnits) {
            Assertions.assertEquals(getSpeed(hostileUnit), getCurrentSpeed(hostileUnit), 0f);
        }
    }

    @Test
    void testTargetWithKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var soundTower = new SoundTower(Vector2.ZERO, null);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.ZERO),
            new Harvester(Vector2.ZERO),
            new BossUnit(Vector2.ZERO),
            new Infantry(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new Harvester(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new BossUnit(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new Infantry(new Vector2(SOUND_TOWER_RANGE + 1, 0f)),
            new Harvester(new Vector2(SOUND_TOWER_RANGE + 1, 0f)),
            new BossUnit(new Vector2(SOUND_TOWER_RANGE + 1, 0f)),
        }).toList();

        Assertions.assertTrue(soundTower.target(hostileUnits, true));

        for (int i = 0; i < hostileUnits.size(); i++) {
            var hostileUnit = hostileUnits.get(i);
            if (i < 6) {
                Assertions.assertNotEquals(getSpeed(hostileUnit), getCurrentSpeed(hostileUnit), 0f);
            } else {
                Assertions.assertEquals(getSpeed(hostileUnit), getCurrentSpeed(hostileUnit), 0f);
            }
        }
    }

    SoundTower getRandomSoundTower() {
        return new SoundTower(new Vector2(new Random().nextInt(), new Random().nextInt()), null);
    }

    float getSpeed(HostileUnit hostileUnit) throws NoSuchFieldException, IllegalAccessException {
        var field = HostileUnit.class.getDeclaredField("speed");
        field.setAccessible(true);
        return (float) field.get(hostileUnit);
    }

    float getCurrentSpeed(HostileUnit hostileUnit) throws NoSuchFieldException, IllegalAccessException {
        var field = HostileUnit.class.getDeclaredField("currentSpeed");
        field.setAccessible(true);
        return (float) field.get(hostileUnit);
    }
}
