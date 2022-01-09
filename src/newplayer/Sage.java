package newplayer;
import battlecode.common.*;

public class Sage extends RobotPlayer {

    static MapLocation enemyArchonLocation = null;
    static Direction sageDirection;

    Sage() throws GameActionException {
    	sageDirection = getSpawnDirection();
    }
    void runSage() throws GameActionException {
    	
    	for (int i = 0; i < 4; i++) {
            enemyArchonLocation = getEnemyArchon(i);
            if (enemyArchonLocation != null) {
                break;
            }
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
