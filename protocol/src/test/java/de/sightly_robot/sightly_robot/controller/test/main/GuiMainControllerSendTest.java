package de.sightly_robot.sightly_robot.controller.test.main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.sightly_robot.sightly_robot.controller.interfaces.IGuiController;
import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import de.sightly_robot.sightly_robot.controller.mqtt.IMqttController;
import de.sightly_robot.sightly_robot.controller.mqtt.MqttController;
import de.sightly_robot.sightly_robot.model.implementation.Position;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

public class GuiMainControllerSendTest {
	private TestReceiveHandler receiveHandler;
	private IMqttController receiver;
	private IGuiController guiController;

	@Before
	public void prepare() throws Exception {
		// Start Receiver
		this.restartReceiver();
		// Start GuiMainController
		this.guiController = new GuiMainController();
		guiController.startMqtt("tcp://localhost");
	}

	/**
	 * Test sending messages to control/state to start and stop the game. And
	 * the one for resets.
	 */
	@Test
	public void testStartStopReset() throws Exception {
		// Test
		this.guiController.startGame();
		Thread.sleep(100);
		assertEquals("running", receiveHandler.getValue("control/state"));

		this.guiController.stopGame();
		Thread.sleep(100);
		assertEquals("stopped", receiveHandler.getValue("control/state"));

		// Check for any other mqtt topics
		assertEquals(1, this.receiveHandler.getKeysSorted().length);

		// Test if message is retained and therefore visible for new connecting
		// client
		this.restartReceiver();
		Thread.sleep(100);
		assertEquals("stopped", receiveHandler.getValue("control/state"));

		// Cleanup retained message
		this.receiver.sendMessage("control/state", null, true);

		// Test reset game
		this.guiController.resetGame();
		Thread.sleep(100);
		assertNotNull(receiveHandler.getValue("extension/2/control/reset"));
	}

	/**
	 * Test sending new walls.
	 */
	@Test
	public void testWalls() throws Exception {
		// Build walls
		List<List<Set<Orientation>>> walls = new ArrayList<List<Set<Orientation>>>();
		walls.add(new ArrayList<Set<Orientation>>());
		walls.add(new ArrayList<Set<Orientation>>());
		walls.add(new ArrayList<Set<Orientation>>());
		walls.get(0).add(
				new HashSet<Orientation>(Arrays
						.asList(new Orientation[] { Orientation.NORTH,
								Orientation.WEST, Orientation.EAST })));
		walls.get(0).add(
				new HashSet<Orientation>(Arrays
						.asList(new Orientation[] { Orientation.NORTH,
								Orientation.EAST, Orientation.WEST })));
		walls.get(1).add(
				new HashSet<Orientation>(Arrays
						.asList(new Orientation[] { Orientation.WEST })));
		walls.get(1).add(
				new HashSet<Orientation>(Arrays
						.asList(new Orientation[] { Orientation.EAST })));
		walls.get(2).add(
				new HashSet<Orientation>(Arrays
						.asList(new Orientation[] { Orientation.SOUTH,
								Orientation.WEST, Orientation.EAST })));
		walls.get(2).add(
				new HashSet<Orientation>(Arrays.asList(new Orientation[] {
						Orientation.SOUTH, Orientation.EAST })));

		// Test
		this.guiController.sendWalls(walls);
		Thread.sleep(100);

		String[] expectedWalls = { "2", "3", "enw", "enw", "w", "e", "esw",
				"es" }; // In alphabetical order
		String[] receivedWalls = this.receiveHandler.getValue("map/walls")
				.split(",");

		assertEquals(expectedWalls.length, receivedWalls.length);
		for (int i = 0; i < expectedWalls.length; i++) {
			char[] receivedSorted = receivedWalls[i].toCharArray();
			Arrays.sort(receivedSorted);
			assertEquals(expectedWalls[i], new String(receivedSorted));
		}

		// Check for any other mqtt topics
		assertEquals(1, this.receiveHandler.getKeysSorted().length);

		// Cleanup retained message
		this.receiver.sendMessage("map/walls", null, true);

	}

	/**
	 * Test sending new food state to server.
	 */
	@Test
	public void testFood() throws Exception {
		// Build food
		List<List<Integer>> food = new ArrayList<List<Integer>>();
		food.add(new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 5 })));
		food.add(new ArrayList<Integer>(Arrays.asList(new Integer[] { 10, 5 })));
		food.add(new ArrayList<Integer>(Arrays.asList(new Integer[] { 2, 9 })));

		// Test
		this.guiController.sendFood(food);
		Thread.sleep(100);

		String expectedFood = "2,3,0,5,10,5,2,9";

		assertEquals(expectedFood,
				this.receiveHandler.getValue("extension/2/map/setfood"));

		// Check for any other mqtt topics
		assertEquals(1, this.receiveHandler.getKeysSorted().length);

		// Check that it is NOT retained
		this.restartReceiver();
		Thread.sleep(100);
		assertEquals(null,
				this.receiveHandler.getValue("extension/2/map/setfood"));
	}

	/**
	 * Test sending new growing rates to server.
	 */
	@Test
	public void testGrowingRates() throws Exception {
		// Build food
		List<List<Integer>> growingRates = new ArrayList<List<Integer>>();
		growingRates.add(new ArrayList<Integer>(Arrays.asList(new Integer[] {
				5000, 3000 })));
		growingRates.add(new ArrayList<Integer>(Arrays.asList(new Integer[] {
				3000, 1000 })));
		growingRates.add(new ArrayList<Integer>(Arrays.asList(new Integer[] {
				1000, 500 })));

		// Test
		this.guiController.sendGrowingRates(growingRates);
		Thread.sleep(100);

		String expectedFood = "2,3,5000,3000,3000,1000,1000,500";

		assertEquals(expectedFood,
				this.receiveHandler.getValue("extension/2/map/setgrowrate"));

		// Check for any other mqtt topics
		assertEquals(1, this.receiveHandler.getKeysSorted().length);

		// Cleanup retained message
		this.receiver.sendMessage("extension/2/map/setgrowrate", null, true);

	}

	/**
	 * Test sending new game parameters (virtual speed, hesitation time)
	 */
	@Test
	public void testGameParameters() throws Exception {
		this.guiController.sendVRobotSpeed(2.57f, 5.025f);
		Thread.sleep(100);

		String[] parts = this.receiveHandler.getValue("robot/virtualspeed").split(",");
		assertEquals(2, parts.length);
		assertEquals(2.57f, Float.parseFloat(parts[0]), 0.005);
		assertEquals(5.025, Float.parseFloat(parts[1]),0.005);

		// Check for any other mqtt topics
		assertEquals(2, this.receiveHandler.getKeysSorted().length);

		// Cleanup retained message
		this.receiver.sendMessage("robot/virtualspeed", null, true);
		this.receiver.sendMessage("extension/2/robot/hesitationtime", null,
				true);

	}

	/**
	 * Test sending new start positions
	 */
	@Test
	public void testStartPositions() throws Exception {
		// Build start positions
		List<IPosition> startpositions = new ArrayList<IPosition>();
		startpositions.add(new Position(0, 0, Orientation.WEST));
		startpositions.add(new Position(2, 5, Orientation.NORTH));
		startpositions.add(new Position(6, 3, Orientation.EAST));
		startpositions.add(new Position(0, 8, Orientation.SOUTH));

		// Test
		this.guiController.sendStartPositions(startpositions);
		Thread.sleep(100);

		String expectedString = "0,0,w,2,5,n,6,3,e,0,8,s";
		assertEquals(expectedString,
				this.receiveHandler.getValue("extension/2/map/startpositions"));

		// Clean up
		this.receiver.sendMessage("extension/2/map/startpositions", null, true);
	}

	/**
	 * Test disable robot, delete robot.
	 */
	@Test
	public void testUtilities() throws Exception {
		// Test disable robot
		this.guiController.disableRobot("1a2b3c4d");
		Thread.sleep(100);
		assertEquals("y",
				receiveHandler.getValue("extension/2/robot/state/1a2b3c4d"));

		// Test delete robot
		this.guiController.deleteRobot("1a2b3c4d");
		Thread.sleep(100);
		assertEquals("", receiveHandler.getValue("robot/type/1a2b3c4d"));

		// No clean up needed, as controller should automatically clean up
		// but we will check if there are clean up message from the controller:
		assertEquals("", receiveHandler.getValue("robot/position/1a2b3c4d"));
		assertEquals("",
				receiveHandler.getValue("extension/2/robot/state/1a2b3c4d"));

	}

	/**
	 * Start the Test MqttController for receiving and its TestMessageHandler.
	 */
	private void restartReceiver() throws Exception {
		this.receiveHandler = new TestReceiveHandler();
		this.receiver = new MqttController("junit_receiver"
				+ UUID.randomUUID().toString(), receiveHandler,
				Arrays.asList(new String[] { "#" }));
		this.receiver.connect("tcp://localhost");
	}

}
