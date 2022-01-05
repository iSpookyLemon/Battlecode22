package newplayer;
import battlecode.common.*;

public class Builder extends RobotPlayer{
    Builder() throws GameActionException {

    }
    void runBuilder() throws GameActionException {
        Direction dir = directions[rng.nextInt(directions.length)];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }
    }
}
