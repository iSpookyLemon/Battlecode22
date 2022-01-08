package newplayer;
import battlecode.common.*;

public class Archon extends RobotPlayer {

    static RobotType buildType;
    static int robotsBuilt = 0;
    static int soldierDelay = 4;

    Archon() throws GameActionException {

    }
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runArchon() throws GameActionException {

        if (rc.getTeamLeadAmount(rc.getTeam()) < 250) {
            soldierDelay = 4;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) < 500) {
            soldierDelay = 3;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) < 1000) {
            soldierDelay = 2;
        } else {
            soldierDelay = 1;
        }

        Direction dir = directions[rng.nextInt(directions.length)];
        if (robotsBuilt % soldierDelay == 0) {
            buildType = RobotType.SOLDIER;
        } else {
            buildType = RobotType.MINER;
        }
        // Let's try to build a miner.
        if (rc.canBuildRobot(buildType, dir)) {
            rc.buildRobot(buildType, dir);
            robotsBuilt++;
        }
        /*
        if (rng.nextBoolean()) {
            // Let's try to build a miner.
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
            }
        } else {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
            }
        }
        */
    }
}