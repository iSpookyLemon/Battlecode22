package ezdubmachine;
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
        
        if (rc.getTeamLeadAmount(rc.getTeam()) > 1000) {
            minerDelay = 4;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) > 500) {
            minerDelay = 3;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) > 250) {
            minerDelay = 2;
        } else {
            minerDelay = 1;
        }
        */
        MapLocation me = rc.getLocation();
        Direction leadDirection = null;
        int bestD = 10000;
        int bestL = 0;

        for (MapLocation loc : rc.senseNearbyLocationsWithLead()) {
            int leadAmount = rc.senseLead(loc);
            int distance = Math.max(Math.abs(me.x - loc.x), Math.abs(me.y - loc.y));
            //int distance = me.distanceSquaredTo(loc);
            if (leadAmount > 1) {
                if (distance < bestD || (distance == bestD && leadAmount > bestL)) {
                    bestD = distance;
                    bestL = leadAmount;
                    leadDirection = me.directionTo(loc);
                }
            }
        }
        
        RobotInfo[] robots = rc.senseNearbyRobots(5, rc.getTeam());
        int miners = 0;
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.MINER) {
                miners++;
            }
        }

        if (robotsBuilt < 6) {
            buildType = RobotType.MINER;
        } else {
            if (miners > 4) { 
                buildType = RobotType.SOLDIER;
            } else {
                if (robotsBuilt % 2 == 0) {
                    buildType = RobotType.SOLDIER;
            } else {
                    buildType = RobotType.MINER;
            }
            }
        }
       
        if (rc.getTeamLeadAmount(rc.getTeam()) > 400) {
            buildType = RobotType.SOLDIER;
        }

        if (rc.isActionReady()) {
            if (leadDirection != null) {
                if (buildType == RobotType.MINER) {
                    if (rc.canBuildRobot(buildType, leadDirection)) {
                        rc.buildRobot(buildType, leadDirection);
                        robotsBuilt++;
                    }
                }
            }
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