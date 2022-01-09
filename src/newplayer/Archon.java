package newplayer;
import battlecode.common.*;

public class Archon extends RobotPlayer {

    static RobotType buildType;
    static RobotType buildType2 = RobotType.SAGE;
    static int robotsBuilt = 0;
    static int soldierDelay = 4;
    static int minerDelay = 4;

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
        
        if (rc.getTeamLeadAmount(rc.getTeam()) > 1000) {
            minerDelay = 4;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) > 500) {
            minerDelay = 3;
        } else if (rc.getTeamLeadAmount(rc.getTeam()) > 250) {
            minerDelay = 2;
        } else {
            minerDelay = 1;
        }
      

        Direction dir = directions[rng.nextInt(directions.length)];
        if (robotsBuilt % soldierDelay == 0) {
            buildType = RobotType.SOLDIER;
        } else if (robotsBuilt % minerDelay == 0){
            buildType = RobotType.MINER;
        }else {
        	buildType = RobotType.SAGE;
        }
        
        if (rc.getTeamLeadAmount(rc.getTeam()) > 200) {
        	buildType = RobotType.BUILDER;
        }
        // Let's try to build a miner.
        if (rc.canBuildRobot(buildType2, dir)) {
            rc.buildRobot(buildType2, dir);
            robotsBuilt++;
        }else{
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