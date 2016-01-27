package de.unihannover.swp2015.robots2.visual.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.core.IResizable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;
import de.unihannover.swp2015.robots2.visual.game.ui.HudStage;
import de.unihannover.swp2015.robots2.visual.game.ui.RobotList;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;

/**
 * The UI draws the ranking at the end of a match.
 * 
 * It will be shown when the game has stopped/paused.
 * 
 * @author Rico Schrage
 */
public class UI implements IRenderable, IUpdateable, Disposable, IResizable {

	/** container of all UI elements which manages drawing and acting */
	private final Stage stage;
	/** model for the ranking (index of robot == place of the robot) */
	private final RobotList ranking;
	/** panel which manages position of the ranking */
	private final Container<RobotList> panel;
	/** title of the panel */
	private Label title;
	/** true if the UI should be visible */
	private boolean visible;

	/**
	 * Construct an UI using the given model, view, batch and resources.
	 * 
	 * @param robots
	 *            model for drawing a ranking
	 * @param view
	 *            viewport of the game
	 * @param batch
	 *            batch of the game
	 * @param res
	 *            resource container
	 */
	public UI(List<IRobot> robots, Viewport view, Batch batch,
			IResourceHandler res) {
		this.stage = new HudStage(view, batch);
		this.ranking = new RobotList(res.createSkin(), robots);
		this.title = new Label("Ranking", res.createSkin(),
				ResConst.SKIN_TITLE_FONT.toString(), Color.WHITE);
		this.panel = new Container<>(ranking);
		this.setupStage(res);
		this.onResize(view);

		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Sets initial values.
	 * 
	 * @param res
	 *            resources
	 */
	private void setupStage(IResourceHandler res) {
		panel.setColor(GameConst.UI_COLOR);
		panel.setBackground(res.createDrawableFromSkin(ResConst.DEFAULT_RECT));
		panel.align(Align.bottomLeft);
		stage.addActor(panel);
		stage.addActor(title);
	}

	/**
	 * Will be called after a robot has been added or removed.
	 */
	public void onRobotChange() {
		onResize(stage.getViewport());
	}

	/**
	 * Sets the whole UI visible.
	 * 
	 * @param visible
	 *            true if you want the UI visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update() {
		if (visible) {
			stage.act();
		}
	}

	@Override
	public void render() {
		if (visible) {
			stage.draw();
			stage.getBatch().setColor(Color.WHITE);
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void onResize(Viewport view) {
		panel.setWidth(view.getWorldWidth() / 2);
		panel.setHeight(panel.getWidth() * GameConst.UI_ASPECT_RATIO);
		panel.setPosition(view.getWorldWidth() / 2 - panel.getWidth() / 2,
				view.getWorldHeight() / 2 - panel.getHeight() / 2);
		panel.invalidate();
		panel.align(Align.bottomLeft);
		panel.pad(panel.getWidth() * GameConst.UI_PADDING_REL);

		final float scale = Math.min(GameConst.UI_RANKING_SCALE_SIZE_MULTIPLIER
				* 1 / (ranking.getSize() + 1) * view.getWorldWidth()
				* GameConst.UI_RANKING_FONT_SCALE, view.getWorldWidth()
				* GameConst.UI_RANKING_FONT_SCALE);
		ranking.setScale(scale, scale);
		ranking.setDistance((GameConst.UI_RANKING_DISTANCE_SIZE_MULTIPLIER)
				/ (ranking.getSize() + 1) * stage.getHeight()
				* GameConst.UI_DISTANCE_PART);

		title.setPosition(panel.getX(), panel.getY() - title.getHeight());
	}

}
