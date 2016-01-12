package de.unihannover.swp2015.robots2.controller.test.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.main.ServerMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

public class ServerMainControllerReceiveTest {

	private TestReceiveHandler receiveHandler;
	private IMqttController sender;
	private IServerController serverController;

	private String robotId = UUID.randomUUID().toString().substring(0, 8);

	/**
	 * Prepare the test-environment
	 */
	@Before
	public void prepare() throws Exception {

		this.receiveHandler = new TestReceiveHandler();

		// Start Sender
		this.sender = new MqttController(
				"junit_receiver" + UUID.randomUUID().toString(), receiveHandler,
				Arrays.asList(new String[] { "#" }));
		this.sender.connect("tcp://localhost");

		// Start GuiMainController
		this.serverController = new ServerMainController();
		serverController.startMqtt("tcp://localhost");
	}

	/**
	 * Test receiving of a new robot
	 */
	@Test
	public void testNewRobot() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public IRobot robot;

			@Override
			public void onModelUpdate(IEvent event) {
				if (event.getType() == UpdateType.ROBOT_ADD)
					this.robot = (IRobot) event.getObject();
			}
		}

		TestStageModelObserver observer = new TestStageModelObserver();
		this.serverController.getGame().observe(observer);

		// Test
		this.sender.sendMessage("robot/type/" + this.robotId, "virtual", true);
		Thread.sleep(100);

		assertNotEquals(null,
				this.serverController.getGame().getRobots().get(this.robotId));
		assertEquals(this.robotId, observer.robot.getId());
		assertEquals(observer.robot,
				this.serverController.getGame().getRobots().get(this.robotId));

		// Proof that a second message doesn't override model oder emit event.
		IRobot firstRobot = observer.robot;
		observer.robot = null;

		sender.sendMessage("robot/type/" + this.robotId, "virtual", false);
		Thread.sleep(100);

		assertEquals(null, observer.robot);
		assertEquals(firstRobot,
				this.serverController.getGame().getRobots().get(this.robotId));
	}

	/**
	 * Test receiving of new walls
	 */
	@Test
	public void testNewWalls() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public int countWallUpdate;
			public int countSizeUpdate;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case STAGE_WALL:
					this.countWallUpdate++;
					break;
				case STAGE_SIZE:
					this.countSizeUpdate++;
					break;
				default:
					break;
				}
			}
		}

		IStage stage = this.serverController.getGame().getStage();

		TestStageModelObserver observer = new TestStageModelObserver();
		stage.observe(observer);

		// Test normal walls
		sender.sendMessage("map/walls", "2,3,en,enw,w,,ew,es", false);
		Thread.sleep(100);

		assertEquals(2, stage.getWidth());
		assertEquals(3, stage.getHeight());
		assertEquals(1, observer.countSizeUpdate);

		assertEquals(1, observer.countWallUpdate);
		assertEquals(true, stage.getField(0, 0).isWall(Orientation.EAST));
		assertEquals(false, stage.getField(0, 0).isWall(Orientation.SOUTH));
		assertEquals(true, stage.getField(1, 0).isWall(Orientation.NORTH));
		assertEquals(false, stage.getField(0, 1).isWall(Orientation.NORTH));
		assertEquals(true, stage.getField(0, 2).isWall(Orientation.EAST));
		assertEquals(false, stage.getField(1, 2).isWall(Orientation.WEST));

		// Proof border plausibility check
		assertEquals(true, stage.getField(0, 0).isWall(Orientation.WEST));
		assertEquals(true, stage.getField(0, 2).isWall(Orientation.SOUTH));
		assertEquals(true, stage.getField(1, 1).isWall(Orientation.EAST));

		// Check negative size change
		sender.sendMessage("map/walls", "1,2,ensw,ensw", false);
		Thread.sleep(100);

		assertEquals(1, stage.getWidth());
		assertEquals(2, stage.getHeight());
		assertEquals(2, observer.countSizeUpdate);
		assertEquals(2, observer.countWallUpdate);
		assertEquals(true, stage.getField(0, 1).isWall(Orientation.NORTH));

		try {
			stage.getField(0, 2);
			fail();
		} catch (IndexOutOfBoundsException e) {

		}
		try {
			stage.getField(1, 1);
			fail();
		} catch (IndexOutOfBoundsException e) {

		}

		// Test normal override
		sender.sendMessage("map/walls", "1,2,enw,esw", false);
		Thread.sleep(100);

		assertEquals(2, observer.countSizeUpdate);
		assertEquals(3, observer.countWallUpdate);
		assertEquals(1, stage.getWidth());
		assertEquals(2, stage.getHeight());
		assertEquals(false, stage.getField(0, 0).isWall(Orientation.SOUTH));
		assertEquals(false, stage.getField(0, 1).isWall(Orientation.NORTH));

		// Test discarding of wrong length message
		sender.sendMessage("map/walls", "1,3,ensw,ensw", false);
		Thread.sleep(100);

		assertEquals(2, observer.countSizeUpdate);
		assertEquals(3, observer.countWallUpdate);
		assertEquals(1, stage.getWidth());
		assertEquals(2, stage.getHeight());
		assertEquals(false, stage.getField(0, 0).isWall(Orientation.SOUTH));
		assertEquals(false, stage.getField(0, 1).isWall(Orientation.NORTH));
	}

	/**
	 * Test receiving of initial food
	 */
	@Test
	public void testInitFood() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public int count;
			public IField field;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case FIELD_FOOD:
					this.count++;
					this.field = (IField) event.getObject();
					break;
				default:
					break;
				}
			}
		}

		IStage stage = this.serverController.getGame().getStage();

		TestStageModelObserver observer = new TestStageModelObserver();
		stage.observe(observer);

		// Init walls & observe fields
		sender.sendMessage("map/walls", "2,3,,,,,,", false);
		Thread.sleep(100);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				stage.getField(i, j).observe(observer);
			}
		}

		// Test full food
		sender.sendMessage("extension/2/map/setfood", "2,3,1,10,5,7,0,10", false);
		Thread.sleep(100);

		assertEquals(6, observer.count);
		assertEquals(1, stage.getField(0, 0).getFood());
		assertEquals(10, stage.getField(1, 0).getFood());
		assertEquals(5, stage.getField(0, 1).getFood());
		assertEquals(10, stage.getField(1, 2).getFood());
	}

	/**
	 * Test receiving of initial growingrates
	 */
	@Test
	public void testInitGrowingrate() throws Exception {
		IStage stage = this.serverController.getGame().getStage();
		
		this.sender.sendMessage("extension/2/map/setgrowrate", "2,2,4,1,3,12", true);
		Thread.sleep(100);
		
		assertEquals(4, stage.getField(0, 0).getGrowingRate());
		assertEquals(1, stage.getField(1, 0).getGrowingRate());
		assertEquals(3, stage.getField(0, 1).getGrowingRate());
		assertEquals(12, stage.getField(1, 1).getGrowingRate());
	}

	/**
	 * Test receiving of virtual speed
	 */
	@Test
	public void testVirtualspeed() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public float virtualspeed;

			@Override
			public void onModelUpdate(IEvent event) {
				if (event.getType() == UpdateType.GAME_PARAMETER) {
					IGame game = (IGame) event.getObject();
					this.virtualspeed = game.getVRobotSpeed();
				}
			}
		}

		TestStageModelObserver observer = new TestStageModelObserver();
		this.serverController.getGame().observe(observer);

		float virtualSpeed = 1.321f;

		// Test
		this.sender.sendMessage("robot/virtualspeed",
				String.valueOf(virtualSpeed), true);
		Thread.sleep(100);

		assertEquals(virtualSpeed, observer.virtualspeed, 0.01);

		// Cleanup
		this.sender.sendMessage("robot/virtualspeed", null, true);
	}

	/**
	 * Test receiving of hesitation time
	 */
	@Test
	public void testHesitationtime() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public int hesitationTime;

			@Override
			public void onModelUpdate(IEvent event) {
				if (event.getType() == UpdateType.GAME_PARAMETER) {
					IGame game = (IGame) event.getObject();
					this.hesitationTime = game.getHesitationTime();
				}
			}
		}

		TestStageModelObserver observer = new TestStageModelObserver();
		this.serverController.getGame().observe(observer);

		int hesitationTime = 323;

		// Test
		this.sender.sendMessage("extension/2/robot/hesitationtime",
				String.valueOf(hesitationTime), true);
		Thread.sleep(100);

		assertEquals(hesitationTime, observer.hesitationTime);

		// Cleanup
		this.sender.sendMessage("extension/2/robot/hesitationtime", null, true);
	}
}
