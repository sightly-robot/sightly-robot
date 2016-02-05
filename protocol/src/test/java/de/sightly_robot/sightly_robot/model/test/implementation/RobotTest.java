package de.sightly_robot.sightly_robot.model.test.implementation;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import de.sightly_robot.sightly_robot.model.implementation.Robot;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * Simple JUnitTest for the robot-model
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotTest {
	
	@Test
	public void testInit() {
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		Robot robot = new Robot(uuid, true, false);
		
		assertEquals(uuid, robot.getId());
		assertEquals(true, robot.isHardwareRobot());
		assertEquals(false, robot.isMyself());
		assertEquals(Orientation.NORTH, robot.getPosition().getOrientation());
	}
	
	@Test
	public void testScore() {
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		Robot robot = new Robot(uuid, true, false);
		
		assertEquals(0, robot.getScore());
		
		robot.setScore(13);
		assertEquals(13, robot.getScore());
		
		robot.setScore(7);
		assertEquals(7, robot.getScore());
		
		robot.addScore(3);
		assertEquals(10, robot.getScore());
		
		robot.addScore(-5);
		assertEquals(5, robot.getScore());
	}
	

}
