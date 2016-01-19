package de.unihannover.swp2015.robots2.visual.game.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * Displays a list of items based on a java {@link List}.
 * 
 * @author Rico Schrage
 */
public class RobotList extends Widget {

	private static final String FONT_NAME = "default";
	
	private final List<IRobot> robots;
	private final Skin skin;
	private float padding;
	private float distance;
	
	public RobotList(Skin skin, List<IRobot> robots) {
		super();		
		
		this.skin = skin;
		this.robots = robots;
	}
	
	@Override
	public void draw(Batch batch, float delta) {
		for (int i = 0; i < robots.size(); ++i) {
			final IRobot robot = robots.get(i);
			skin.getFont(FONT_NAME).draw(batch, i + ". " + robot.getName(), padding, i*distance);
			skin.getFont(FONT_NAME).draw(batch, String.valueOf(robot.getScore()), getWidth()/2, i*distance);
		}
	}

	/**
	 * @return the padding
	 */
	public float getPadding() {
		return padding;
	}

	/**
	 * @param padding the padding to set
	 */
	public void setPadding(float padding) {
		this.padding = padding;
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
