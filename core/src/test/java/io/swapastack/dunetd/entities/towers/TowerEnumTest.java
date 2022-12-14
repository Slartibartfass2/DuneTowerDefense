package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TowerEnumTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void whenFromTowerIsCalledWithValidTowers_thenCorrectEnumIsReturned() {
        var guardTower = new GuardTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.GUARD_TOWER, TowerEnum.fromTower(guardTower));

        var bombTower = new BombTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.BOMB_TOWER, TowerEnum.fromTower(bombTower));

        var soundTower = new SoundTower(Vector2.ZERO, null);
        Assertions.assertEquals(TowerEnum.SOUND_TOWER, TowerEnum.fromTower(soundTower));
    }

    @Test
    void whenFromTowerIsCalledWithAnonymousTower_thenExceptionIsThrown() {
        var tower = TowerTestHelper.createDummyTower();

        Assertions.assertThrows(IllegalStateException.class, () -> TowerEnum.fromTower(tower));
    }

    @Test
    void whenToTowerIsCalled_thenCorrectTowerIsCreated() {
        var guardTowerEnum = TowerEnum.GUARD_TOWER;
        var guardTower = guardTowerEnum.toTower(Vector2.ZERO, null);
        Assertions.assertInstanceOf(GuardTower.class, guardTower);

        var bombTowerEnum = TowerEnum.BOMB_TOWER;
        var bombTower = bombTowerEnum.toTower(Vector2.ZERO, null);
        Assertions.assertInstanceOf(BombTower.class, bombTower);

        var soundTowerEnum = TowerEnum.SOUND_TOWER;
        var soundTower = soundTowerEnum.toTower(Vector2.ZERO, null);
        Assertions.assertInstanceOf(SoundTower.class, soundTower);
    }
}
