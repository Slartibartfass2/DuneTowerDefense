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

class SoundTowerTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");

    @Test
    void testTargetWithInvalidArguments() {
        var soundTower = getRandomSoundTower();
        Assertions.assertThrows(IllegalArgumentException.class, () -> soundTower.target(null, false));
    }

    @Test
    void testTargetWithoutKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var soundTower = new SoundTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero)
        }).toList();

        Assertions.assertFalse(soundTower.target(hostileUnits, false));

        for (var hostileUnit : hostileUnits) {
            Assertions.assertEquals(getSpeed(hostileUnit), getCurrentSpeed(hostileUnit), 0f);
        }
    }

    @Test
    void testTargetWithKillOrder() throws NoSuchFieldException, IllegalAccessException {
        var soundTower = new SoundTower(0, 0);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero),
            new Infantry(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new Harvester(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new BossUnit(new Vector2(SOUND_TOWER_RANGE, 0f)),
            new Infantry(new Vector2(SOUND_TOWER_RANGE + 1, 0f)),
            new Harvester(new Vector2(SOUND_TOWER_RANGE + 1, 0f)),
            new BossUnit(new Vector2(SOUND_TOWER_RANGE + 1, 0f))
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
        var random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();

        return new SoundTower(x, y);
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
