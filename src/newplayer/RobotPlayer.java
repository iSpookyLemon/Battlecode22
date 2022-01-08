//Testing out git
package newplayer;

import battlecode.common.*;
import java.util.Random;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {
    static RobotController rc;

    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;

    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    static final Random rng = new Random(6147);

    /** Array containing all the possible movement directions. */
    static final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc  The RobotController object. You use it to perform actions from this robot, and to get
     *            information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // Hello world! Standard output is very useful for debugging.
        // Everything you say here will be directly viewable in your terminal when you run a match!
        //System.out.println("I'm a " + rc.getType() + " and I just got created! I have health " + rc.getHealth());
        RobotPlayer.rc = rc;

        Archon archon = null;
        Miner miner = null;
        Soldier soldier = null;
        Laboratory laboratory = null;
        Watchtower watchtower = null;
        Builder builder = null;
        Sage sage = null;

        switch (rc.getType()) {
            case ARCHON:     archon = new Archon(); break;
            case MINER:      miner = new Miner(); break;
            case SOLDIER:    soldier = new Soldier(); break;
            case LABORATORY: laboratory = new Laboratory(); break;
            case WATCHTOWER: watchtower = new Watchtower(); break;// You might want to give them a try!
            case BUILDER:    builder = new Builder(); break;
            case SAGE:       sage = new Sage(); break;
        }

        // You can also use indicators to save debug notes in replays.
        rc.setIndicatorString("Hello world!");

        while (true) {
            // This code runs during the entire lifespan of the robot, which is why it is in an infinite
            // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
            // loop, we call Clock.yield(), signifying that we've done everything we want to do.

            turnCount += 1;  // We have now been alive for one more turn!

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
            try {
                // The same run() function is called for every robot on your team, even if they are
                // different types. Here, we separate the control depending on the RobotType, so we can
                // use different strategies on different robots. If you wish, you are free to rewrite
                // this into a different control structure!
                switch (rc.getType()) {
                    case ARCHON:     archon.runArchon(); break;
                    case MINER:      miner.runMiner(); break;
                    case SOLDIER:    soldier.runSoldier(); break;
                    case LABORATORY: laboratory.runLaboratory(); break;
                    case WATCHTOWER: watchtower.runWatchtower(); break;
                    case BUILDER:    builder.runBuilder(); break;
                    case SAGE:       sage.runSage(); break;
                }
            } catch (GameActionException e) {
                // Oh no! It looks like we did something illegal in the Battlecode world. You should
                // handle GameActionExceptions judiciously, in case unexpected events occur in the game
                // world. Remember, uncaught exceptions cause your robot to explode!
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } catch (Exception e) {
                // Oh no! It looks like our code tried to do something bad. This isn't a
                // GameActionException, so it's more likely to be a bug in our code.
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } finally {
                // Signify we've done everything we want to do, thereby ending our turn.
                // This will make our code wait until the next turn, and then perform this loop again.
                Clock.yield();
            }
            // End of loop: go back to the top. Clock.yield() has ended, so it's time for another turn!
        }

        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }

    static MapLocation getParentArchonLocation() throws GameActionException {
        Team myTeam = rc.getTeam();
        RobotInfo[] robots = rc.senseNearbyRobots(2, myTeam);
        MapLocation archonLocation = null;
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.ARCHON) {
                archonLocation = robot.getLocation();
            }
        }
        return archonLocation;
    }

    static int locationToInt(MapLocation loc) {
        return loc.x + loc.y * 60;
    }

    static MapLocation intToLocation(int n) {
        return new MapLocation(n % 60, n / 60);
    }

    static void moveToLocation(MapLocation loc) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(loc);
        moveInDirection(dir);
    }
    
    static void moveInDirection(Direction dir) throws GameActionException {
        Direction leftDirection = dir;
        Direction rightDirection = dir;
        for (int i = 0; i < 5; i++) {
            if (rc.canMove(leftDirection)) {
                rc.move(leftDirection);
            }
            if (rc.canMove(rightDirection)) {
                rc.move(rightDirection);
            }
            leftDirection = leftDirection.rotateLeft();
            rightDirection = rightDirection.rotateRight();
        }
    }

    static Direction getSpawnDirection() throws GameActionException {
        Team myTeam = rc.getTeam();
        RobotInfo[] robots = rc.senseNearbyRobots(2, myTeam);
        Direction dir = null;
        for (RobotInfo robot : robots) {
            if (robot.type == RobotType.ARCHON) {
                MapLocation archonLocation = robot.getLocation();
                dir = archonLocation.directionTo(rc.getLocation());
            }
        }
        return dir;
    }

    static Direction rebound(Direction dir) throws GameActionException {
        Direction[] cardinalDirections = Direction.cardinalDirections();
        boolean isCardinalDirection = false;
        for (Direction d : cardinalDirections) {
            if (dir == d) {
                isCardinalDirection = true;
            }
        }
        if (isCardinalDirection) {
            dir = dir.opposite().rotateRight();
        } else {
            if (rc.onTheMap(rc.getLocation().add(dir.rotateRight().rotateRight()))) {
                dir = dir.rotateRight().rotateRight();
            } else if (rc.onTheMap(rc.getLocation().add(dir.rotateLeft().rotateLeft()))) {
                dir = dir.rotateLeft().rotateLeft();
            } else {
                dir = dir.opposite();
            }
        }
        return dir;
    }

    static void senseEnemyArchon() throws GameActionException {
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        for (RobotInfo robot : enemies) {
            if (robot.getType() == RobotType.ARCHON) {
                int n = locationToInt(robot.getLocation());
                boolean inArray = false;
                int start = -1;
                for (int x = 0; x < 4; x ++) {
                    int value = rc.readSharedArray(x);
                    if (value == n * 2 + 1) {
                        inArray = true;
                        break;
                    }
                    if (value == 0) {
                        start = x;
                        break;
                    }
                }
                if (inArray == false && start >= 0) {
                    rc.writeSharedArray(start, n * 2 + 1);
                }
            }
        }
    }

    static MapLocation getEnemyArchon(int i) throws GameActionException {
        MapLocation enemyArchonLocation = null;
        if (rc.readSharedArray(i) % 2 == 1) {
            int n = rc.readSharedArray(0);
            enemyArchonLocation = intToLocation((n - 1)/2);
        }
        return enemyArchonLocation;
    }

    static void removeEnemyArchon(MapLocation loc) throws GameActionException {
        for (int x = 0; x < 4; x ++) {
            int value = rc.readSharedArray(x);
            if ((value - 1) / 2 == locationToInt(loc)) {
                rc.writeSharedArray(x, 0);
                break;
            }
        }
    }
}