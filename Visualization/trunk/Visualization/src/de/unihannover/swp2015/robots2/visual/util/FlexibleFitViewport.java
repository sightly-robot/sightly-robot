package de.unihannover.swp2015.robots2.visual.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class FlexibleFitViewport extends FitViewport {

	private int offsetX = 0;
	private int offsetY = 0;
	
	public FlexibleFitViewport(float worldWidth, float worldHeight) {
		this(worldWidth, worldHeight, new OrthographicCamera(worldWidth, worldHeight));
	}
	
	public FlexibleFitViewport(float worldWidth, float worldHeight, Camera camera) {
		super(worldWidth, worldHeight, camera);
	}
	
	/**
	 * Set offset of the viewport in x direction.
	 * @param offsetX offset x axes
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * Set offset of the viewport in y direction.
	 * 
	 * @param offsetY offset y axes
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * Set offset of the viewport in x/y direction.
	 * 
	 * @param offsetX offset x axes
	 * @param offsetY offset y axes
	 */
	public void setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	@Override
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
		Vector2 scaled = getScaling().apply(getWorldWidth(), getWorldHeight(), screenWidth, screenHeight);
		int viewportWidth = Math.round(scaled.x);
		int viewportHeight = Math.round(scaled.y);

		// Center.
		setScreenBounds((screenWidth - viewportWidth) / 2 + offsetX, (screenHeight - viewportHeight) / 2 + offsetY, viewportWidth, viewportHeight);

		apply(centerCamera);
	}

}
