package newplayer;
import battlecode.common.*;

public class Miner extends RobotPlayer {
    Miner() throws GameActionException {
        Team myTeam = rc.getTeam();
        RobotInfo[] robots = rc.senseNearbyRobots(2, myTeam);
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.ARCHON) {
                MapLocation archonLocation = robot.getLocation();
                minerDirection = archonLocation.directionTo(rc.getLocation());
            }
        }
    }
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runMiner() throws GameActionException {
        // Try to mine on squares around us.

        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        for (RobotInfo robot : enemies) {
            if (robot.getType() == RobotType.ARCHON) {
                rc.writeSharedArray(0, locationToInt(robot.getLocation()));
                rc.writeSharedArray(1, 1);
            }
        }

        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation)) {
                    rc.mineLead(mineLocation);
                }
            }
        }

        // Move in assigned direction
        if (rc.isMovementReady()) {
            MapLocation moveLocation = rc.getLocation().add(minerDirection);
            if (rc.onTheMap(moveLocation) == false) {
                Direction[] cardinalDirections = Direction.cardinalDirections();
                boolean isCardinalDirection = false;
                for (Direction dir : cardinalDirections) {
                    if (minerDirection == dir) {
                        isCardinalDirection = true;
                    }
                }
                if (isCardinalDirection) {
                    minerDirection = minerDirection.opposite().rotateRight();
                } else {
                    if (rc.onTheMap(rc.getLocation().add(minerDirection.rotateRight().rotateRight()))) {
                        minerDirection = minerDirection.rotateRight().rotateRight();
                    } else if (rc.onTheMap(rc.getLocation().add(minerDirection.rotateLeft().rotateLeft()))) {
                        minerDirection = minerDirection.rotateLeft().rotateLeft();
                    } else {
                        minerDirection = minerDirection.opposite();
                    }
                }
            }
            if (rc.canMove(minerDirection)) {
                rc.move(minerDirection);
            } else {
                for (Direction dir : directions) {
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
            }
        }
    }
}
