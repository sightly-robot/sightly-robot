package de.sightly_robot.sightly_robot.visual.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Replaces the implementation of {@link Stage#draw()} to make it work more
 * efficiently and more flexible (it works better as HUD).
 * 
 * @author Rico Schrage
 */
public class HudStage extends Stage {

	/**
	 * Creates a stage with a {@link ScalingViewport} set to
	 * {@link Scaling#stretch}.
	 * 
	 * The stage will use its own {@link Batch}, which will be disposed when the
	 * stage is disposed.
	 */
	public HudStage() {
		super();
	}

	/**
	 * Creates a stage with the specified viewport.
	 * 
	 * The stage will use its own {@link Batch}, which will be disposed when the
	 * stage is disposed.
	 */
	public HudStage(Viewport viewport) {
		super(viewport);
	}

	/**
	 * Creates a stage with the specified viewport and batch.
	 * 
	 * This can be used to avoid creating a new batch (which can be quite slow),
	 * if multiple stages are used during an application's life time.
	 * 
	 * @param batch
	 *            Won't be disposed if {@link #dispose()} is called.
	 */
	public HudStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
	}

	@Override
	public void draw() {
		if (!getRoot().isVisible())
			return;

		Batch batch = getBatch();
		if (batch != null) {
			batch.begin();
			getRoot().draw(batch, 1);
			batch.end();
		}
	}

}
