package de.unihannover.swp2015.robots2.model.test.implementation;

import static org.junit.Assert.*;
import org.junit.Test;

import de.unihannover.swp2015.robots2.model.implementation.Game;

/**
 * Simple JUnitTest for the game-model
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class GameTest {
	
	@Test
	public void testRunning() {
		Game game = new Game();
		
		assertEquals(false, game.isRunning());
		
		game.setRunning(true);
		assertEquals(true, game.isRunning());
		
		game.setRunning(false);
		assertEquals(false, game.isRunning());
	}
	
	@Test
	public void testVRobotSpeed() {
		Game game = new Game();
		
		game.setVRobotSpeed(0.3f);
		assertEquals(0.3f, game.getVRobotSpeed(), 0.001);
		
		game.setVRobotSpeed(1f);
		assertEquals(1f, game.getVRobotSpeed(), 0.001);
		
		game.setVRobotSpeed(1.7f);
		assertEquals(1.7f, game.getVRobotSpeed(), 0.001);
		
		game.setVRobotSpeed(5.2f);
		assertEquals(5.2f, game.getVRobotSpeed(), 0.001);
	}
	
	@Test
	public void testHesitationTime() {
		Game game = new Game();
		
		game.setHesitationTime(1);
		assertEquals(1, game.getHesitationTime());
		
		game.setHesitationTime(4);
		assertEquals(4, game.getHesitationTime());
	}

}
