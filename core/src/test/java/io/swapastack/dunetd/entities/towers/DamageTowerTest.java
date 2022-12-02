package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.game.EntityController;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DamageTowerTest {

    static {
        TestHelper.readConfigFile();
    }

    @Test
    void testConstructor1WithValidArguments() {
        assertNotNull(getNewRandomDamageTower());
    }

    @Test
    void testConstructor2WithInvalidArguments() {
        var random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();
        var range = random.nextFloat();
        int damage = random.nextInt();
        int reloadTime = random.nextInt();
        int buildCost = random.nextInt();

        assertThrows(IllegalArgumentException.class, () -> getNewDamageTower(x, y, range, buildCost, damage, reloadTime, null));
    }

    DamageTower getNewRandomDamageTower() {
        return new DamageTower(new Random().nextInt(), new Random().nextInt(), new Random().nextFloat(),
                new Random().nextInt(), new Random().nextInt(), new Random().nextInt()) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }

    DamageTower getNewDamageTower(int x, int y, float range, int buildCost, int damage, int reloadTimeInMs,
                                         EntityController entityController) {
        return new DamageTower(x, y, range, buildCost, damage, reloadTimeInMs, entityController) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }
}