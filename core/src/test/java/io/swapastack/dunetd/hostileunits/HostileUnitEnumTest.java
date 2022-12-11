package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HostileUnitEnumTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testFromHostileUnitWithValidArguments() {
        var infantry = new Infantry(Vector2.ZERO);
        Assertions.assertEquals(HostileUnitEnum.INFANTRY, HostileUnitEnum.fromHostileUnit(infantry));

        var harvester = new Harvester(Vector2.ZERO);
        Assertions.assertEquals(HostileUnitEnum.HARVESTER, HostileUnitEnum.fromHostileUnit(harvester));

        var bossUnit = new BossUnit(Vector2.ZERO);
        Assertions.assertEquals(HostileUnitEnum.BOSS_UNIT, HostileUnitEnum.fromHostileUnit(bossUnit));
    }

    @Test
    void testFromHostileUnitWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> HostileUnitEnum.fromHostileUnit(null));
    }

    @Test
    void testToHostileUnitWithValidArguments() {
        var infantryEnum = HostileUnitEnum.INFANTRY;
        var infantry = infantryEnum.toHostileUnit(Vector2.ZERO, null);
        Assertions.assertNotNull(infantry);

        var harvesterEnum = HostileUnitEnum.HARVESTER;
        var harvester = harvesterEnum.toHostileUnit(Vector2.ZERO, null);
        Assertions.assertNotNull(harvester);

        var bossUnitEnum = HostileUnitEnum.BOSS_UNIT;
        var bossUnit = bossUnitEnum.toHostileUnit(Vector2.ZERO, null);
        Assertions.assertNotNull(bossUnit);
    }

    @Test
    void testToHostileUnitWithInvalidArguments() {
        var infantryEnum = HostileUnitEnum.INFANTRY;
        Assertions.assertThrows(IllegalArgumentException.class, () -> infantryEnum.toHostileUnit(null, null));

        var harvesterEnum = HostileUnitEnum.HARVESTER;
        Assertions.assertThrows(IllegalArgumentException.class, () -> harvesterEnum.toHostileUnit(null, null));

        var bossUnitEnum = HostileUnitEnum.BOSS_UNIT;
        Assertions.assertThrows(IllegalArgumentException.class, () -> bossUnitEnum.toHostileUnit(null, null));
    }
}
