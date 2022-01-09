package newplayer;
import battlecode.common.*;

public class Archon extends RobotPlayer {

    static RobotType buildType;
    static int robotsBuilt = 0;
    static int soldierDelay = 4;
    static int i = 0;

    Archon() throws GameActionException {

    }
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runArchon() throws GameActionException {
        /*
        if (rc.getTeamLeadAmount(rc.getTeam()) < 250) {
            soldierDelay = 4;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) < 500) {
            soldierDelay = 3;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) < 1000) {
            soldierDelay = 2;
        } else {
            soldierDelay = 1;
        }
        */
        
        RobotInfo[] robots = rc.senseNearbyRobots(9, rc.getTeam());
        int miners = 0;
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.MINER) {
                miners++;
            }
        }

        if (miners > 5) {
            buildType = RobotType.SOLDIER;
        } else {
            if (robotsBuilt % 2 == 0) {
                buildType = RobotType.SOLDIER;
            } else {
                buildType = RobotType.MINER;
            }
        }

        if (rc.isActionReady()) {
            for (int x = 0; x < 8; x++) {
                Direction dir = directions[i];
                if (rc.canBuildRobot(buildType, dir)) {
                    rc.buildRobot(buildType, dir);
                    robotsBuilt++;
                    i = increaseI(i);
                    break;
                }
                i = increaseI(i);
            }
        }
    }

    int increaseI(int i) {
        if (i == 7) {
            return 0;
        } else {
            return i + 1;
        }
    }
}