package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.NonNull;

class TowerEnumTest {

    @Test
    void testFromTowerWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> TowerEnum.fromTower(null));

        var tower = new Tower(Vector2.ZERO, 0, 0, 0, null, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };

        Assertions.assertThrows(IllegalStateException.class, () -> TowerEnum.fromTower(tower));
    }

    @Test
    void testFromTowerWithValidArguments() {
        var guardTower = new GuardTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.GUARD_TOWER, TowerEnum.fromTower(guardTower));

        var bombTower = new BombTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.BOMB_TOWER, TowerEnum.fromTower(bombTower));

        var soundTower = new SoundTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.SOUND_TOWER, TowerEnum.fromTower(soundTower));
    }

    @Test
    void testToTowerExpectedOutput() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();

        var towerEnum0 = TowerEnum.GUARD_TOWER;
        var tower = towerEnum0.toTower(Vector2.ZERO, null);
        Assertions.assertTrue(tower instanceof GuardTower);

        var towerEnum1 = TowerEnum.BOMB_TOWER;
        tower = towerEnum1.toTower(Vector2.ZERO, null);
        Assertions.assertTrue(tower instanceof BombTower);

        var towerEnum2 = TowerEnum.SOUND_TOWER;
        tower = towerEnum2.toTower(Vector2.ZERO, null);
        Assertions.assertTrue(tower instanceof SoundTower);
    }

    @Test
    void testNext() {
        // Test if next element is always element with ordinal + 1, or 0 if it's the last element
        for (int i = 0; i < TowerEnum.values().length; i++) {
            var towerEnum = TowerEnum.values()[i];
            towerEnum = towerEnum.next();
            Assertions.assertEquals(towerEnum.ordinal(), (i + 1) % TowerEnum.values().length);
        }
    }

    @Test
    void testPrevious() {
        // Test if previous element is always element with ordinal - 1, or values().length - 1 if it's the first element
        for (int i = 0; i < TowerEnum.values().length; i++) {
            var towerEnum = TowerEnum.values()[i];
            towerEnum = towerEnum.previous();
            Assertions.assertEquals((i - 1 + TowerEnum.values().length) % TowerEnum.values().length,
                    towerEnum.ordinal());
        }
    }
}
