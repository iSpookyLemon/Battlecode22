package ezdubmachine;
import battlecode.common.*;
import java.util.ArrayList;
import java.util.Vector;

public class Soldier extends RobotPlayer{

    static MapLocation enemyArchonLocation = null;
    static Direction soldierDirection;
    static MapLocation parentLocation;
    static ArrayList<MapLocation> enemyLocations = new ArrayList<MapLocation>();
    static ArrayList<MapLocation> friendLocations = new ArrayList<MapLocation>();

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
        MapLocation me = rc.getLocation();

        for (int i = 0; i < 4; i++) {
            enemyArchonLocation = getEnemyArchon(i);
            if (enemyArchonLocation != null) {
                break;
            }
        }


        // Sense for enemy soldiers
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
        enemyLocations.clear();
        for (RobotInfo robot : enemies) {
            if (robot.type == RobotType.SOLDIER) {
                MapLocation enemyLocation = robot.getLocation();
                enemyLocations.add(enemyLocation);
            }
        }

        //ATTACK
        if (rc.isActionReady()) {
            if (enemyLocations.size() > 0) {
                if (rc.canAttack(enemyLocations.get(0))) {
                    rc.attack(enemyLocations.get(0));
                }
            } else {
                if (enemies.length > 0) {
                    MapLocation toAttack = enemies[0].location;
                    if (rc.canAttack(toAttack)) {
                        rc.attack(toAttack);
                    }
                }
            }
        }
        
        // If there are enemy soldiers
        if (rc.isMovementReady()) {
            // Move away from enemy soldiers
            if (enemyLocations.size() > 0) {
                Vector<Vector<Integer>> vectors = locationToVector(enemyLocations);
                Vector<Integer> sumVector = vectorAddition(vectors);
                MapLocation loc = new MapLocation(me.x + sumVector.get(0), me.y + sumVector.get(1));
                Direction dir = me.directionTo(loc);
                soldierDirection = dir.opposite();
                moveInDirection(dir);
                //moveInDirection(enemyLocations.get(0).directionTo(me));
            //If there is an enemy archon to go to
            } else if (enemyArchonLocation != null) {
                moveToEnemyArchon(enemyArchonLocation);
            } else {
                //Sense for team robots
                RobotInfo[] robots = rc.senseNearbyRobots(16, rc.getTeam());
                friendLocations.clear();
                for (RobotInfo robot : robots) {
                    if (robot.type == RobotType.SOLDIER) {
                        MapLocation friendLocation = robot.getLocation();
                        friendLocations.add(friendLocation);
                    }
                }
                /*
                //If there are teammates
                if (friendLocations.size() > 0) {
                    Vector<Vector<Integer>> vectors = locationToVector(friendLocations);
                    Vector<Integer> sumVector = vectorAddition(vectors);
                    MapLocation loc = new MapLocation(me.x + sumVector.get(0), me.y + sumVector.get(1));
                    Direction dir = me.directionTo(loc);
                    soldierDirection = dir;
                    moveInDirection(dir);
                } else {*/
                    //Move in direction
                    if (rc.onTheMap(rc.getLocation().add(soldierDirection)) == false) {
                        soldierDirection = rebound(soldierDirection);
                    }
                    moveInDirection(soldierDirection);
                //}
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
