package newplayer;
import battlecode.common.*;

public class Sage extends RobotPlayer {

    static MapLocation enemyArchonLocation = null;
    static Direction sageDirection;

    Sage() throws GameActionException {

    }
    void runSage() throws GameActionException {

        if (rc.readSharedArray(1) == 1) {
            int n = rc.readSharedArray(0);
            enemyArchonLocation = intToLocation(n);
        }
        
        if (rc.isMovementReady()) {
            if (enemyArchonLocation != null) {
                moveToLocation(enemyArchonLocation);
        	} else {
                if (rc.onTheMap(rc.getLocation().add(sageDirection)) == false) {
                    sageDirection = rebound(sageDirection);
                }
                moveInDirection(sageDirection);
            }
        }

        if (rc.canEnvision(AnomalyType.CHARGE)) {
            rc.envision(AnomalyType.CHARGE);
        }
    }
}
