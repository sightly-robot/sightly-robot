package de.unihannover.swp2015.robots2.visual.resource.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Abstracts renderable data like animations, compund of {@link TextureRegion}'s, and single {@link TextureRegion}'s.
 * <br>
 * It can be used as common representation of {@link TextureRegion} and {@link Animation} (technically it's not a common base class).
 *  
 * @author Rico Schrage
 */
public class RenderUnit {

	/**
	 * Can be null.
	 */
	protected Animation animation;

	/**
	 * State of the animation.
	 */
	protected float stateTime = 0;

	/**
	 * Can be null.
	 */
	protected TextureRegion tex;

	/**
	 * Creates an uninitialized instance of {@link RenderUnit}.
	 */
	public RenderUnit() {

	}

	/**
	 * Initializes the textureUnit as animation with the given frames.
	 * 
	 * @param frames frames for the animation
	 */
	public void initAsAnimation(final Array<TextureRegion> frames, final Animation.PlayMode playMode,
			final float duration) {
		if (this.isAnimation() || this.isTexture())
			throw new IllegalStateException("You must not initialize it twice!");

		this.animation = new Animation(duration, frames, playMode);
	}

	/**
	 * Initializes the textureUnit as normal texture(-region) with the given
	 * textureRegion.
	 * 
	 * @param tex {@link TextureRegion}
	 */
	public void initAsTexture(final TextureRegion tex) {
		if (this.isAnimation() || this.isTexture())
			throw new IllegalStateException("You must not initialize it twice!");

		this.tex = tex;
	}

	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float)}
	 * @param batch batch, which will be used for drawing
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void draw(final Batch batch, final float x, final float y) {
		if (this.isTexture()) {
			batch.draw(tex, x, y);
		} 
		else if (this.isAnimation()) {
			this.stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y);

			if (stateTime > animation.getAnimationDuration())
				stateTime -= animation.getAnimationDuration();
		} 
		else {
			throw new IllegalStateException("The TextureUnit is not initialized!");
		}
	}
	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float, float, float)}
	 * @param batch batch, which will be used for drawing
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param width width
	 * @param height height
	 */
	public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
		if (this.isTexture()) {
			batch.draw(tex, x, y, width, height);
		} 
		else if (this.isAnimation()) {
			this.stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y, width, height);

			if (stateTime > animation.getAnimationDuration())
				stateTime = 0;
		} 
		else {
			throw new IllegalStateException("The TextureUnit is not initialized!");
		}
	}
	
	/**
	 * Draws the unit at (x, y).
	 * 
	 * @see {@link SpriteBatch#draw(TextureRegion, float, float, float, float)}
	 * @param batch batch, which will be used for drawing
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param width width 
	 * @param height height
	 * @param originY the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
	 * @param originX the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
	 * @param scaleX the scale of the rectangle around originX/originY in x
	 * @param scaleY the scale of the rectangle around originX/originY in y
	 * @param rotation rotation in degrees
	 */
	public void draw(final Batch batch, final float x, final float y, final float width, final float height,
			final float originX, final float originY, final float scaleX, final float scaleY, final float rotation) {
		
		if (this.isTexture()) {
			batch.draw(tex, x, y, width, height, originX, originY, scaleX, scaleY, rotation);
		} 
		else if (this.isAnimation()) {
			this.stateTime += Gdx.graphics.getDeltaTime();

			batch.draw(animation.getKeyFrame(stateTime), x, y, width, height, originX, originY, scaleX, scaleY, rotation);

			if (stateTime > animation.getAnimationDuration())
				stateTime = 0;
		} 
		else {
			throw new IllegalStateException("The TextureUnit is not initialized!");
		}
	}

	/**
	 * Returns whether the unit represents an {@link Animation}.
	 *
	 * @return true if it represents an {@link Animation}, false otherwise
	 */
	public boolean isAnimation() {
		return animation != null;
	}

	/**
	 * Returns whether the unit represents a {@link TextureRegion}. Hint: It's
	 * not possible, that {@link RenderUnit.isTexture} and
	 * {@link RenderUnit#isAnimation()} are true.
	 *
	 * @return true if it represents a {@link TextureRegion}, false otherwise
	 */
	public boolean isTexture() {
		return tex != null;
	}

}