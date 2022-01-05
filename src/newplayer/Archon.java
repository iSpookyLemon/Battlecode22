package newplayer;
import battlecode.common.*;

public class Archon extends RobotPlayer {

    static RobotType buildType = RobotType.MINER;

    Archon() throws GameActionException {

    }
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runArchon() throws GameActionException {
        if (turnCount % 50 == 0) {
            buildType = RobotType.BUILDER;
        }
        // Pick a direction to build in.
        Direction dir = directions[rng.nextInt(directions.length)];
        // Let's try to build a miner.
        if (rc.isActionReady()) {
            if (buildType == RobotType.MINER && rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
            } else if (buildType == RobotType.BUILDER && rc.canBuildRobot(RobotType.BUILDER, dir)) {
                rc.buildRobot(RobotType.BUILDER, dir);
                buildType = RobotType.MINER;
            }
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