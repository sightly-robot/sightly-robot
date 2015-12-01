package de.unihannover.swp2015.robots2.controller.test.main;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

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
	 * Test sending messages to control/state to start and stop the game.
	 */
	@Test
	public void testStartStop() throws Exception {
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
		this.receiver.deleteRetainedMessage("control/state");
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
		this.receiver.deleteRetainedMessage("map/walls");

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
		this.receiver.deleteRetainedMessage("extension/2/map/setgrowrate");

	}

	/**
	 * Test sending new game parameters (virtualspeed, hesitationtime)
	 */
	@Test
	public void testGameParameters() throws Exception {
		this.guiController.sendGameParameters(2.57f, 5000);
		Thread.sleep(100);

		assertEquals(2.57f, Float.parseFloat(this.receiveHandler
				.getValue("robot/virtualspeed")), 0.005);

		assertEquals(5000, Integer.parseInt(this.receiveHandler
				.getValue("extension/2/robot/hesitationtime")));

		// Check for any other mqtt topics
		assertEquals(2, this.receiveHandler.getKeysSorted().length);

		// Cleanup retained message
		this.receiver.deleteRetainedMessage("robot/virtualspeed");
		this.receiver.deleteRetainedMessage("extension/2/robot/hesitationtime");

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
		this.receiver.deleteRetainedMessage("extension/2/map/startpositions");
	}

	/**
	 * Start the Test MqttController for receiving and its TestMessageHandler.
	 */
	private void restartReceiver() throws Exception {
		this.receiveHandler = new TestReceiveHandler();
		this.receiver = new MqttController("tcp://localhost", "junit_receiver",
				receiveHandler, Arrays.asList(new String[] { "#" }));
	}

}
