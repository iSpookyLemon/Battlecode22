package ezdubmachine;
import battlecode.common.*;

public class Laboratory extends RobotPlayer {
    Laboratory() throws GameActionException {

    }
    void runLaboratory() throws GameActionException {
        if (rc.canTransmute()) {
            rc.transmute();
        }
    }
}
