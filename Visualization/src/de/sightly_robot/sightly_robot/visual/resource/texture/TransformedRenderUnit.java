package de.sightly_robot.sightly_robot.visual.resource.texture;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Decorator for instances of {@link RenderUnit} with the possibility to set
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
	 *            unit which describes what has to be rendered
	 */
	public TransformedRenderUnit(RenderUnit renderUnit) {
		this(renderUnit, 0, 0, 0, 0);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit which describes what has to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y,
			float width, float height) {
		this(renderUnit, x, y, width, height, 0, 0, 0);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit which describes what has to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param originX
	 *            x-coordinate of rotation center
	 * @param originY
	 *            y-coordinate of rotation center
	 * @param rotation
	 *            rotation in degrees
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y,
			float width, float height, float originX, float originY,
			float rotation) {
		this(renderUnit, x, y, width, height, originX, originY, rotation, 1, 1);
	}

	/**
	 * Constructs a transformed RenderUnit with the given transformations.
	 * 
	 * @param renderUnit
	 *            unit which describes what has to be rendered
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param originX
	 *            x-coordinate of rotation and scaling center
	 * @param originY
	 *            y-coordinate of rotation and scaling center
	 * @param rotation
	 *            rotation in degrees
	 * @param scaleX
	 *            scale in x direction
	 * @param scaleY
	 *            scale in y direction
	 */
	public TransformedRenderUnit(RenderUnit renderUnit, float x, float y,
			float width, float height, float originX, float originY,
			float rotation, float scaleX, float scaleY) {
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
		renderUnit.draw(batch, x, y, width, height, originX, originY, scaleX,
				scaleY, rotation);
	}

	/**
	 * @return current x position
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            desired x position
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return current y position
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            desired y position
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return current width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            desired width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return current height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            desired height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return current rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            desired rotation
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return current x origin
	 */
	public float getOriginX() {
		return originX;
	}

	/**
	 * @param originX
	 *            desired x origin
	 */
	public void setOriginX(float originX) {
		this.originX = originX;
	}

	/**
	 * @return current y origin
	 */
	public float getOriginY() {
		return originY;
	}

	/**
	 * @param originY
	 *            desired y origin
	 */
	public void setOriginY(float originY) {
		this.originY = originY;
	}

	/**
	 * @return current x scale
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * @param scaleX
	 *            desired x scale
	 */
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	/**
	 * @return current y scale
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * @param scaleY
	 *            desired y scale
	 */
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	/**
	 * @return current render unit
	 */
	public RenderUnit getRenderUnit() {
		return renderUnit;
	}

	/**
	 * @param renderUnit
	 *            desired render unit
	 */
	public void setRenderUnit(RenderUnit renderUnit) {
		this.renderUnit = renderUnit;
	}

}
