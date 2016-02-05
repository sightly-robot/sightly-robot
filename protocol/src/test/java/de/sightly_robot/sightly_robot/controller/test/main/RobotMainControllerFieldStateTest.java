package de.sightly_robot.sightly_robot.controller.test.main;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.controller.main.RobotMainController;
import de.sightly_robot.sightly_robot.controller.mqtt.MqttController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent.UpdateType;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;

public class RobotMainControllerFieldStateTest {

	private static class TestModelObserver implements IModelObserver {
		public int count;

		@Override
		public void onModelUpdate(IEvent event) {
			if (event.getType() == UpdateType.FIELD_STATE)
				this.count++;
		}
	}

	private IRobotController robotController;
	private MqttController otherPart;
	private TestReceiveHandler otherPartHandler;
	private TestModelObserver observer;

	@Before
	public void setup() throws Exception {
		// Setup sender and IRobotController
		this.otherPartHandler = new TestReceiveHandler();
		this.otherPart = new MqttController("junit_otherPart"
				+ UUID.randomUUID().toString(), this.otherPartHandler,
				Arrays.asList(new String[] { "#" }));
		this.otherPart.connect("tcp://localhost");

		this.robotController = new RobotMainController(false);
		this.robotController.startMqtt("tcp://localhost");
		this.observer = new TestModelObserver();

		// Init and observe Fields
		this.otherPart.sendMessage("map/walls", "2,2,nw,ne,sw,se", false);
		Thread.sleep(100);
		this.robotController.getGame().getStage().getField(1, 0)
				.observe(observer);
	}

	@After
	public void cleanUp() {
		this.robotController.releaseField(1, 0);
	}

	@Test
	public void testOtherLockOccupyRelease() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Lock field
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.LOCKED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(1, this.observer.count);

		// Occupy field
		this.otherPart.sendMessage("map/occupied/set/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.OCCUPIED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(2, this.observer.count);

		// Check that no timer is changing field state
		Thread.sleep(5000);
		assertEquals(State.OCCUPIED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(2, this.observer.count);

		// Release field
		this.otherPart.sendMessage("map/occupied/release/1-0", "", false);
		Thread.sleep(50);

		assertEquals(State.FREE, f.getState());
		assertEquals("", f.getLockedBy());
		assertEquals(3, this.observer.count);

		// Immediately occupy field (missed lock message)
		this.otherPart.sendMessage("map/occupied/set/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.OCCUPIED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(4, this.observer.count);
	}

	@Test
	public void testOtherLockTimeout() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Lock field
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.LOCKED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(1, this.observer.count);

		// Check before timer runout
		Thread.sleep(2800);
		assertEquals(State.LOCKED, f.getState());

		// Let timer run out
		Thread.sleep(500);
		assertEquals(State.FREE, f.getState());
		assertEquals("", f.getLockedBy());
		assertEquals(2, this.observer.count);
	}

	@Test
	public void testOwnLockOccupyRelease() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Request field
		this.robotController.requestField(1, 0);
		Thread.sleep(50);

		assertEquals(State.LOCK_WAIT, f.getState());
		assertEquals(1, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/lock/1-0"));

		Thread.sleep(70);

		assertEquals(State.OURS, f.getState());
		assertEquals(2, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/set/1-0"));

		// Check that no timer is changing field state
		Thread.sleep(3500);
		assertEquals(State.OURS, f.getState());
		assertEquals(2, this.observer.count);

		// Release field
		this.robotController.releaseField(1, 0);
		Thread.sleep(50);

		assertEquals(State.FREE, f.getState());
		assertEquals("", f.getLockedBy());
		assertEquals(3, this.observer.count);
		assertEquals("", otherPartHandler.getValue("map/occupied/release/1-0"));

	}

	@Test
	public void testLockConflictWin() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Request field
		this.robotController.requestField(1, 0);
		Thread.sleep(50);
		assertEquals(State.LOCK_WAIT, f.getState());
		assertEquals(1, this.observer.count);
		assertEquals(1, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/lock/1-0"));

		// Other also sends lock
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.RANDOM_WAIT, f.getState());
		assertEquals(2, this.observer.count);

		// Nothing happens for 3.5 seconds, so we should send new lock and
		// occupy
		Thread.sleep(3500);

		assertEquals(State.OURS, f.getState());
		assertEquals(4, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/lock/1-0"));
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/set/1-0"));
	}

	@Test
	public void testLockConflictLose() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Request field
		this.robotController.requestField(1, 0);
		Thread.sleep(50);
		assertEquals(State.LOCK_WAIT, f.getState());
		assertEquals(1, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/lock/1-0"));
		otherPartHandler.removeValue("map/occupied/lock/1-0");

		// Other also sends lock
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.RANDOM_WAIT, f.getState());
		assertEquals(2, this.observer.count);

		// Other part sends new lock after (too) short time
		Thread.sleep(20);
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(100);

		assertEquals(State.LOCKED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(3, this.observer.count);

		this.otherPart.sendMessage("map/occupied/set/1-0", "1a2b3c4d", false);

		// No wrong timers (no lock or occupy from us)
		Thread.sleep(3500);
		assertEquals(State.OCCUPIED, f.getState());
		assertEquals("1a2b3c4d",
				otherPartHandler.getValue("map/occupied/lock/1-0"));
		assertEquals("1a2b3c4d",
				otherPartHandler.getValue("map/occupied/set/1-0"));
	}

	@Test
	public void testLockConflictRelease() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Request field
		this.robotController.requestField(1, 0);
		Thread.sleep(50);
		assertEquals(State.LOCK_WAIT, f.getState());
		assertEquals(1, this.observer.count);
		assertEquals(this.robotController.getMyself().getId(),
				otherPartHandler.getValue("map/occupied/lock/1-0"));

		// Other also sends lock
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(50);

		assertEquals(State.RANDOM_WAIT, f.getState());
		assertEquals(2, this.observer.count);

		// Our AI doesn't want to wait
		this.robotController.releaseField(1, 0);

		assertEquals(State.FREE, f.getState());
		assertEquals(3, this.observer.count);

		// We shouldn't send any message or change state, even after 3 seconds.
		Thread.sleep(3500);

		assertEquals(State.FREE, f.getState());
		assertEquals(3, this.observer.count);
		assertEquals("1a2b3c4d",
				otherPartHandler.getValue("map/occupied/lock/1-0"));
		assertEquals(null, otherPartHandler.getValue("map/occupied/set/1-0"));
	}

	@Test
	public void testLockTimerRestart() throws Exception {
		IField f = this.robotController.getGame().getStage().getField(1, 0);
		assertEquals(State.FREE, f.getState());

		// Other locks field, takes some time, occupys and releases
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);
		Thread.sleep(1000);
		this.otherPart.sendMessage("map/occupied/set/1-0", "1a2b3c4d", false);
		Thread.sleep(500);
		this.otherPart.sendMessage("map/occupied/release/1-0", "1a2b3c4d",
				false);
		Thread.sleep(500);

		assertEquals(State.FREE, f.getState());
		assertEquals(3, this.observer.count);

		// Other locks again
		this.otherPart.sendMessage("map/occupied/lock/1-0", "1a2b3c4d", false);

		// We should stay in State LOCKED for ~ 3s
		Thread.sleep(2800);
		assertEquals(State.LOCKED, f.getState());
		assertEquals("1a2b3c4d", f.getLockedBy());
		assertEquals(4, this.observer.count);

		// Then we should change state
		Thread.sleep(400);
		assertEquals(State.FREE, f.getState());
		assertEquals(5, this.observer.count);
	}

	@Test
	public void testConcurrentFieldTimers() throws Exception {
		IField f1 = this.robotController.getGame().getStage().getField(1, 0);
		IField f2 = this.robotController.getGame().getStage().getField(0, 0);
		assertEquals(State.FREE, f1.getState());
		assertEquals(State.FREE, f2.getState());

		// Other locks field 0-0 but loses interest
		this.otherPart.sendMessage("map/occupied/lock/0-0", "1a2b3c4d", false);
		Thread.sleep(1000);

		// We want field 1-0
		this.robotController.requestField(1, 0);

		// We should get the field after 100ms
		Thread.sleep(150);
		assertEquals(State.OURS, f1.getState());
		assertEquals(2, this.observer.count);

		// While the other field should be declared FREE after ~ 3s
		Thread.sleep(2000);
		assertEquals(State.FREE, f2.getState());
	}

}
