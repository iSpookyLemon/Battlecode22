package ezdubmachine;
import battlecode.common.*;
import java.util.ArrayList;
import java.util.Vector;

public class Miner extends RobotPlayer {

    static Direction minerDirection;
    static MapLocation parentLocation;
    static ArrayList<MapLocation> minerLocations = new ArrayList<MapLocation>();
    static ArrayList<MapLocation> enemyLocations = new ArrayList<MapLocation>();

    Miner() throws GameActionException {
        parentLocation = getParentArchonLocation();
        minerDirection = parentLocation.directionTo(rc.getLocation());
    }
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runMiner() throws GameActionException {
        senseEnemyArchon();

        MapLocation me = rc.getLocation();
        MapLocation seenLead = null;
        int bestD = 10000;
        int bestL = 0;

        for (MapLocation loc : rc.senseNearbyLocationsWithLead()) {
            int leadAmount = rc.senseLead(loc);
            int distance = Math.max(Math.abs(me.x - loc.x), Math.abs(me.y - loc.y));
            //int distance = me.distanceSquaredTo(loc);
            if (leadAmount > 1) {
                if (rc.isActionReady() && distance <= 2) {
                    mine(loc);
                }
                if (distance < bestD || (distance == bestD && leadAmount > bestL)) {
                    bestD = distance;
                    bestL = leadAmount;
                    seenLead = loc;
                }
            }
        }

        // MOVING
        if (rc.isMovementReady()) {
            // Sense for enemies that could attack
            RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            enemyLocations.clear();
            for (RobotInfo robot : enemies) {
                if (robot.type == RobotType.SOLDIER || robot.type == RobotType.WATCHTOWER) {
                    MapLocation enemyLocation = robot.getLocation();
                    enemyLocations.add(enemyLocation);
                }
            }
            //If there are enemies then move away
            if (enemyLocations.size() > 0) {
                Vector<Vector<Integer>> vectors = locationToVector(enemyLocations);
                Vector<Integer> sumVector = vectorAddition(vectors);
                MapLocation loc = new MapLocation(me.x + sumVector.get(0), me.y + sumVector.get(1));
                Direction dir = me.directionTo(loc);
                moveInDirection(dir);
            // If you are close to archon
            } else if (rc.getLocation().distanceSquaredTo(parentLocation) < 16) {
                MapLocation moveLocation = rc.getLocation().add(minerDirection);
                if (rc.onTheMap(moveLocation) == false) {
                    minerDirection = rebound(minerDirection);
                }
                moveInDirection(minerDirection);
            } else {
                if (seenLead != null) {
                    moveToLocation(seenLead);
                } else {
                    //Look for other miners
                    /*
                    RobotInfo[] robots = rc.senseNearbyRobots(16, rc.getTeam());
                    minerLocations.clear();
                    for (RobotInfo robot : robots) {
                        if (robot.type == RobotType.MINER) {
                            MapLocation minerLocation = robot.getLocation();
                            minerLocations.add(minerLocation);
                        }
                    }
                    
                    if (minerLocations.size() > 0) {
                        Vector<Vector<Integer>> vectors = locationToVector(minerLocations);
                        Vector<Integer> sumVector = vectorAddition(vectors);
                        MapLocation loc = new MapLocation(me.x + sumVector.get(0), me.y + sumVector.get(1));
                        Direction dir = me.directionTo(loc);
                        minerDirection = dir;
                        moveInDirection(dir);
                    } else {*/
                        if (rc.isMovementReady()) {
                            MapLocation moveLocation = rc.getLocation().add(minerDirection);
                            if (rc.onTheMap(moveLocation) == false) {
                                minerDirection = rebound(minerDirection);
                            }
                            moveInDirection(minerDirection);
                        }
                    //}
                }
            }
        }
    }

    static void mine(MapLocation mineLocation) throws GameActionException {
        while (rc.canMineGold(mineLocation)) {
            rc.mineGold(mineLocation);
        }
        int leadAmount = rc.senseLead(mineLocation);
        while (rc.canMineLead(mineLocation) && leadAmount > 1) {
            rc.mineLead(mineLocation);
            leadAmount -= 1;
        }
    }
}
