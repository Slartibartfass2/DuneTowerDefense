package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TowerEnumTest {

    @Test
    public void testFromTowerWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> TowerEnum.fromTower(null));

        var tower = new Tower(0, 0, 0, 0, 0) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }

            @Override
            protected void idle(float deltaTime) {

            }
        };

        assertThrows(IllegalStateException.class, () -> TowerEnum.fromTower(tower));
    }

    @Test
    public void testFromTowerWithValidArguments() {
        var guardTower = new GuardTower(0, 0);
        assertEquals(TowerEnum.GUARD_TOWER, TowerEnum.fromTower(guardTower));

        var bombTower = new BombTower(0, 0);
        assertEquals(TowerEnum.BOMB_TOWER, TowerEnum.fromTower(bombTower));

        var soundTower = new SoundTower(0, 0);
        assertEquals(TowerEnum.SOUND_TOWER, TowerEnum.fromTower(soundTower));
    }

    @Test
    public void testToTowerExpectedOutput() {
        TestHelper.readConfigFile();

        var towerEnum0 = TowerEnum.GUARD_TOWER;
        var tower = towerEnum0.toTower(0, 0, null);
        assertTrue(tower instanceof GuardTower);

        var towerEnum1 = TowerEnum.BOMB_TOWER;
        tower = towerEnum1.toTower(0, 0, null);
        assertTrue(tower instanceof BombTower);

        var towerEnum2 = TowerEnum.SOUND_TOWER;
        tower = towerEnum2.toTower(0, 0, null);
        assertTrue(tower instanceof SoundTower);
    }

    @Test
    public void testNext() {
        // Test if next element is always element with ordinal + 1, or 0 if it's the last element
        for (int i = 0; i < TowerEnum.values().length; i++) {
            var towerEnum = TowerEnum.values()[i];
            towerEnum = towerEnum.next();
            assertEquals(towerEnum.ordinal(), (i + 1) % TowerEnum.values().length);
        }
    }

    @Test
    public void testPrevious() {
        // Test if previous element is always element with ordinal - 1, or values().length - 1 if it's the first element
        for (int i = 0; i < TowerEnum.values().length; i++) {
            var towerEnum = TowerEnum.values()[i];
            towerEnum = towerEnum.previous();
            assertEquals((i - 1 + TowerEnum.values().length) % TowerEnum.values().length, towerEnum.ordinal());
        }
    }
}