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

public class PostProcessHandler implements Disposable, Resizable {
	
	/**
	 * Capable to render post-processing effects. For the map.
	 */
	private PostProcessor pp;
	
	/**
	 * Capable to render post-processing effects. For the map.
	 */
	private PostProcessor pp2;
		
	
	/**
	 * FBO, part of a workaround for a bug in the PP-lib.
	 */
	private FrameBuffer fbo;
	
	/**
	 * TextureRegion, part of a workaround for a bug in the PP-lib.
	 * Describes the resulting texture of the FBO as texture region.
	 */
	private TextureRegion reg;
	
	/**
	 * Effect, which blurs the screen when game is stopped.
	 */
	private Bloom bloom;
	
	/**
	 * Effect, which blurs the screen when game is stopped.
	 */
	private Fxaa fxaa;
		
	public PostProcessHandler(int worldWidth, int worldHeight) {
		fbo = new FrameBuffer(Pixmap.Format.RGBA4444, worldWidth, worldHeight, false);
		reg = new TextureRegion(fbo.getColorBufferTexture());
		pp = new PostProcessor(false, true, true);
		pp2 = new PostProcessor(false, true, true);
		bloom = new Bloom((int)(worldWidth/2f), (int)(worldHeight/2f));
		fxaa = new Fxaa(worldWidth * 2, worldHeight * 2);
		
		setupEffects();
	}
	
	private void setupEffects() {
		bloom.setBaseIntesity(0);
		bloom.setThreshold(0);
		bloom.setBloomSaturation(0.3f);
		bloom.setEnabled(false);
		pp.addEffect(bloom);
		pp2.addEffect(fxaa);
	}

	public void onViewUpdate(Viewport view) {
		final int worldWidth = (int) view.getWorldWidth();
		final int worldHeight = (int) view.getWorldHeight();
		
		fbo.dispose();
		fbo = new FrameBuffer(Pixmap.Format.RGBA4444, worldWidth, worldHeight, false);
		reg = new TextureRegion(fbo.getColorBufferTexture());
		pp.removeEffect(bloom);
		bloom.dispose();
		bloom = new Bloom((int)(worldWidth/2f), (int)(worldHeight/2f));
		bloom.setBaseIntesity(0);
		bloom.setThreshold(0);
		bloom.setBloomSaturation(0.3f);
		bloom.setEnabled(false);
		pp.addEffect(bloom);
		pp2.removeEffect(fxaa);
		fxaa.dispose();
		fxaa = new Fxaa(worldWidth * 2, worldHeight * 2);
		pp2.addEffect(fxaa);
	}

	public void renderPP() {
		pp.render();
	}
	
	public void renderPPToInternalBuffer() {
		pp.render(fbo);
	}
	
	public void capturePP() {
		pp.capture();
	}
	
	public void renderPP2() {
		pp2.render();
	}
	
	public void renderPP2ToInternalBuffer() {
		pp2.render(fbo);
	}
	
	public void capturePP2() {
		pp2.capture();
	}
	
	public boolean isBloomEnabled() {
		return bloom.isEnabled();
	}
	
	public void setBloomEnabled(boolean enable) {
		bloom.setEnabled(enable);
	}
	
	public TextureRegion getReg() {
		return reg;
	}

	@Override
	public void onResize(Viewport view) {
		this.pp.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));
		this.pp2.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));	
	}

	@Override
	public void dispose() {
		pp.dispose();
		pp2.dispose();
		fbo.dispose();
	}

}
