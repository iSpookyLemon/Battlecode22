package newplayer;
import battlecode.common.*;
import java.util.ArrayList;

public class Miner extends RobotPlayer {

    static MapLocation mineLocation;
    static Direction minerDirection;
    static MapLocation parentLocation;

    Miner() throws GameActionException {
        minerDirection = getSpawnDirection();
        parentLocation = getParentArchonLocation();
    }
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runMiner() throws GameActionException {
        // Try to mine on squares around us.
        senseEnemyArchon();

        MapLocation me = rc.getLocation();
        ArrayList<MapLocation> seenLead = new ArrayList<MapLocation>();
        ArrayList<MapLocation> mineLocations = new ArrayList<MapLocation>();

        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(me, rc.getType().visionRadiusSquared)) {
            int leadAmount = rc.senseLead(loc);
            if (leadAmount > 1) {
                if (me.distanceSquaredTo(loc) <= 2) {
                    mineLocations.add(loc);
                }
                seenLead.add(loc);
            }
        }

        for (MapLocation mineLocation : mineLocations) {
            while (rc.canMineGold(mineLocation)) {
                rc.mineGold(mineLocation);
            }
            int leadAmount = rc.senseLead(mineLocation);
            while (rc.canMineLead(mineLocation) && leadAmount > 1) {
                rc.mineLead(mineLocation);
                leadAmount -= 1;
            }
        }

        if (rc.getLocation().distanceSquaredTo(parentLocation) < 16) {
            if (rc.isMovementReady()) {
                MapLocation moveLocation = rc.getLocation().add(minerDirection);
                if (rc.onTheMap(moveLocation) == false) {
                    minerDirection = rebound(minerDirection);
                }
                moveInDirection(minerDirection);
            }
        } else {
            if (mineLocations.size() == 0) {
                if (seenLead.size() > 0) {
                    MapLocation closest = null;
                    int best = 10000;
                    for (MapLocation loc : seenLead) {
                        if (me.distanceSquaredTo(loc) < best) {
                            best = me.distanceSquaredTo(loc);
                            closest = loc;
                        }
                    }
                    moveToLocation(closest);
                } else {
                    if (rc.isMovementReady()) {
                        MapLocation moveLocation = rc.getLocation().add(minerDirection);
                        if (rc.onTheMap(moveLocation) == false) {
                            minerDirection = rebound(minerDirection);
                        }
                        moveInDirection(minerDirection);
                    }
                }
            }
        }
    }
}
