package de.unihannover.swp2015.robots2.visual.game.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * Displays a list of items based on a java {@link List}.
 * 
 * @author Rico Schrage
 */
public class RobotList extends Widget {
	
	/** Key for the font in the skin object */
	private static final String FONT = "default-font";
	
	/** Main model */
	private final List<IRobot> robots;
	
	/** Skin object, which contains the font */
	private Skin skin;
	
	/** Distance between name of the robot and the score */
	private float distance;
	
	/**
	 * Constructs a robot specific list, which updates itself when the list changes.
	 * 
	 * @param skin have to contain a font with the key <code>default-font</code>
	 * @param robots list of {@link IRobot}
	 */
	public RobotList(Skin skin, List<IRobot> robots) {
		super();		
		
		this.skin = skin;
		this.robots = robots;
	}
	
	@Override
	public void draw(Batch batch, float delta) {
		super.draw(batch, delta);
		
		if (getParent() == null)
			return;
		skin.getFont(FONT).getData().setScale(getScaleX(), getScaleY());
		for (int i = 0; i < robots.size(); ++i) {
			final IRobot robot = robots.get(i);
			skin.getFont(FONT).draw(batch, i + ". " + robot.getName(), getX(), getY() + i*distance);
			skin.getFont(FONT).draw(batch, String.valueOf(robot.getScore()), getX() + getParent().getWidth()/2, getY() + i*distance );
		}
		skin.getFont(FONT).getData().setScale(1, 1);
	}

	@Override
	public float getPrefHeight() {
		return distance*robots.size();
	}
	@Override
	public float getPrefWidth() {
		return distance*robots.size();
	}
	
	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);

		if (stage != null) {
			distance = stage.getHeight()/10;
		}
	}
	
	/**
	 * @return the skin
	 */
	public Skin getSkin() {
		return skin;
	}
	
	/**
	 * @param skin the skin to set
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	
	/**
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
}
