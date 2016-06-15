package dancedads;
import robocode.*;
import java.util.Random;
import java.lang.Math;

import static robocode.util.Utils.normalRelativeAngleDegrees;


import robocode.util.*;
import java.awt.geom.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * DanceDads - a robot by (your name here)
 */
public class DanceDads extends AdvancedRobot
{
	/**
	 * run: DanceDads's default behavior
	 */
	boolean movingForward;
	
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		Random rand = new Random();
		// Robot main loop
		int its = 0;
		double field_height = getBattleFieldHeight();
		double field_width = getBattleFieldWidth();
		
		while (true) {
			// Tell the game we will want to move ahead 40000 -- some large number
			setAhead(40000);
			movingForward = true;
			// Tell the game we will want to turn right 90
			setTurnRight(90);
			// At this point, we have indicated to the game that *when we do something*,
			// we will want to move ahead and turn right.  That's what "set" means.
			// It is important to realize we have not done anything yet!
			// In order to actually move, we'll want to call a method that
			// takes real time, such as waitFor.
			// waitFor actually starts the action -- we start moving and turning.
			// It will not return until we have finished turning.
			waitFor(new TurnCompleteCondition(this));
			// Note:  We are still moving ahead now, but the turn is complete.
			// Now we'll turn the other way...
			setTurnLeft(180);
			// ... and wait for the turn to finish ...
			waitFor(new TurnCompleteCondition(this));
			// ... then the other way ...
			setTurnRight(180);
			// .. and wait for that turn to finish.
			waitFor(new TurnCompleteCondition(this));
			// then back to the top to do it all again
		}
		/*
		while(true) {
			double x = getX();
			double y = getY();
			double heading = getHeading();
			double mov_magnitude = rand.nextInt(300) + 1;
			its++;
			
			if (rand.nextDouble() > 0.5 ) {
				if (!(willCollide(0 - mov_magnitude)) ) {
					back(mov_magnitude);
				}
				else {
					continue;
				}
			}
			else {
				if (!(willCollide(mov_magnitude)) ) {
					ahead(mov_magnitude);
				}
			}
						if (!(willCollide(mov_magnitude)) ) {
				ahead(mov_magnitude);
			}
			
			if (rand.nextDouble() > 0.5) {
				turnRight(rand.nextInt(50));	
			}
			else {
			
				turnLeft(rand.nextInt(50));
			}
			
		}
		*/
	}
		

	// computes whether moving ahead at the given magnitude will cause collision
	public boolean willCollide(double magnitude) {
		// get destination coordinates
		double cur_x = getX();
		double cur_y = getY();
		double heading = getHeading();
		double dest_x = magnitude * Math.cos(heading) + cur_x;
		double dest_y = magnitude * Math.sin(heading) + cur_y;
		
		double field_height = getBattleFieldHeight();
		double field_width = getBattleFieldWidth();

		if ((dest_x > field_width ) || (dest_x < 0)) {
			return false;
		}
		if ((dest_y > field_height ) || (dest_y < 0)) {
			return true;
		}

		return false;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	public void onHitWall(HitWallEvent e) {
		// Bounce off!
		reverseDirection();
	}

	/**
	 * reverseDirection:  Switch from ahead to back & vice versa
	 */
	public void reverseDirection() {
		if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}
	
	/**
	 * onHitRobot:  Back up!
	 */
	public void onHitRobot(HitRobotEvent e) {
		// If we're moving the other robot, reverse!
		if (e.isMyFault()) {
			reverseDirection();
		}
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
}
