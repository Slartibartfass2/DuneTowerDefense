package io.swapastack.dunetd.game;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.hostileunits.HostileUnitEnum;

import lombok.NonNull;
import lombok.ToString;

/**
 * Class to collect statistics data from the game. The data consists of:<br>
 * - the amount and types of towers built<br>
 * - the amount and types of hostile units killed by towers<br>
 * - the amount and types of hostile units killed by the shai hulud<br>
 * - the amount and types of towers destroyed by the shai hulud<br>
 * - the amount and types of hostile units, which reached the end portal
 */
@ToString
public final class Statistics {

    // Constants
    private static final int POINTS_FOR_TOWER_BUILT = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_TOWER_BUILT");
    private static final int POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER");
    private static final int POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD");
    private static final int POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL");
    private static final int POINTS_FOR_TOWER_DESTROYED = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_TOWER_DESTROYED");
    private static final int POINTS_FOR_REMAINING_SPICE = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_REMAINING_SPICE");
    private static final int POINTS_FOR_REMAINING_HEALTH = Configuration.getInstance()
            .getIntProperty("POINTS_FOR_REMAINING_HEALTH");

    // Counters for how many towers were build
    private int guardTowersBuilt = 0;
    private int bombTowersBuilt = 0;
    private int soundTowersBuilt = 0;

    // Counters for how many hostile units were killed by towers
    private int infantriesKilledByTowers = 0;
    private int harvestersKilledByTowers = 0;
    private int bossUnitsKilledByTowers = 0;

    // Counters for how many hostile units were killed by the shai hulud
    private int infantriesKilledByShaiHulud = 0;
    private int harvestersKilledByShaiHulud = 0;
    private int bossUnitsKilledByShaiHulud = 0;

    // Counters for how many towers were destroyed by the shai hulud
    private int guardTowersDestroyedByShaiHulud = 0;
    private int bombTowersDestroyedByShaiHulud = 0;
    private int soundTowersDestroyedByShaiHulud = 0;

    // Counters for how many hostile units reached the end portal
    private int infantriesReachedEndPortal = 0;
    private int harvestersReachedEndPortal = 0;
    private int bossUnitsReachedEndPortal = 0;

    public void builtTower(@NonNull TowerEnum towerEnum) {
        switch (towerEnum) {
            case GUARD_TOWER -> guardTowersBuilt++;
            case BOMB_TOWER -> bombTowersBuilt++;
            case SOUND_TOWER -> soundTowersBuilt++;
        }
    }

    public void killedHostileUnitByTower(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesKilledByTowers++;
            case HARVESTER -> harvestersKilledByTowers++;
            case BOSS_UNIT -> bossUnitsKilledByTowers++;
        }
    }

    public void killedHostileUnitByShaiHulud(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesKilledByShaiHulud++;
            case HARVESTER -> harvestersKilledByShaiHulud++;
            case BOSS_UNIT -> bossUnitsKilledByShaiHulud++;
        }
    }

    public void destroyedTowerByShaiHulud(@NonNull TowerEnum towerEnum) {
        switch (towerEnum) {
            case GUARD_TOWER -> guardTowersDestroyedByShaiHulud++;
            case BOMB_TOWER -> bombTowersDestroyedByShaiHulud++;
            case SOUND_TOWER -> soundTowersDestroyedByShaiHulud++;
        }
    }

    public void hostileUnitReachedEndPortal(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesReachedEndPortal++;
            case HARVESTER -> harvestersReachedEndPortal++;
            case BOSS_UNIT -> bossUnitsReachedEndPortal++;
        }
    }

    // TODO: Remove from here and put somewhere else
    public VisTable getStatisticsTable() {
        // Towers built
        int towersBuilt = guardTowersBuilt + bombTowersBuilt + soundTowersBuilt;
        var towersBuiltTable = new VisTable(true);
        towersBuiltTable.add(new StatisticsRow("Built towers", towersBuilt, POINTS_FOR_TOWER_BUILT))
                .left().growX().row();
        towersBuiltTable.add(new StatisticsRow("- Guard towers", guardTowersBuilt, POINTS_FOR_TOWER_BUILT))
                .left().growX().row();
        towersBuiltTable.add(new StatisticsRow("- Bomb towers", bombTowersBuilt, POINTS_FOR_TOWER_BUILT))
                .left().growX().row();
        towersBuiltTable.add(new StatisticsRow("- Sound towers", soundTowersBuilt, POINTS_FOR_TOWER_BUILT))
                .left().growX().row();

        // Hostile units killed by towers
        int hostileUnitsKilledByTowers = infantriesKilledByTowers + harvestersKilledByTowers + bossUnitsKilledByTowers;
        var hostileUnitsKilledByTowersTable = new VisTable(true);
        hostileUnitsKilledByTowersTable.add(new StatisticsRow("Killed hostile units with towers",
                hostileUnitsKilledByTowers, POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();
        hostileUnitsKilledByTowersTable.add(new StatisticsRow("- Infantry", infantriesKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();
        hostileUnitsKilledByTowersTable.add(new StatisticsRow("- Harvester", harvestersKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();
        hostileUnitsKilledByTowersTable.add(new StatisticsRow("- Boss unit", bossUnitsKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();

        // Hostile units killed
        int hostileUnitsKilledByShaiHulud =
                infantriesKilledByShaiHulud + harvestersKilledByShaiHulud + bossUnitsKilledByShaiHulud;
        var hostileUnitsKilledByShaiHuludTable = new VisTable(true);
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow("Killed hostile units with shai hulud",
                hostileUnitsKilledByShaiHulud, POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow("- Infantry", infantriesKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow("- Harvester", harvestersKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow("- Boss unit", bossUnitsKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();

        // Hostile units reached the end portal
        int hostileUnitsReachedEndPortal =
                infantriesReachedEndPortal + harvestersReachedEndPortal + bossUnitsReachedEndPortal;
        var hostileUnitsReachedEndPortalTable = new VisTable(true);
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow("Hostile units reached the end portal",
                hostileUnitsReachedEndPortal, POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow("- Infantry", infantriesReachedEndPortal,
                POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow("- Harvester", harvestersReachedEndPortal,
                POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow("- Boss unit", bossUnitsReachedEndPortal,
                POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();

        // Total points
        int result = towersBuilt * POINTS_FOR_TOWER_BUILT;
        result += hostileUnitsKilledByTowers * POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER;
        result += hostileUnitsKilledByShaiHulud * POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD;
        result += hostileUnitsReachedEndPortal * POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL;

        var totalTable = new VisTable(true);
        totalTable.add(new VisLabel("Total points: ")).left().growX();
        totalTable.add(new VisLabel(result + ""));

        var table = new VisTable(true);
        table.add(towersBuiltTable).growX().row();
        table.add(hostileUnitsKilledByTowersTable).growX().row();
        table.add(hostileUnitsKilledByShaiHuludTable).growX().row();
        table.add(hostileUnitsReachedEndPortalTable).growX().row();
        table.addSeparator();
        table.add(totalTable).growX();
        table.left();
        return table;
    }

    @SuppressWarnings("squid:S110")
    private static class StatisticsRow extends VisTable {
        public StatisticsRow(@NonNull String name, int count, int pointsPerCount) {
            super(true);
            add(new VisLabel(name)).left().growX();
            add(new VisLabel(count + " x " + pointsPerCount + " Spice")).right();
            add(new VisLabel(" = " + count * pointsPerCount)).right();
        }
    }
}
