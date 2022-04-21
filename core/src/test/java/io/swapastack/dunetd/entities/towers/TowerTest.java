package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.game.EntityController;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;
import lombok.NonNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class TowerTest {
    
    static {
        TestHelper.readConfigFile();
    }
    
    @Test
    public void testConstructor1WithValidArguments() {
        assertNotNull(getNewRandomTower());
    }
    
    @Test
    public void testConstructor2WithInvalidArguments() {
        Random random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();
        float range = random.nextFloat();
        int reloadTime = random.nextInt();
        int buildCost = random.nextInt();
        
        assertThrows(IllegalArgumentException.class, () -> getNewTower(x, y, range, reloadTime, buildCost, null));
    }
    
    @Test
    public void testUpdateOnceWithHostileUnits() {
        var tower = getNewRandomTower(false);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        tower.update(hostileUnits, 0.16f);
        
        var tower1 = getNewRandomTower(true);
        var hostileUnits1 = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        tower1.update(hostileUnits1, 0.16f);
    }
    
    @Test
    public void testUpdateSeveralTimesWithHostileUnits() {
        var tower = getNewRandomTower(false);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        for (int i = 0; i < 10; i++)
            tower.update(hostileUnits, 0.16f);
        
        var tower1 = getNewRandomTower(true);
        var hostileUnits1 = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        for (int i = 0; i < 10; i++)
            tower1.update(hostileUnits1, 0.16f);
    }
    
    @Test
    public void testUpdateWithoutHostileUnits() {
        var tower = getNewRandomTower();
        assertThrows(IllegalArgumentException.class, () -> tower.update(null, 0.16f));
    }
    
    @Test
    public void testTargetWithHostileUnits() {
        var tower = getNewRandomTower();
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        tower.target(hostileUnits, true);
    }
    
    @Test
    public void testTargetWithoutHostileUnits() {
        var tower = getNewRandomTower();
        assertThrows(IllegalArgumentException.class, () -> tower.target(null, true));
    }
    
    @Test
    public void testGetHostileUnitsInRangeWithHostileUnitsInRange() {
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.Zero, 100);
        assertNotNull(hostileUnitsInRange);
        assertEquals(3, hostileUnitsInRange.size());
    }
    
    @Test
    public void testGetHostileUnitsInRangeWithHostileUnitsOutOfRange() {
        int range = 10;
        int outOfRange = range + 1;
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(new Vector2(outOfRange, 0f)),
                new Harvester(new Vector2(outOfRange, 0f)),
                new BossUnit(new Vector2(outOfRange, 0f))
        }).toList();
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.Zero, range * range);
        assertNotNull(hostileUnitsInRange);
        assertEquals(0, hostileUnitsInRange.size());
    }
    
    @Test
    public void testGetHostileUnitsInRangeWithTowerInRange() {
        var tower = getNewTower(0, 0, 10, 10, 100);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(Vector2.Zero),
                new Harvester(Vector2.Zero),
                new BossUnit(Vector2.Zero)
        }).toList();
        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnits);
        assertNotNull(hostileUnitsInRange);
        assertEquals(3, hostileUnitsInRange.size());
    }
    
    @Test
    public void testGetHostileUnitsInRangeWithTowerOutOfRange() {
        int range = 10;
        int outOfRange = range + 1;
        var tower = getNewTower(0, 0, range, 10, 100);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
                new Infantry(new Vector2(outOfRange, 0f)),
                new Harvester(new Vector2(outOfRange, 0f)),
                new BossUnit(new Vector2(outOfRange, 0f))
        }).toList();
        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnits);
        assertNotNull(hostileUnitsInRange);
        assertEquals(0, hostileUnitsInRange.size());
    }
    
    @Test
    public void testRePosition() {
        var tower = getNewRandomTower();
        
        int x = new Random().nextInt();
        int y = new Random().nextInt();
        
        tower.rePosition(x, y);
        
        assertEquals(x, tower.getX());
        assertEquals(y, tower.getY());
    }
    
    @Test
    public void testSetToDebris() {
        var tower = getNewRandomTower();
        tower.setToDebris();
        assertTrue(tower.isDebris);
    }
    
    @Test
    public void testGetRange() {
        float range = new Random().nextFloat();
        var tower = getNewTower(0, 0, range, 100, 100);
        assertEquals(range, tower.range, 0f);
        assertEquals(range, tower.getRange(), 0f);
    }
    
    @Test
    public void testGetBuildCost() {
        var tower = getNewRandomTower();
        assertEquals(tower.buildCost, tower.getBuildCost());
    }
    
    @Test
    public void testIsDebris() {
        var tower = getNewRandomTower();
        tower.isDebris = true;
        assertTrue(tower.isDebris());
    }
    
    @Test
    public void testEquals() {
        var tower = getNewRandomTower();
        var tower1 = getNewRandomTower();
    
        assertNotEquals(tower, tower1);
        
        var tower2 = getNewTower(0, 0, 0, 0, 0);
        var tower3 = getNewTower(0, 0, 0, 0, 0);
        
        assertNotEquals(tower2, tower3);
    }
    
    public Tower getNewRandomTower() {
        return new Tower(new Random().nextInt(), new Random().nextInt(), new Random().nextFloat(), new Random().nextInt(),
                new Random().nextInt()) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
            
            @Override
            protected void idle(float deltaTime) {
            }
        };
    }
    
    public Tower getNewRandomTower(boolean targetReturnValue) {
        return new Tower(new Random().nextInt(), new Random().nextInt(), new Random().nextFloat(), new Random().nextInt(),
                new Random().nextInt()) {
            int counter = 10;
            
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                if (--counter <= 0)
                    return false;
                return targetReturnValue;
            }
            
            @Override
            protected void idle(float deltaTime) {
            }
        };
    }
    
    public Tower getNewTower(int x, int y, float range, int buildCost, int reloadTimeInMs) {
        return new Tower(x, y, range, buildCost, reloadTimeInMs) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
            
            @Override
            protected void idle(float deltaTime) {
            }
        };
    }
    
    public Tower getNewTower(int x, int y, float range, int buildCost, int reloadTimeInMs,
                             EntityController entityController) {
        return new Tower(x, y, range, buildCost, reloadTimeInMs, entityController, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
            
            @Override
            protected void idle(float deltaTime) {
            }
        };
    }
}