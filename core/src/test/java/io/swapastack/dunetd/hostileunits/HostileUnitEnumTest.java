package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import org.junit.Test;

import static io.swapastack.dunetd.hostileunits.HostileUnitEnum.fromHostileUnit;
import static org.junit.Assert.*;

public class HostileUnitEnumTest {

    static {
        TestHelper.readConfigFile();
    }

    @Test
    public void testFromHostileUnitWithValidArguments() {
        var infantry = new Infantry(Vector2.Zero);
        assertEquals(HostileUnitEnum.INFANTRY, fromHostileUnit(infantry));

        var harvester = new Harvester(Vector2.Zero);
        assertEquals(HostileUnitEnum.HARVESTER, fromHostileUnit(harvester));

        var bossUnit = new BossUnit(Vector2.Zero);
        assertEquals(HostileUnitEnum.BOSS_UNIT, fromHostileUnit(bossUnit));
    }

    @Test
    public void testFromHostileUnitWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> fromHostileUnit(null));
    }

    @Test
    public void testToHostileUnitWithValidArguments() {
        var infantryEnum = HostileUnitEnum.INFANTRY;
        var infantry = infantryEnum.toHostileUnit(Vector2.Zero, null);
        assertNotNull(infantry);

        var harvesterEnum = HostileUnitEnum.HARVESTER;
        var harvester = harvesterEnum.toHostileUnit(Vector2.Zero, null);
        assertNotNull(harvester);

        var bossUnitEnum = HostileUnitEnum.BOSS_UNIT;
        var bossUnit = bossUnitEnum.toHostileUnit(Vector2.Zero, null);
        assertNotNull(bossUnit);
    }

    @Test
    public void testToHostileUnitWithInvalidArguments() {
        var infantryEnum = HostileUnitEnum.INFANTRY;
        assertThrows(IllegalArgumentException.class, () -> infantryEnum.toHostileUnit(null, null));

        var harvesterEnum = HostileUnitEnum.HARVESTER;
        assertThrows(IllegalArgumentException.class, () -> harvesterEnum.toHostileUnit(null, null));

        var bossUnitEnum = HostileUnitEnum.BOSS_UNIT;
        assertThrows(IllegalArgumentException.class, () -> bossUnitEnum.toHostileUnit(null, null));
    }
}