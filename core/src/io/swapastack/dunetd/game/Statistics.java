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
    private static final String UNEXPECTED_VALUE = "Unexpected value: %s";

    private static final String INFANTRY_TEXT = "- Infantry";

    private static final String HARVESTER_TEXT = "- Harvester";

    private static final String BOSS_UNIT_TEXT = "- Boss unit";

    // Counters for how many towers were build
    private int guardTowersBuilt;
    private int bombTowersBuilt;
    private int soundTowersBuilt;

    // Counters for how many hostile units were killed by towers
    private int infantriesKilledByTowers;
    private int harvestersKilledByTowers;
    private int bossUnitsKilledByTowers;

    // Counters for how many hostile units were killed by the shai hulud
    private int infantriesKilledByShaiHulud;
    private int harvestersKilledByShaiHulud;
    private int bossUnitsKilledByShaiHulud;

    // Counters for how many towers were destroyed by the shai hulud
    private int guardTowersDestroyedByShaiHulud;
    private int bombTowersDestroyedByShaiHulud;
    private int soundTowersDestroyedByShaiHulud;

    // Counters for how many hostile units reached the end portal
    private int infantriesReachedEndPortal;
    private int harvestersReachedEndPortal;
    private int bossUnitsReachedEndPortal;

    public Statistics() {
        bossUnitsReachedEndPortal = 0;
        harvestersReachedEndPortal = 0;
        infantriesReachedEndPortal = 0;
        soundTowersDestroyedByShaiHulud = 0;
        bombTowersDestroyedByShaiHulud = 0;
        guardTowersDestroyedByShaiHulud = 0;
        bossUnitsKilledByShaiHulud = 0;
        harvestersKilledByShaiHulud = 0;
        infantriesKilledByShaiHulud = 0;
        bossUnitsKilledByTowers = 0;
        harvestersKilledByTowers = 0;
        infantriesKilledByTowers = 0;
        soundTowersBuilt = 0;
        bombTowersBuilt = 0;
        guardTowersBuilt = 0;
    }

    public void builtTower(@NonNull TowerEnum towerEnum) {
        switch (towerEnum) {
            case GUARD_TOWER -> guardTowersBuilt++;
            case BOMB_TOWER -> bombTowersBuilt++;
            case SOUND_TOWER -> soundTowersBuilt++;
            default -> throw new IllegalStateException(UNEXPECTED_VALUE.formatted(towerEnum));
        }
    }

    public void killedHostileUnitByTower(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesKilledByTowers++;
            case HARVESTER -> harvestersKilledByTowers++;
            case BOSS_UNIT -> bossUnitsKilledByTowers++;
            default -> throw new IllegalStateException(UNEXPECTED_VALUE.formatted(hostileUnitEnum));
        }
    }

    public void killedHostileUnitByShaiHulud(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesKilledByShaiHulud++;
            case HARVESTER -> harvestersKilledByShaiHulud++;
            case BOSS_UNIT -> bossUnitsKilledByShaiHulud++;
            default -> throw new IllegalStateException(UNEXPECTED_VALUE.formatted(hostileUnitEnum));
        }
    }

    public void destroyedTowerByShaiHulud(@NonNull TowerEnum towerEnum) {
        switch (towerEnum) {
            case GUARD_TOWER -> guardTowersDestroyedByShaiHulud++;
            case BOMB_TOWER -> bombTowersDestroyedByShaiHulud++;
            case SOUND_TOWER -> soundTowersDestroyedByShaiHulud++;
            default -> throw new IllegalStateException(UNEXPECTED_VALUE.formatted(towerEnum));
        }
    }

    public void hostileUnitReachedEndPortal(@NonNull HostileUnitEnum hostileUnitEnum) {
        switch (hostileUnitEnum) {
            case INFANTRY -> infantriesReachedEndPortal++;
            case HARVESTER -> harvestersReachedEndPortal++;
            case BOSS_UNIT -> bossUnitsReachedEndPortal++;
            default -> throw new IllegalStateException(UNEXPECTED_VALUE.formatted(hostileUnitEnum));
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
        hostileUnitsKilledByTowersTable.add(new StatisticsRow(INFANTRY_TEXT, infantriesKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();
        hostileUnitsKilledByTowersTable.add(new StatisticsRow(HARVESTER_TEXT, harvestersKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();
        hostileUnitsKilledByTowersTable.add(new StatisticsRow(BOSS_UNIT_TEXT, bossUnitsKilledByTowers,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_TOWER)).left().growX().row();

        // Hostile units killed
        int hostileUnitsKilledByShaiHulud =
                infantriesKilledByShaiHulud + harvestersKilledByShaiHulud + bossUnitsKilledByShaiHulud;
        var hostileUnitsKilledByShaiHuludTable = new VisTable(true);
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow("Killed hostile units with shai hulud",
                hostileUnitsKilledByShaiHulud, POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow(INFANTRY_TEXT, infantriesKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow(HARVESTER_TEXT, harvestersKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();
        hostileUnitsKilledByShaiHuludTable.add(new StatisticsRow(BOSS_UNIT_TEXT, bossUnitsKilledByShaiHulud,
                POINTS_FOR_HOSTILE_UNIT_KILLED_BY_SHAI_HULUD)).left().growX().row();

        // Hostile units reached the end portal
        int hostileUnitsReachedEndPortal =
                infantriesReachedEndPortal + harvestersReachedEndPortal + bossUnitsReachedEndPortal;
        var hostileUnitsReachedEndPortalTable = new VisTable(true);
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow("Hostile units reached the end portal",
                hostileUnitsReachedEndPortal, POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow(INFANTRY_TEXT, infantriesReachedEndPortal,
                POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow(HARVESTER_TEXT, harvestersReachedEndPortal,
                POINTS_FOR_HOSTILE_UNIT_REACHED_END_PORTAL)).left().growX().row();
        hostileUnitsReachedEndPortalTable.add(new StatisticsRow(BOSS_UNIT_TEXT, bossUnitsReachedEndPortal,
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
        StatisticsRow(@NonNull String name, int count, int pointsPerCount) {
            super(true);
            add(new VisLabel(name)).left().growX();
            add(new VisLabel(count + " x " + pointsPerCount + " Spice")).right();
            add(new VisLabel(" = " + count * pointsPerCount)).right();
        }
    }
}
