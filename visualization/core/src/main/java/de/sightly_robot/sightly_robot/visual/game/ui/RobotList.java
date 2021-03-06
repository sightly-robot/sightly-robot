package de.sightly_robot.sightly_robot.visual.game.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.visual.resource.ResConst;
import de.sightly_robot.sightly_robot.visual.util.ColorUtil;

/**
 * Displays a list of items based on {@link List}.
 * 
 * @author Rico Schrage
 */
public class RobotList extends Widget {

	/** main model */
	private final List<IRobot> robots;

	/** skin object which contains the font */
	private Skin skin;

	/** spacing between name of the robot and the score at the ranking */
	private float distance;

	/**
	 * Constructs a robot specific list, which updates itself when the list
	 * changes.
	 * 
	 * @param skin
	 *            has to contain a font with the key <code>default-font</code>
	 * @param robots
	 *            list of {@link IRobot}
	 */
	public RobotList(Skin skin, List<IRobot> robots) {
		super();

		this.skin = skin;
		this.robots = robots;
	}

	/**
	 * @return current skin
	 */
	public Skin getSkin() {
		return skin;
	}

	/**
	 * @param skin
	 *            desired skin
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	/**
	 * @return current distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            desired distance
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * @return size of the list of robots
	 */
	public int getSize() {
		return robots.size();
	}

	@Override
	public void draw(Batch batch, float delta) {
		super.draw(batch, delta);

		if (getParent() == null)
			return;
		final BitmapFont font = skin.getFont(ResConst.SKIN_RANKING_FONT
				.toString());
		font.getData().setScale(getScaleX(), getScaleY());
		for (int i = 0; i < robots.size(); ++i) {
			final IRobot robot = robots.get(i);
			font.setColor(ColorUtil.fromAwtColor(robot.getColor()));
			font.draw(batch, (i + 1) + ". " + robot.getName(), getX(), getY()
					+ i * distance);
			font.draw(batch, String.valueOf(robot.getScore()), getX()
					+ getParent().getWidth() / 2, getY() + i * distance);
		}
		font.getData().setScale(1, 1);
	}

	@Override
	public float getPrefHeight() {
		return distance * robots.size();
	}

	@Override
	public float getPrefWidth() {
		return distance * robots.size();
	}

	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);

		if (stage != null) {
			distance = stage.getHeight() / 10;
		}
	}

}
