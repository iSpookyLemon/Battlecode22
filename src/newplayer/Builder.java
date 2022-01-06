package newplayer;
import battlecode.common.*;

public class Builder extends RobotPlayer{

    static boolean hasBuiltWatchtower = false;
    static MapLocation watchtowerLocation;
    static RobotInfo watchtowerInfo;
    static boolean moveMode = true;
    static MapLocation parentArchonLocation;

    Builder() throws GameActionException {
        parentArchonLocation = getParentArchonLocation();
    }
    void runBuilder() throws GameActionException {

        if (hasBuiltWatchtower == false && rc.isActionReady() && rc.getLocation().distanceSquaredTo(parentArchonLocation) > 36) {
            for (Direction dir : directions) {
                if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                    rc.buildRobot(RobotType.WATCHTOWER, dir);
                    hasBuiltWatchtower = true;
                    watchtowerLocation = rc.getLocation().add(dir);
                    watchtowerInfo = rc.senseRobotAtLocation(watchtowerLocation);
                    moveMode = false;
                }
            }
        }

        if (moveMode == false) {
            watchtowerInfo = rc.senseRobotAtLocation(watchtowerLocation);
            if (watchtowerInfo != null && watchtowerInfo.getMode() == RobotMode.PROTOTYPE) {
                if (rc.canRepair(watchtowerLocation)) {
                    rc.repair(watchtowerLocation);
                }
            } else {
                moveMode = true;
            }
        } else {
            Direction dir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("I moved!");
            }
        }
    }
}
