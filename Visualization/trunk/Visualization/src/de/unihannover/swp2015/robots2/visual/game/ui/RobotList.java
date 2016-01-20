package de.unihannover.swp2015.robots2.visual.game.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IResizable;

/**
 * Displays a list of items based on a java {@link List}.
 * 
 * @author Rico Schrage
 */
public class RobotList extends Widget implements IResizable {
	
	private static final String FONT = "default-font";
	
	private final List<IRobot> robots;
	private final Skin skin;
	private float distance;
	
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
		for (int i = 0; i < robots.size(); ++i) {
			final IRobot robot = robots.get(i);
			skin.getFont(FONT).draw(batch, i + ". " + robot.getName(), getX(), getY() + i*distance);
			skin.getFont(FONT).draw(batch, String.valueOf(robot.getScore()), getX() + getParent().getWidth()/2, getY() + i*distance );
		}
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

	@Override
	public void onResize(Viewport view) {
		
	}
	
}
