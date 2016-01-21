package de.unihannover.swp2015.robots2.visual.resource.texture;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Decorator for instances of {@link RenderUnit}. Add the possibility to set
 * transformations.
 * 
 * @author Rico Schrage
 */
public class TransformedRenderUnit {

	private RenderUnit renderUnit;
	private float x;
	private float y;
	private float width;
	private float height;
	private float rotation;
	private float originX;
	private float originY;
	private float scaleX;
	private float scaleY;

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit, which describes what have to be rendered
	 */
	public TransformedRenderUnit(RenderUnit renderUnit) {
		this(renderUnit, 0, 0, 0, 0);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit, which describes what have to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y, float width, float height) {
		this(renderUnit, x, y, width, height, 0, 0, 0);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit, which describes what have to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param originX
	 *            X-coordinate you want to rotate around
	 * @param originY
	 *            Y-coordinate you want to rotate around
	 * @param rotation
	 *            rotation in degrees
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y, float width, float height, float originX,
			float originY, float rotation) {
		this(renderUnit, x, y, width, height, originX, originY, rotation, 1, 1);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit, which describes what have to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param originX
	 *            X-coordinate you want to rotate around
	 * @param originY
	 *            Y-coordinate you want to rotate around
	 * @param rotation
	 *            rotation in degrees
	 * @param scaleX
	 *            scale x direction
	 * @param scaleY
	 *            scale y direction
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y, float width, float height, float originX,
			float originY, float rotation, float scaleX, float scaleY) {
		this.renderUnit = renderUnit;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.originX = originX;
		this.originY = originY;
		this.rotation = rotation;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	/**
	 * Draw the renderUnit with the stored transformations.
	 * 
	 * @param batch
	 *            {@link Batch}
	 */
	public void draw(Batch batch) {
		renderUnit.draw(batch, x, y, width, height, originX, originY, scaleX, scaleY, rotation);
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the originX
	 */
	public float getOriginX() {
		return originX;
	}

	/**
	 * @param originX
	 *            the originX to set
	 */
	public void setOriginX(float originX) {
		this.originX = originX;
	}

	/**
	 * @return the originY
	 */
	public float getOriginY() {
		return originY;
	}

	/**
	 * @param originY
	 *            the originY to set
	 */
	public void setOriginY(float originY) {
		this.originY = originY;
	}

	/**
	 * @return the scaleX
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * @param scaleX
	 *            the scaleX to set
	 */
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	/**
	 * @return the scaleY
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	/**
	 * @return the renderUnit
	 */
	public RenderUnit getRenderUnit() {
		return renderUnit;
	}

	/**
	 * @param renderUnit
	 *            the renderUnit to set
	 */
	public void setRenderUnit(RenderUnit renderUnit) {
		this.renderUnit = renderUnit;
	}

}
