package newplayer;
import battlecode.common.*;

public class Soldier extends RobotPlayer{

    static MapLocation enemyArchonLocation = null;

    Soldier() throws GameActionException {

    }
    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    void runSoldier() throws GameActionException {

        if (rc.readSharedArray(1) == 1) {
            int n = rc.readSharedArray(0);
            enemyArchonLocation = intToLocation(n);
        }

        // Try to attack someone
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }

        // Also try to move randomly.
        if (enemyArchonLocation != null) {
            moveToLocation(enemyArchonLocation);
        }
    }
}
