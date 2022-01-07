package newplayer;
import battlecode.common.*;

public class Sage extends RobotPlayer {

    static MapLocation enemyArchonLocation = null;

    Sage() throws GameActionException {

    }
    void runSage() throws GameActionException {

        if (rc.readSharedArray(1) == 1) {
            int n = rc.readSharedArray(0);
            enemyArchonLocation = intToLocation(n);
        }

        if (rc.canEnvision(AnomalyType.CHARGE)) {
            rc.envision(AnomalyType.CHARGE);
        }

        if (enemyArchonLocation != null) {
            moveToLocation(enemyArchonLocation);
        }
    }
}
