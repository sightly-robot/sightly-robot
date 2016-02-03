package de.unihannover.swp2015.robots2.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;

/**
 * Game server component, that observes robots, harvests food and credits the
 * robot's scores.
 * 
 * @author Michael Thies
 */
public class Harvester implements IModelObserver {
	private final IServerController controller;

	private static final Logger LOGGER = LogManager.getLogger(Harvester.class
			.getName());

	/** Map used to find robot on Field that just grew */
	private final Map<IField, IRobot> fieldBlockingRobot;
	/** Map used to manage observers and above map */
	private final Map<IRobot, IField> lastRobotPosition;

	public Harvester(IServerController controller) {
		this.controller = controller;
		this.lastRobotPosition = new HashMap<>();
		this.fieldBlockingRobot = new HashMap<>();

		this.controller.getGame().observe(this);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case ROBOT_ADD:
			IRobot r = (IRobot) event.getObject();
			r.observe(this);
			this.onPositionChange(r);
			break;
		case ROBOT_DELETE:
			this.onRobotDelete((IRobot) event.getObject());
			break;

		case GAME_STATE:
			if (this.controller.getGame().isRunning())
				// Initially harvest food on fields robots are standing on
				for (IRobot robot : this.controller.getGame().getRobots()
						.values())
					this.onPositionChange(robot);
			break;

		case ROBOT_POSITION:
		case ROBOT_STATE:
			this.onPositionChange((IRobot) event.getObject());
			break;

		case FIELD_FOOD:
			this.onFieldGrow((IField) event.getObject());
			break;

		default:
			break;
		}
	}

	/**
	 * Event handler to be called when food grows on an observed field.
	 * 
	 * @param robot
	 *            The Robot that changed its position.
	 */
	private void onFieldGrow(IField field) {
		IRobot robot = this.fieldBlockingRobot.get(field);
		// If a robot is on this field and it is active
		if (robot != null) {
			LOGGER.debug("Field {}-{} grew and robot {} stays on it.",
					field.getX(), field.getY(), robot.getId());
			this.harvest(robot, field);
		}
	}

	/**
	 * Event handler to be called when any robot changes it's position.
	 * 
	 * @param robot
	 *            The Robot that changed its position.
	 */
	private void onPositionChange(IRobot robot) {
		LOGGER.debug("New position for robot {}.", robot.getId());
		// Manage fieldBlockingRobot map
		IField lastPosition = this.lastRobotPosition.get(robot);
		if (lastPosition != null) {
			lastPosition.unobserve(this);
			this.fieldBlockingRobot.remove(lastPosition);
		}

		int x = robot.getPosition().getX();
		int y = robot.getPosition().getY();

		if (x < 0 || y < 0
				|| y >= this.controller.getGame().getStage().getHeight()
				|| x >= this.controller.getGame().getStage().getWidth())
			return;

		IField field = this.controller.getGame().getStage().getField(x, y);

		this.fieldBlockingRobot.put(field, robot);
		this.lastRobotPosition.put(robot, field);
		field.observe(this);

		this.harvest(robot, field);
	}

	/**
	 * Event handler to be called when a robot is deleted.
	 * 
	 * Deletes this robot from the maps.
	 * 
	 * @param robot
	 *            The deleted robot.
	 */
	private void onRobotDelete(IRobot robot) {
		LOGGER.debug("Deleting robot {} from data structures.", robot.getId());
		IField lastposition = this.lastRobotPosition.get(robot);
		if (lastposition != null) {
			this.lastRobotPosition.remove(robot);
			this.fieldBlockingRobot.remove(lastposition);
		}
	}

	/**
	 * Harvest the food on the given field and add the corresponding number of
	 * points to the given Robot's score. (But only if game is running and robot
	 * is enabled.)
	 * 
	 * Please make shure the robot is really on this field.
	 * 
	 * @param robot
	 *            The Robot (should be on the given field)
	 * @param field
	 *            The field this robot is on.
	 */
	private void harvest(IRobot robot, IField field) {
		if (field.getFood() != 0 && this.controller.getGame().isRunning()
				&& robot.getState() == RobotState.ENABLED) {
			LOGGER.debug("Harvesting food for robot {} on field {}-{}",
					robot.getId(), field.getX(), field.getY());

			controller.increaseScore(robot.getId(), field.getFood());
			controller.updateFood(field.getX(), field.getY(), 0);
		}
	}

}
