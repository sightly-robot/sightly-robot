package de.unihannover.swp2015.robots2.visual.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
import de.unihannover.swp2015.robots2.visual.util.ColorUtil;
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
	
	/** Data of the information bubble. */
	private final Bubble bubble;
	
	/** RenderUnit, which contains a texture to draw the start-position */
	private final RenderUnit startPositionTexture;
	
	/** true if the start-position should be drawn */
	private boolean drawStartPosition = false;
		
	/** width of the field */
	private float fieldWidth;
	
	/** height of the field */
	private float fieldHeight;
	
	/**
	 * Constructs a robot entity using given parameters.
	 * 
	 * @param robot data model of the {@link Robot}
	 * @param batch for drawing the robot
	 * @param gameHandler parent
	 */
	public Robot(final IRobot robot, RobotGameHandler gameHandler) {
		super(robot, gameHandler);

		this.robo = resHandler.createRenderUnit(ResConst.DEFAULT_ROBO);
		this.bubble = new Bubble();
		this.startPositionTexture = resHandler.createRenderUnit(ResConst.DEFAULT_STARTPOS);
		this.drawStartPosition = robot.getState() == RobotState.SETUPSTATE;
		this.rotation = ModelUtil.calculateRotation(robot.getPosition().getOrientation());

		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY);

		this.updateWidth(robot);
		this.updateHeight(robot);
		this.initBubble(robot);
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
		this.bubble.x = robot.getPosition().getX() * fieldWidth - renderX;
		this.bubble.width = fieldWidth * 0.75f;
	}
	
	/**
	 * Updates all values, which depend on {@link fieldHeight}
	 * 
	 * @param robot robot model
	 */
	private void updateHeight(final IRobot robot) {
		this.height = fieldHeight * GameConst.ROBOT_SCALE;
		this.renderY = robot.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
		this.bubble.height = fieldHeight * 0.2f;
		this.bubble.y = robot.getPosition().getY() * fieldHeight - renderY;
	}
	
	/**
	 * Initializes the data of the information bubble.
	 * 
	 * @param robo data model of the robot
	 * @param fieldWidth field width
	 * @param fieldHeight field height
	 */
	private void initBubble(final IRobot robo) {
		this.bubble.tex = resHandler.createRenderUnit(ResConst.DEFAULT_BUBBLE);
		this.bubble.color = ColorUtil.fromAwtColor(robo.getColor());
		this.bubble.color = bubble.color.set(bubble.color.r, bubble.color.g, bubble.color.b, bubble.color.a * 0.7f);
		this.bubble.font = resHandler.getFont(ResConst.DEFAULT_FONT);
		this.bubble.points = robo.getId().substring(0, 4) + " : " + robo.getScore() + "(" + ((RobotGameHandler) gameHandler).getRanking(robo) + ")";
	}
	
	@Override
	public void draw(final Batch batch) {
		super.draw(batch);
	
		final IRobot robot = (IRobot) model; 	
		
		if( drawStartPosition) {
			startPositionTexture.draw(batch, fieldWidth*robot.getPosition().getX(), fieldHeight*robot.getPosition().getY(), fieldWidth/2f, fieldHeight/2f, fieldWidth, fieldHeight, 1f, 1f, rotation);
		} 
		else {
			robo.draw(batch, renderX, renderY, width/2f, height/2f, width, height, 1f, 1f, rotation);	
	
			batch.setColor(bubble.color);		
			bubble.tex.draw(batch, renderX + bubble.x, renderY + bubble.y, bubble.width, bubble.height);
			bubble.font.setColor(1-bubble.color.r, 1-bubble.color.g, 1-bubble.color.b, bubble.color.a);
			bubble.font.draw(batch, bubble.points, renderX + 5 + bubble.x, renderY + 5 + bubble.y);
			batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		IRobot robotModel = (IRobot) model;
		RobotGameHandler handler = (RobotGameHandler) gameHandler;
		
		switch(event.getType()) {
		
			case ROBOT_ADD:
				break;
					
			case ROBOT_SCORE:
				bubble.points = robotModel.getId().substring(0, 4) + "  : " + robotModel.getScore() + "("
						+ handler.getRanking(robotModel) + ")";
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
			
		default:
			break;
		
		}
	}
	
	/**
	 * Model for a bubble, which displays information about the robot.
	 * 
	 * @author Rico Schrage
	 * @author Daphne Schössow
	 */
	protected static class Bubble {
	
		/**
		 * Texture of the bubble.
		 */
		protected RenderUnit tex;
		
		/**
		 * Width of the bubble.
		 */
		protected float width;
		
		/**
		 * Height of the bubble.
		 */
		protected float height;
		
		/**
		 * Color of the bubble. This value is based on the model.
		 */
		protected Color color;
		
		/**
		 * Font of the name/score/rank.
		 */
		protected BitmapFont font;
		
		/**
		 * Points as string.
		 */
		protected CharSequence points;
		
		/**
		 * X-position on (virtual) screen relative to the robot.
		 */
		protected float x;
	
		/**
		 * Y-position on (virtual) screen relative to the robot.
		 */
		protected float y;
	
	}

}
