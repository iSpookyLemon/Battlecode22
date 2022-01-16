package sonic;
import battlecode.common.*;

public class Builder extends RobotPlayer{

    static boolean hasBuiltWatchtower = false;
    static boolean hasBuiltLaboratory = false;
    static MapLocation buildingLocation;
    static RobotInfo buildingInfo;
    static boolean moveMode = true;
    static MapLocation parentArchonLocation;

    Builder() throws GameActionException {
        parentArchonLocation = getParentArchonLocation();
    }
    void runBuilder() throws GameActionException {

        if (rc.isActionReady()) {
            if (hasBuiltWatchtower == false) {
                if (rc.getLocation().distanceSquaredTo(parentArchonLocation) > 36) {
                    for (Direction dir : directions) {
                        if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                            rc.buildRobot(RobotType.WATCHTOWER, dir);
                            hasBuiltWatchtower = true;
                            buildingLocation = rc.getLocation().add(dir);
                            buildingInfo = rc.senseRobotAtLocation(buildingLocation);
                            moveMode = false;
                        }
                    }
                }
            } else if (hasBuiltLaboratory == false) {
                if (rc.getLocation().distanceSquaredTo(parentArchonLocation) > 100) {
                    for (Direction dir : directions) {
                        if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                            rc.buildRobot(RobotType.LABORATORY, dir);
                            hasBuiltLaboratory = true;
                            buildingLocation = rc.getLocation().add(dir);
                            buildingInfo = rc.senseRobotAtLocation(buildingLocation);
                            moveMode = false;
                        }
                    }
                }
            }
        }

        if (moveMode == false) {
            buildingInfo = rc.senseRobotAtLocation(buildingLocation);
            if (buildingInfo != null && buildingInfo.getMode() == RobotMode.PROTOTYPE) {
                if (rc.canRepair(buildingLocation)) {
                    rc.repair(buildingLocation);
                }
            } else {
                moveMode = true;
            }
        } else {
            Direction dir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
    }
}
