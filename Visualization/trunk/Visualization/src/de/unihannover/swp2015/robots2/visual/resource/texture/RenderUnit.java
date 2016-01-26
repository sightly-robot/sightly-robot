package de.unihannover.swp2015.robots2.visual.resource.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Abstracts renderable data like animations, compounds of {@link TextureRegion}
 * s, and single {@link TextureRegion}s. <br>
 * It can be used as common representation of {@link TextureRegion} and
 * {@link Animation} (technically it's not a common base class).
 * 
 * @author Rico Schrage
 */
public class RenderUnit {

	/** Can be null. */
	protected Animation animation;

	/** State of the animation. */
	protected float stateTime = 0;

	/** Can be null. */
	protected TextureRegion tex;

	/**
	 * Creates an uninitialized instance of {@link RenderUnit}.
	 */
	public RenderUnit() {
		// has to be initialized
	}

	/**
	 * Initializes the textureUnit as an animation with the given frames.
	 * 
	 * @param frames
	 *            frames for the animation
	 */
	public void initAsAnimation(final Array<AtlasRegion> frames,
			final Animation.PlayMode playMode, final float duration) {
		this.tex = null;
		this.animation = new Animation(duration, frames, playMode);
		this.stateTime = 0;
	}

	/**
	 * Initializes the textureUnit as normal texture(-region) with the given
	 * textureRegion.
	 * 
	 * @param tex
	 *            {@link TextureRegion}
	 */
	public void initAsTexture(final TextureRegion tex) {
		this.animation = null;
		this.tex = tex;
	}

	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float)}
	 * @param batch
	 *            batch which will be used for drawing
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void draw(final Batch batch, final float x, final float y) {
		if (isTexture()) {
			batch.draw(tex, x, y);
		} else if (isAnimation()) {
			stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y);
		} else {
			throw new IllegalStateException(
					"The TextureUnit is not initialized!");
		}
	}

	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float, float, float)}
	 * @param batch
	 *            batch which will be used for drawing
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public void draw(final Batch batch, final float x, final float y,
			final float width, final float height) {
		if (isTexture()) {
			batch.draw(tex, x, y, width, height);
		} else if (isAnimation()) {
			stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y, width, height);
		} else {
			throw new IllegalStateException(
					"The TextureUnit is not initialized!");
		}
	}

	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float, float, float)}
	 * @param batch
	 *            batch which will be used for drawing
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param originX
	 *            x coordinate of scaling and rotation center relative to the
	 *            screen space coordinates
	 * @param originY
	 *            y coordinate of scaling and rotation center relative to the
	 *            screen space coordinates
	 * @param scaleX
	 *            scaling factor in x direction
	 * @param scaleY
	 *            scaling factor in y direction
	 * @param rotation
	 *            rotation in degrees
	 */
	public void draw(final Batch batch, final float x, final float y,
			final float width, final float height, final float originX,
			final float originY, final float scaleX, final float scaleY,
			final float rotation) {

		if (isTexture()) {
			batch.draw(tex, x, y, originX, originY, width, height, scaleX,
					scaleY, rotation);
		} else if (isAnimation()) {
			stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y, originX,
					originY, width, height, scaleX, scaleY, rotation);
		} else {
			throw new IllegalStateException(
					"The TextureUnit is not initialized!");
		}
	}

	/**
	 * Returns if the unit represents an {@link Animation}.
	 *
	 * @return true, if it represents an {@link Animation}, false otherwise
	 */
	public boolean isAnimation() {
		return animation != null;
	}

	/**
	 * Returns if the unit represents a {@link TextureRegion}.
	 * 
	 * 
	 * Hint: It's not possible, that {@link RenderUnit.isTexture} and
	 * {@link RenderUnit#isAnimation()} are true.
	 *
	 * @return true, if it represents a {@link TextureRegion}, false otherwise
	 */
	public boolean isTexture() {
		return tex != null;
	}

	/**
	 * @return width of the render unit
	 */
	public float getWidth() {
		if (isTexture()) {
			return tex.getRegionWidth();
		} else if (isAnimation()) {
			return animation.getKeyFrame(stateTime).getRegionWidth();
		}
		throw new IllegalStateException("The TextureUnit is not initialized!");
	}

	/**
	 * @return height of the render unit
	 */
	public float getHeight() {
		if (isTexture()) {
			return tex.getRegionHeight();
		} else if (isAnimation()) {
			return animation.getKeyFrame(stateTime).getRegionHeight();
		}
		throw new IllegalStateException("The TextureUnit is not initialized!");
	}

}
