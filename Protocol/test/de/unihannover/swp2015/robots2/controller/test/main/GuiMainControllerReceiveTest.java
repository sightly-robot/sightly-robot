package de.unihannover.swp2015.robots2.controller.test.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class GuiMainControllerReceiveTest {
	private IGuiController guiController;
	private MqttController sender;

	@Before
	public void prepare() throws Exception {
		IMqttMessageHandler receiveHandler = new TestReceiveHandler();
		this.sender = new MqttController("junit_sender_"
				+ UUID.randomUUID().toString(), receiveHandler,
				Arrays.asList(new String[] {}));
		this.sender.connect("tcp://localhost");

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
			public int countProgress;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_POSITION:
					this.count++;
					this.robot = (IRobot) event.getObject();
					break;
				case ROBOT_PROGRESS:
					this.countProgress++;
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
		Thread.sleep(200);
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
		assertEquals(1, observer.count); // Position event shoud fire
		assertEquals(1, observer.countProgress); // and progress also

		// Update progress
		sender.sendMessage("extension/2/robot/progress/1a2b3c4d", "250", false);
		Thread.sleep(100);

		assertEquals(250, r.getPosition().getProgress());
		assertEquals(r, observer.robot);
		assertEquals(1, observer.count); // No position event fired
		assertEquals(2, observer.countProgress); // but progress event fired

		// Perform SET_POSITION which now should not affect the position or
		// state of robot
		sender.sendMessage("robot/setposition/1a2b3c4d", "2,4,e", false);
		Thread.sleep(100);

		assertEquals(1, r.getPosition().getX());
		assertEquals(2, r.getPosition().getY());
		assertEquals(RobotState.CONNECTED, r.getState());
		assertEquals(1, observer.count);
		assertEquals(0, observer.countState);

		// Perform position again, which should also reset progress
		sender.sendMessage("robot/position/1a2b3c4d", "0,2,w", false);
		Thread.sleep(100);

		assertEquals(0, r.getPosition().getProgress());
		assertEquals(3, observer.countProgress);

	}

	@Test
	public void testRobotState() throws Exception {
		class TestRobotModelObserver implements IModelObserver {
			public IRobot robot;
			public int count;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_STATE:
					this.count++;
					this.robot = (IRobot) event.getObject();
					break;
				default:
					break;
				}
			}
		}

		// Add robot
		sender.sendMessage("robot/type/1a2b3c4d", "virtual", false);
		Thread.sleep(200);
		IRobot r = this.guiController.getGame().getRobots().get("1a2b3c4d");

		// Observe robot
		TestRobotModelObserver observer = new TestRobotModelObserver();
		r.observe(observer);

		// The robot should be in connected state initially
		assertEquals(RobotState.CONNECTED, r.getState());
		assertEquals(0, observer.count);

		// Now it encounters a robotics error (a bit strange, I know)
		sender.sendMessage("extension/2/robot/state/1a2b3c4d",
				RobotState.ROBOTICS_ERROR.toString(), false);
		Thread.sleep(100);

		assertEquals(RobotState.ROBOTICS_ERROR, r.getState());
		assertEquals(1, observer.count);
		assertEquals(r, observer.robot);

		// But another GUI assigns a new position and the robot goes to
		// setup state
		sender.sendMessage("extension/2/robot/state/1a2b3c4d",
				RobotState.SETUPSTATE.toString(), false);
		Thread.sleep(100);

		assertEquals(RobotState.SETUPSTATE, r.getState());
		assertEquals(2, observer.count);
		
		// And finally the robot is ready
		sender.sendMessage("extension/2/robot/state/1a2b3c4d",
				RobotState.ENABLED.toString(), false);
		Thread.sleep(100);

		assertEquals(RobotState.ENABLED, r.getState());
		assertEquals(3, observer.count);
		
		// But now it loses connection
		sender.sendMessage("event/error/robot/1a2b3c4d/connection",
				"error", false);
		Thread.sleep(100);

		assertEquals(RobotState.DISCONNECTED, r.getState());
		assertEquals(4, observer.count);
		
		// Which should not change if it disables itself
		sender.sendMessage("extension/2/robot/state/1a2b3c4d",
				"x", false);
		Thread.sleep(100);

		assertEquals(RobotState.DISCONNECTED, r.getState());
		assertEquals(5, observer.count); // The event will still fire 
		
		// But after reconnect the status change should be visible
		sender.sendMessage("event/error/robot/1a2b3c4d/connection",
				null,false);
		Thread.sleep(100);

		assertEquals(RobotState.MANUAL_DISABLED_ROBOT, r.getState());
		assertEquals(6, observer.count); 
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

		Thread.sleep(100);

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

	/*@Test 
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
		sender.sendMessage("robot/virtualspeed", "1.87,5.025", false);
		Thread.sleep(100);

		assertEquals(1.87, this.guiController.getGame().getVRobotSpeed(), 0.002);
		assertEquals(5.025, this.guiController.getGame().getVRobotRotationSpeed(), 0.002);
		assertEquals(1, observer.count);
	}*/

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

	@Test
	public void testFood() throws Exception {
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

		IStage stage = this.guiController.getGame().getStage();

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
		sender.sendMessage("map/food", "2,3,1,10,5,7,0,10", false);
		Thread.sleep(100);

		assertEquals(6, observer.count);
		assertEquals(1, stage.getField(0, 0).getFood());
		assertEquals(10, stage.getField(1, 0).getFood());
		assertEquals(5, stage.getField(0, 1).getFood());
		assertEquals(10, stage.getField(1, 2).getFood());

		// Test single field food
		sender.sendMessage("map/food/1-0", "7", false);
		Thread.sleep(100);
		assertEquals(7, observer.count);
		assertEquals(7, stage.getField(1, 0).getFood());
		assertEquals(stage.getField(1, 0), observer.field);

		// Test discarding of invalid message
		sender.sendMessage("map/food", "2,3,7,5,3,5,6,7,9", false);
		Thread.sleep(100);

		assertEquals(7, observer.count);
		assertEquals(1, stage.getField(0, 0).getFood());
		assertEquals(7, stage.getField(1, 0).getFood());
	}

	@Test
	public void testStartpositions() throws Exception {
		class TestStageModelObserver implements IModelObserver {
			public int count;

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case STAGE_STARTPOSITIONS:
					this.count++;
					break;
				default:
					break;
				}
			}
		}

		IStage stage = this.guiController.getGame().getStage();

		TestStageModelObserver observer = new TestStageModelObserver();
		stage.observe(observer);

		// Init walls
		sender.sendMessage("map/walls", "3,3,,,,,,,,,", false);
		Thread.sleep(100);
		assertEquals(3, stage.getWidth());

		// Test new start positions
		sender.sendMessage("extension/2/map/startpositions",
				"1,0,n,2,1,e,0,1,s", false);
		Thread.sleep(100);

		List<IPosition> sp = stage.getStartPositions();
		assertEquals(3, sp.size());
		assertEquals(1, sp.get(0).getX());
		assertEquals(0, sp.get(0).getY());
		assertEquals(Orientation.NORTH, sp.get(0).getOrientation());
		assertEquals(2, sp.get(1).getX());
		assertEquals(1, sp.get(1).getY());
		assertEquals(Orientation.EAST, sp.get(1).getOrientation());
		assertEquals(0, sp.get(2).getX());
		assertEquals(1, sp.get(2).getY());
		assertEquals(Orientation.SOUTH, sp.get(2).getOrientation());
		assertEquals(1, observer.count);

		// Test discarding of wrong length message
		sender.sendMessage("extension/2/map/startpositions", "2,1,s,2,1,e,0,1",
				false);
		Thread.sleep(100);

		assertEquals(3, sp.size());
		assertEquals(1, observer.count);
		assertEquals(1, sp.get(0).getX());
		assertEquals(0, sp.get(0).getY());
		assertEquals(Orientation.NORTH, sp.get(0).getOrientation());

		// Test discarding of invalid start positions
		// first valid, second invalid Orientation
		sender.sendMessage("extension/2/map/startpositions", "0,1,s,2,1,b",
				false);
		Thread.sleep(100);

		assertEquals(1, sp.size());
		assertEquals(0, sp.get(0).getX());
		assertEquals(1, sp.get(0).getY());
		assertEquals(Orientation.SOUTH, sp.get(0).getOrientation());
		assertEquals(2, observer.count);
	}

}
