package de.unihannover.swp2015.robots2.visual.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.game.GameConst;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.game.entity.component.RobotEngine;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;
import de.unihannover.swp2015.robots2.visual.util.ModelUtil;

/**
 * An entity used for the visualization of robots
 * 
 * @version 1.0
 * @author Daphne Schössow
 */
public class Robot extends Entity {
	
	/** Visual representation of the entity. */
	private final RenderUnit robo;
		
	/** RenderUnit, which contains a texture to draw the start-position */
	private final RenderUnit startPositionTexture;
	
	/** True if the start-position should be drawn */
	private boolean drawStartPosition = false;
		
	/** Width of the field */
	private float fieldWidth;
	
	/** Height of the field */
	private float fieldHeight;
	
	/** Bubble of the robot, which displays some information about the robot */
	private RobotBubble bubble;
	
	/** True if the robot should get rendered */
	private boolean renderRobot;
	
	/** True if the bubble should get rendered (if renderRobot == false this has no effect) */
	private boolean renderBubble;
	
	/**
	 * Constructs a robot entity using given parameters.
	 * 
	 * @param robot data model of the {@link Robot}
	 * @param batch for drawing the robot
	 * @param gameHandler parent
	 */
	public Robot(IRobot robot, RobotGameHandler gameHandler) {
		super(robot, gameHandler);
		
		this.robo = resHandler.createRenderUnit(ResConst.DEFAULT_ROBO);
		this.startPositionTexture = resHandler.createRenderUnit(ResConst.DEFAULT_STARTPOS);
		this.drawStartPosition = robot.getState() == RobotState.SETUPSTATE;
		this.rotation = ModelUtil.calculateRotation(robot.getPosition().getOrientation());
		
		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY);
		this.renderRobot = robot.isHardwareRobot() ? prefs.getBoolean(PrefKey.RENDER_ROBOTS) : prefs.getBoolean(PrefKey.RENDER_VIRTUAL_ROBOTS);
		this.renderBubble = prefs.getBoolean(PrefKey.RENDER_SCORE);
		
		this.updateWidth(robot);
		this.updateHeight(robot);
		this.bubble = new RobotBubble(gameHandler, this);
		
		this.registerComponent(new RobotEngine(prefs));
	}
	
	/**
	 * Updates all values, which depend on {@link fieldWidth}
	 * 
	 * @param robot robot model
	 */
	private void updateWidth(final IRobot robot) {
		this.width = fieldWidth * GameConst.ROBOT_SCALE;
		this.renderX = robot.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
	}
	
	/**
	 * Updates all values, which depend on {@link fieldHeight}
	 * 
	 * @param robot robot model
	 */
	private void updateHeight(final IRobot robot) {
		this.height = fieldHeight * GameConst.ROBOT_SCALE;
		this.renderY = robot.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
	}
	
	@Override
	public void draw(final Batch batch) {
		if (renderRobot) {
			super.draw(batch);
		
			final IRobot robot = (IRobot) model; 	
			
			if (drawStartPosition) {
				startPositionTexture.draw(batch, fieldWidth*robot.getPosition().getX(), fieldHeight*robot.getPosition().getY(), fieldWidth/2f, fieldHeight/2f, fieldWidth, fieldHeight, 1f, 1f, rotation);
			} 
			else {
				robo.draw(batch, renderX, renderY, width, height, width/2f, height/2f, 1f, 1f, rotation);
				if (renderBubble) {
					bubble.draw(batch);
				}
			}
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		IRobot robotModel = (IRobot) model;
		
		switch(event.getType()) {
		
			case ROBOT_ADD:
				break;
	
			case ROBOT_STATE:
				drawStartPosition = robotModel.getState() == RobotState.SETUPSTATE;
				break;

			default:
				//other events won't be handled 
				break;
		}		
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		final IRobot robot = (IRobot) model;

		switch(updatedKey) {
		
		case FIELD_HEIGHT_KEY:
			modList.clear();
			fieldHeight = (float) value;
			updateHeight(robot);
			break;
			
		case FIELD_WIDTH_KEY:
			modList.clear();
			fieldWidth = (float) value;
			updateWidth(robot);
			break;

		case RENDER_ROBOTS:
			renderRobot = robot.isHardwareRobot() ? (boolean) value : renderRobot; 
			break;
			
		case RENDER_SCORE:
		case RENDER_NAME:
			renderBubble = (boolean) value;
			break;
			
		case RENDER_VIRTUAL_ROBOTS:
			renderRobot = robot.isHardwareRobot() ? renderRobot : (boolean) value; 
			break;
			
		default:
			break;
		
		}
	}

}
