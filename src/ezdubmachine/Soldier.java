package ezdubmachine;
import battlecode.common.*;

public class Soldier extends RobotPlayer{

    static MapLocation enemyArchonLocation = null;
    static Direction soldierDirection;
    static MapLocation parentLocation;

    Soldier() throws GameActionException {
        soldierDirection = getSpawnDirection();
        parentLocation = getParentArchonLocation();
    }
    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runSoldier() throws GameActionException {
        senseEnemyArchon();

        for (int i = 0; i < 4; i++) {
            enemyArchonLocation = getEnemyArchon(i);
            if (enemyArchonLocation != null) {
                break;
            }
        }


        // Try to attack someone
        
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);

        for (RobotInfo robot : enemies) {
            MapLocation enemyLocation = robot.getLocation();
            if (robot.type == RobotType.SOLDIER) {
                if (rc.canAttack(enemyLocation)) {
                    rc.attack(enemyLocation);
                }
                //moveInDirection(rc.getLocation().directionTo(parentLocation));
            	moveInDirection(enemyLocation.directionTo(rc.getLocation()));
            }
        }

        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }

        if (enemyArchonLocation != null) {
            if (rc.canSenseLocation(enemyArchonLocation)) {
                RobotInfo robot = rc.senseRobotAtLocation(enemyArchonLocation);
                if (robot != null && robot.getType() != RobotType.ARCHON) {
                    removeEnemyArchon(enemyArchonLocation);
                }
            }
        }

        // Also try to move randomly.
        if (rc.isMovementReady()) {
            if (enemyArchonLocation != null) {
                moveToEnemyArchon(enemyArchonLocation);
            } else {
                if (rc.onTheMap(rc.getLocation().add(soldierDirection)) == false) {
                    soldierDirection = rebound(soldierDirection);
                }
                moveInDirection(soldierDirection);
            }
        }
       
    }

    static void moveToEnemyArchon(MapLocation loc) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(loc);
        Direction leftDirection = dir;
        Direction rightDirection = dir;
        Direction bestDirection = null;
        int rubbleAmount = 101;
        MapLocation me = rc.getLocation();
        int n = 2;
        if (rc.getLocation().distanceSquaredTo(enemyArchonLocation) < 36) {
            n = 3;
        }
        for (int i = 0; i < n; i++) {
            if (rc.canMove(leftDirection)) {
                if (rc.canSenseLocation(me.add(leftDirection))) {
                    if (rc.senseRubble(me.add(leftDirection)) < rubbleAmount) {
                        bestDirection = leftDirection;
                        rubbleAmount = rc.senseRubble(me.add(leftDirection));
                    }
                }
            }
            if (rc.canMove(rightDirection)) {
                if (rc.canSenseLocation(me.add(rightDirection))) {
                    if (rc.senseRubble(me.add(rightDirection)) < rubbleAmount) {
                        bestDirection = rightDirection;
                        rubbleAmount = rc.senseRubble(me.add(rightDirection));
                    }
                }
            }
            leftDirection = leftDirection.rotateLeft();
            rightDirection = rightDirection.rotateRight();
        }
        if (bestDirection != null) {
            rc.move(bestDirection);
        }
    }
}
