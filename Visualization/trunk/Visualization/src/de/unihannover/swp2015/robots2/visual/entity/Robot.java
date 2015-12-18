package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.visual.core.GameConst;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.entity.modifier.MoveModifierX;
import de.unihannover.swp2015.robots2.visual.entity.modifier.MoveModifierY;
import de.unihannover.swp2015.robots2.visual.entity.modifier.RotationModifier;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IFinishListener;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;
import de.unihannover.swp2015.robots2.visual.util.ColorUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of robots
 * 
 * @version 1.0
 * @author Daphne Schössow
 */
public class Robot extends Entity {
	
	/**
	 * Visual representation of the entity.
	 */
	private final RenderUnit robo;
	
	/**
	 * Data of the information bubble.
	 */
	private final Bubble bubble;
	
	/**
	 * Width of the robot.
	 */
	private float width;
	
	/**
	 * Height of the robot.
	 */
	private float height;
	
	private final RenderUnit startpos;
	
	private boolean startp = false;
	
	private int arrowrot = 0;
	
	private float fieldWidth;
	private float fieldHeight;
	
	/**
	 * Constructs a robot entity using given parameters.
	 * 
	 * @param robModel data model of the {@link Robot}
	 * @param batch for drawing the robot
	 * @param gameHandler parent
	 */
	public Robot(final IRobot robModel, RobotGameHandler gameHandler) {
		super(robModel, gameHandler);

		this.robo = resHandler.createRenderUnit(ResConst.DEFAULT_ROBO);
		this.bubble = new Bubble();
		this.modList = new ArrayList<>();
		this.startpos = resHandler.createRenderUnit(ResConst.DEFAULT_STARTPOS);

		this.fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		this.fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

		this.width = fieldWidth * GameConst.ROBOT_SCALE;
		this.height = fieldHeight * GameConst.ROBOT_SCALE;
		this.renderX = robModel.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
		this.renderY = robModel.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
		
		this.initDirection(robModel);
		this.initBubble(robModel, fieldWidth, fieldHeight);
		if(robModel.getState() == RobotState.SETUPSTATE) {
			this.drawStartPositions(robModel);
		}
	}
	
	/**
	 * Initializes the data of the information bubble.
	 * 
	 * @param robo data model of the robot
	 * @param fieldWidth field width
	 * @param fieldHeight field height
	 */
	private void initBubble(final IRobot robo, final float fieldWidth, final float fieldHeight) {
		this.bubble.tex = resHandler.createRenderUnit(ResConst.DEFAULT_BUBBLE);
		this.bubble.color = ColorUtil.fromAwtColor(robo.getColor());
		this.bubble.color = bubble.color.set(bubble.color.r, bubble.color.g, bubble.color.b, bubble.color.a * 0.7f);
		this.bubble.font = resHandler.getFont(ResConst.DEFAULT_FONT);
        this.bubble.points = robo.getId().substring(0, 4) + " : " + robo.getScore() + "(" + ((RobotGameHandler) gameHandler).getRanking(robo) + ")";
		this.bubble.x = robo.getPosition().getX() * fieldWidth - renderX;
		this.bubble.y = robo.getPosition().getY() * fieldHeight - renderY;
		this.bubble.width = fieldWidth * 0.75f;
		this.bubble.height = fieldHeight * 0.2f;
	}
	
	private void drawStartPositions(final IRobot robo) {
		this.startp = true;
		switch(robo.getPosition().getOrientation()){
		case SOUTH:
			this.arrowrot = 180;
			break;
		case NORTH:
			this.arrowrot = 0;
			break;
		case WEST:
			this.arrowrot = -90;
			break;
		case EAST:
			this.arrowrot = 90;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Initializes the <code>rotation</code> of the robot.
	 * 
	 * @param robo data model of the robot
	 */
	private void initDirection(final IRobot robo) {
		switch(robo.getPosition().getOrientation()) {
		case SOUTH:
			this.rotation = 180;
			break;
		case NORTH:
			this.rotation = 0;
			break;
		case WEST:
			this.rotation = -90;
			break;
		case EAST:
			this.rotation = 90;
			break;
		default:
			break;
		}
	}

	/**
	 * Updates x/y-coordinate of the robot. This method uses entityModifier for a smooth transition.
	 * 
	 * @see IEntityModifier
	 * @param robo data model of the robot
	 */
	private void updateRobot(final IRobot robo) {

		if (modList != null)
			modList.clear();
		
		IEntityModifier mod;
		
		//TODO dynamic duration
		//TODO use progress
		switch (robo.getPosition().getOrientation()) {
		case SOUTH:
			mod = new RotationModifier(this, 0.1f, rotation, 180);
			break;
		case NORTH:
			mod = new RotationModifier(this, 0.1f, rotation, 0);
			break;
		case WEST:
			mod = new RotationModifier(this, 0.1f, rotation, -90);
			break;
		case EAST:
			mod = new RotationModifier(this, 0.1f, rotation, 90);
			break;
		default:
			mod = new RotationModifier(this, 0.1f, rotation, 90);
			break;
		}
		
		mod.addFinishListener(new IFinishListener() {
			@Override
			public void onFinish() {
				Robot.this.updatePosition(robo);
			}
		});
		this.registerModifier(mod);
	}
	
	/**
	 * Updated the position of the robot using {@link IEntityModifier}.
	 * 
	 * @param robo model of the robot
	 */
	private void updatePosition(final IRobot robo) { 

		final float newRenderX = robo.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
		final float newRenderY = robo.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;

		if ((int) renderX != (int) newRenderX) 
			this.registerModifier(new MoveModifierX(this, 0.4f, renderX, newRenderX));
		if ((int) renderY != (int) newRenderY) 
			this.registerModifier(new MoveModifierY(this, 0.4f, renderY, newRenderY));
	}
	
	@Override
	public void draw(final Batch batch) {
		if(this.startp) // TODO check in whole game
			startpos.draw(batch, renderX, renderY, fieldWidth/2f, fieldHeight/2f, fieldWidth, fieldHeight, 1f, 1f, arrowrot);
		else{
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
				
			case ROBOT_POSITION:
				if (this.modList != null && this.modList.size() == 0)
					this.updateRobot(robotModel);
				break;
					
			case ROBOT_SCORE:
				this.bubble.points = robotModel.getId().substring(0, 4) + "  : " + robotModel.getScore() + "("
						+ handler.getRanking(robotModel) + ")";
				break;
	
			case ROBOT_STATE:
				this.startp = robotModel.getState() == RobotState.SETUPSTATE;
	
				break;
			case STAGE_STARTPOSITIONS:
				drawStartPositions(robotModel);
				break;
				default:
				break;
		}		
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		
		if (updatedKey.getEnum() == PrefConst.FIELD_HEIGHT_KEY || updatedKey.getEnum() == PrefConst.FIELD_WIDTH_KEY) {
			this.fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
			this.fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

			if (modList != null)
				this.modList.clear();
			
			final IRobot r = (IRobot) model;

			this.width = fieldWidth * GameConst.ROBOT_SCALE;
			this.height = fieldHeight * GameConst.ROBOT_SCALE;
			this.renderX = r.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
			this.renderY = r.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
			
			this.bubble.width = fieldWidth * 0.75f;
			this.bubble.height = fieldHeight * 0.2f;
			this.bubble.x = r.getPosition().getX() * fieldWidth - renderX;
			this.bubble.y = r.getPosition().getY() * fieldHeight - renderY;
		}
	}
	
	/**
	 * Model for an information bubble.
	 * 
	 * @author Rico Schrage
	 * @author Daphne Schössow
	 */
	protected static class Bubble {
	
		/**
		 * Texture of the bubble.
		 */
		public RenderUnit tex;
		
		/**
		 * Width of the bubble.
		 */
		public float width;
		
		/**
		 * Height of the bubble.
		 */
		public float height;
		
		/**
		 * Color of the bubble. This value is based on the model.
		 */
		public Color color;
		
		/**
		 * Font of the name/score/rank.
		 */
		public BitmapFont font;
		
		/**
		 * Points as string.
		 */
		public CharSequence points;
		
		/**
		 * X-position on (virtual) screen relative to the robot.
		 */
		public float x;
	
		/**
		 * Y-position on (virtual) screen relative to the robot.
		 */
		public float y;
	
	}

}
