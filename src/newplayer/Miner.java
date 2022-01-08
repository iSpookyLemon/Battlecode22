package newplayer;
import battlecode.common.*;
import java.util.ArrayList;

public class Miner extends RobotPlayer {

    static MapLocation mineLocation;
    static Direction minerDirection;

    Miner() throws GameActionException {
        minerDirection = getSpawnDirection();
    }
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runMiner() throws GameActionException {
        // Try to mine on squares around us.
        senseEnemyArchon();

        MapLocation me = rc.getLocation();
        ArrayList<Integer> seenLead = new ArrayList<Integer>();
        ArrayList<Integer> mineLocations = new ArrayList<Integer>();

        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(me, rc.getType().visionRadiusSquared)) {
            int n = locationToInt(loc);
            int leadAmount = rc.senseLead(loc);
            if (leadAmount > 1) {
                if (me.distanceSquaredTo(loc) <= 2) {
                    mineLocations.add(n);
                }
                seenLead.add(n);
            }
        }

        for (int n : mineLocations) {
            MapLocation mineLocation = intToLocation(n);
            while (rc.canMineGold(mineLocation)) {
                rc.mineGold(mineLocation);
            }
            int leadAmount = rc.senseLead(mineLocation);
            while (rc.canMineLead(mineLocation) && leadAmount > 1) {
                rc.mineLead(mineLocation);
                leadAmount -= 1;
            }
        }

        if (mineLocations.size() == 0) {
            if (seenLead.size() > 0) {
                int x = seenLead.get(0);
                moveToLocation(intToLocation(x));
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
