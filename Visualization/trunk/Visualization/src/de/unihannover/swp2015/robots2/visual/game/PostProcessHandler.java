package de.unihannover.swp2015.robots2.visual.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.Fxaa;

import de.unihannover.swp2015.robots2.visual.core.Resizable;

/**
 * Manages all post processing effects.
 * Can be used to apply a FXAA effect or/and a bloom effect.
 * <br>
 * Simple usage:<br>
 * <br>
 * <code>
 * 	handler.captureBloom();<br>
 *  //draw your stuff<br>
 *  handler.renderBloom();<br>
 * </code>
 * 
 * @author Rico Schrage
 */
public class PostProcessHandler implements Disposable, Resizable {
	
	/** Capable to render post-processing effects. Renders bloom effect. */
	private PostProcessor pp;
	
	/** Capable to render post-processing effects. Renders FXAA effect */
	private PostProcessor pp2;
		
	/** FBO, part of a workaround for a bug in the PP-library. */
	private FrameBuffer fbo;
	
	/** Describes the resulting texture of the FBO as texture region. */
	private TextureRegion reg;
	
	/** Effect, which blurs the screen when game is stopped. */
	private Bloom bloom;
	
	/** Effect, which blurs the screen when game is stopped. */
	private Fxaa fxaa;
	
	/**
	 * Manages all pp-api calls. Updates itself when resizing.
	 * 
	 * @param worldWidth width of the glViewport
	 * @param worldHeight height of the glViewport
	 */
	public PostProcessHandler(int worldWidth, int worldHeight) {
		this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, worldWidth, worldHeight, false);
		this.reg = new TextureRegion(fbo.getColorBufferTexture());
		this.pp = new PostProcessor(false, true, true);
		this.pp2 = new PostProcessor(false, true, true);
		
		this.bloom = createBloom(worldWidth, worldHeight);
		this.pp.addEffect(bloom);
		this.fxaa = createFxaa(worldWidth, worldHeight);
		this.pp2.addEffect(fxaa);
	}
	
	/**
	 * Creates the bloom effect with some default values.
	 * 
	 * @param worldWidth width of the glViewport
	 * @param worldHeight height of the glViewport
	 * @return resulting bloom effect
	 */
	private Bloom createBloom(int worldWidth, int worldHeight) {
		final Bloom bloom = new Bloom((int)(worldWidth/2f), (int)(worldHeight/2f));
		bloom.setBaseIntesity(0);
		bloom.setThreshold(0);
		bloom.setBloomSaturation(0.3f);
		bloom.setEnabled(false);
		return bloom;
	}
	
	/**
	 * Creates the FXAA effect using the glViewport size.
	 * 
	 * @param worldWidth width of the glViewport
	 * @param worldHeight height of the glViewport
	 * @return resulting FXAA effect
	 */
	private Fxaa createFxaa(int worldWidth, int worldHeight) {
		return new Fxaa(worldWidth*2, worldHeight*2);
	}

	/**
	 * Have to be called when the viewport changes. Creates new {@link PostProcessor}'s and effects.
	 * 
	 * @param view viewport, which has been updated
	 */
	public void onViewUpdate(Viewport view) {
		final int worldWidth = (int) view.getWorldWidth();
		final int worldHeight = (int) view.getWorldHeight();
		
		pp.dispose();
		pp2.dispose();
		fbo.dispose();
		
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, worldWidth, worldHeight, false);
		reg = new TextureRegion(fbo.getColorBufferTexture());
		
		pp = new PostProcessor(worldWidth, worldHeight, false, true, true);
		pp2 = new PostProcessor(worldWidth, worldHeight, false, true, true);
		onResize(view);
		
		bloom = createBloom(worldWidth, worldHeight);
		pp.addEffect(bloom);
		fxaa = createFxaa(worldWidth, worldHeight);
		pp2.addEffect(fxaa);
	}

	/**
	 * Render captured screen applying a Bloom effect (on screen).
	 */
	public void renderBloom() {
		pp.render();
	}
	
	/**
	 * Render captured screen applying a Bloom effect (to buffer).
	 */
	public void renderBloomToInternalBuffer() {
		pp.render(fbo);
	}
	
	/**
	 * Capture screen to use the captured frame for applying a Bloom effect later.
	 */
	public void captureBloom() {
		pp.capture();
	}

	/**
	 * Render captured screen applying a FXAA effect (on screen).
	 */
	public void renderFxaa() {
		pp2.render();
	}

	/**
	 * Render captured screen applying a FXAA effect (to buffer).
	 */
	public void renderFxaaToInternalBuffer() {
		pp2.render(fbo);
	}

	/**
	 * Capture screen to use the captured frame for applying a FXAA effect later.
	 */
	public void captureFxaa() {
		pp2.capture();
	}
	
	/**
	 * @return true if the bloom effect is enabled, false otherwise
	 */
	public boolean isBloomEnabled() {
		return bloom.isEnabled();
	}
	
	/**
	 * Enables/disables the bloom effect.
	 * 
	 * @param enable if true the effect will be enabled, otherwise it will be disabled
	 */
	public void setBloomEnabled(boolean enable) {
		bloom.setEnabled(enable);
	}
	
	/**
	 * @return internal frame buffer as {@link TextureRegion}.
	 */
	public TextureRegion getBufferTexture() {
		return reg;
	}

	@Override
	public void onResize(Viewport view) {
		pp.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));
		pp2.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));
	}

	@Override
	public void dispose() {
		pp.dispose();
		pp2.dispose();
		fbo.dispose();
	}

}
