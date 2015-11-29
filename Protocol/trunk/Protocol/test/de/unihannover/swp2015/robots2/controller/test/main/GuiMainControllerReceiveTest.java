package de.unihannover.swp2015.robots2.controller.test.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class GuiMainControllerReceiveTest {
	private IGuiController guiController;
	private MqttController sender;

	@Before
	public void prepare() throws Exception {
		IMqttMessageHandler receiveHandler = new TestReceiveHandler();
		this.sender = new MqttController("tcp://localhost", "junit_sender",
				receiveHandler, Arrays.asList(new String[] {}));

		this.guiController = new GuiMainController();
		this.guiController.startMqtt("tcp://localhost");
	}

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
		this.guiController.getGame().observe(observer);

		sender.sendMessage("robot/type/1a2b3c4d", "virtual", false);
		Thread.sleep(100);

		assertNotEquals(null,
				this.guiController.getGame().getRobots().get("1a2b3c4d"));
		assertEquals("1a2b3c4d", observer.robot.getId());
		assertEquals(observer.robot, this.guiController.getGame().getRobots()
				.get("1a2b3c4d"));

		// Proof that a second message doesn't override model oder emit event.
		IRobot firstRobot = observer.robot;
		observer.robot = null;

		sender.sendMessage("robot/type/1a2b3c4d", "virtual", false);
		Thread.sleep(100);

		assertEquals(null, observer.robot);
		assertEquals(firstRobot,
				this.guiController.getGame().getRobots().get("1a2b3c4d"));
	}

	@Test
	public void testRobotPosition() throws Exception {
		class TestRobotModelObserver implements IModelObserver {
			public IRobot robot;
			public int count;
			public int countState;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_POSITION:
					this.count++;
					this.robot = (IRobot) event.getObject();
					break;
				case ROBOT_STATE:
					this.countState++;
					break;
				default:
					break;
				}
			}
		}

		// Add robot
		sender.sendMessage("robot/type/1a2b3c4d", "virtual", false);
		Thread.sleep(100);
		IRobot r = this.guiController.getGame().getRobots().get("1a2b3c4d");

		// Observe robot
		TestRobotModelObserver observer = new TestRobotModelObserver();
		r.observe(observer);

		// Update position
		sender.sendMessage("robot/position/1a2b3c4d", "1,2,w", false);
		Thread.sleep(100);

		assertEquals(1, r.getPosition().getX());
		assertEquals(2, r.getPosition().getY());
		assertEquals(Orientation.WEST, r.getPosition().getOrientation());
		assertEquals(r, observer.robot);
		assertEquals(1, observer.count);

		// Update progress
		sender.sendMessage("extension/2/robot/progress/1a2b3c4d", "250", false);
		Thread.sleep(100);

		assertEquals(250, r.getPosition().getProgress());
		assertEquals(r, observer.robot);
		assertEquals(2, observer.count);

		// Perform SET_POSITION
		sender.sendMessage("robot/setposition/1a2b3c4d", "2,4,e", false);
		Thread.sleep(100);

		assertEquals(2, r.getPosition().getX());
		assertEquals(4, r.getPosition().getY());
		assertEquals(Orientation.EAST, r.getPosition().getOrientation());
		assertEquals(0, r.getPosition().getProgress());
		assertEquals(true, r.isSetupState());
		assertEquals(r, observer.robot);
		assertEquals(3, observer.count);
		assertEquals(1, observer.countState);
	}

	@Test
	public void testRobotScore() throws Exception {
		class TestRobotModelObserver implements IModelObserver {
			public IRobot robot;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_SCORE:
					this.robot = (IRobot) event.getObject();
					break;
				default:
					break;
				}
			}
		}

		// Add robot
		sender.sendMessage("robot/type/1a2b3c4d", "virtual", false);
		Thread.sleep(100);
		IRobot r = this.guiController.getGame().getRobots().get("1a2b3c4d");

		// Observe robot
		TestRobotModelObserver observer = new TestRobotModelObserver();
		r.observe(observer);

		// Update score
		sender.sendMessage("robot/score/1a2b3c4d", "25", false);
		Thread.sleep(100);

		assertEquals(25, r.getScore());
		assertEquals(r, observer.robot);
	}

	@Test
	public void testGameParameters() throws Exception {
		class TestGameModelObserver implements IModelObserver {
			public int count;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case GAME_PARAMETER:
					this.count++;
					break;
				default:
					break;
				}
			}
		}
		
		TestGameModelObserver observer = new TestGameModelObserver();
		this.guiController.getGame().observe(observer);

		// Test
		sender.sendMessage("robot/virtualspeed", "1.87", false);
		sender.sendMessage("extension/2/robot/hesitationtime", "5025", false);
		Thread.sleep(100);

		assertEquals(1.87, this.guiController.getGame().getVRobotSpeed(), 0.002);
		assertEquals(5025, this.guiController.getGame().getHesitationTime());
		assertEquals(2, observer.count);
	}

	@Test
	public void testGameState() throws Exception {
		class TestGameModelObserver implements IModelObserver {
			public int count;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case GAME_STATE:
					this.count++;
					break;
				default:
					break;
				}
			}
		}
		
		TestGameModelObserver observer = new TestGameModelObserver();
		this.guiController.getGame().observe(observer);

		// Test
		sender.sendMessage("control/state", "running", false);
		Thread.sleep(100);

		assertEquals(true, this.guiController.getGame().isRunning());
		assertEquals(1, observer.count);
		

		sender.sendMessage("control/state", "stopped", false);
		Thread.sleep(100);

		assertEquals(false, this.guiController.getGame().isRunning());
		assertEquals(2, observer.count);
	}

	@Test
	public void testWalls() throws Exception {
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
		
		IStage stage = this.guiController.getGame().getStage();
		
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


}
