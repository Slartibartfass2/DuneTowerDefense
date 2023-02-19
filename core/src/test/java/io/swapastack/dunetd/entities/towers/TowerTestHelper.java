package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.NotNull;

import lombok.NonNull;

final class TowerTestHelper {

    static HostileUnit createHostileUnitWithHealth(Vector2 position, int health) {
        return new HostileUnit(position, 0, health, null) {
            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    static HostileUnit createHostileUnitWithSpeed(Vector2 position, int speed) {
        return new HostileUnit(position, speed, 1, null) {
            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    static Tower createDummyTower() {
        return new Tower(Vector2.ZERO, 0, 0, 0, null, 0) {
            @Override
            protected boolean target(boolean killOrder, @NotNull @NonNull HostileUnit... hostileUnits) {
                return false;
            }
        };
    }

    static Tower createTowerWithReloadTimeAndTargetReturnsTrueOnce(int reloadTimeInMilliseconds) {
        return new Tower(Vector2.ZERO, 0, 0, reloadTimeInMilliseconds, null, 0) {
            private int once;

            @Override
            protected boolean target(boolean killOrder, @NotNull @NonNull HostileUnit... hostileUnits) {
                return once++ == 0;
            }
        };
    }

    static Tower createTowerWithRange(int range) {
        return new Tower(Vector2.ZERO, range, 0, 0, null, 0) {
            @Override
            protected boolean target(boolean killOrder, @NotNull @NonNull HostileUnit... hostileUnits) {
                return false;
            }
        };
    }
}
