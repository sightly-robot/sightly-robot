package de.unihannover.swp2015.robots2.visual.ui;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.GameConst;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObserver;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * End-screen of the robot game.
 * 
 * @author Rico Schrage
 */
public class UI implements IRenderable, IPreferencesObserver {

	/**
	 * Robot list, which is sorted by score.
	 */
	private final List<IRobot> robotList;
	
	/**
	 * ResourceHandler, which contains all necessary resources for the ui.
	 */
	private final IResourceHandler res;
	
	/**
	 * Robot preferences.
	 */
	private final IPreferences pref;
	
	/**
	 * Batch, which will be used for drawing.
	 */
	private final Batch batch;
	
	/**
	 * Bounds of the screen.
	 */
	private final Rectangle rect;
	
	/**
	 * Font of the result.
	 */
	private final BitmapFont font;
	
	/**
	 * Font of the result.
	 */
	private final BitmapFont fontTitle;
			
	/**
	 * Texture for the background of the ui.
	 */
	private final TextureRegion background;
	
	/**
	 * Color of the background texture.
	 */
	private final Color color;
	
	/**
	 * True if the ranking should be displayed.
	 */
	private boolean display = false;
	
	/**
	 * Constructs a UI.
	 * 
	 * @param robotList list of sorted robots (by score).
	 * @param gameHandler parent
	 */
	public UI(final List<IRobot> robotList, final Batch batch, final IGameHandler gameHandler) {
		this.robotList = robotList;
		this.res = gameHandler.getResourceHandler();
		this.pref = gameHandler.getPreferences();
		this.batch = batch;
		this.rect = new Rectangle();
		this.font = res.getFont(ResConst.DEFAULT_FONT_BIG);
		this.font.setColor(Color.BLACK);
		this.fontTitle = res.getFont(ResConst.DEFAULT_FONT_TITLE);
		this.fontTitle.setColor(Color.BLACK);
		this.background = res.getRegion(ResConst.DEFAULT_BUBBLE);
		this.color = new Color(1, 1, 1, 0.7f);
		
		final float viewWidth = pref.getFloat(PrefConst.VIEW_WIDTH, 0);
		final float viewHeight = pref.getFloat(PrefConst.VIEW_HEIGHT, 0); 
		this.rect.setSize(viewWidth * GameConst.UI_WIDTH_REL, viewHeight * GameConst.UI_HEIGHT_REL);
		this.rect.setPosition(viewWidth/2 - rect.width/2, viewHeight/2 - rect.height/2);
	}
	
	@Override
	public void render() {
		
		batch.begin();
		batch.setColor(color);
		
		batch.draw(background, rect.x, rect.y, rect.width, rect.height);
		
		if (robotList.size() == 0 || !display) {
			fontTitle.draw(batch, GameConst.UI_NO_DATA,
					rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_TOP_ABS + GameConst.UI_PADDING_ABS);
		}
		else {
			fontTitle.draw(batch, GameConst.UI_TITLE,
					rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_PADDING_ABS);
			for (int i = 0; i < robotList.size(); ++i) {
				final IRobot robot = robotList.get(i);
				font.draw(batch, (i+1) + " " + robot.getId().substring(0, 8) + " (" + robot.getScore() + ")",
						rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_TOP_ABS + GameConst.UI_PADDING_ABS + i * GameConst.UI_DIFF_ABS);
			}
		}
		
		batch.setColor(Color.WHITE);
		batch.end();
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		final PrefConst key = (PrefConst) updatedKey.getEnum();
		
		switch(key) {
		case VIEW_HEIGHT:
			final float viewHeight = pref.getFloat(PrefConst.VIEW_HEIGHT, 0); 
			this.rect.setHeight(viewHeight * GameConst.UI_HEIGHT_REL);
			this.rect.setY(viewHeight/2 - rect.height/2);
			break;
		case VIEW_WIDTH:
			final float viewWidth = pref.getFloat(PrefConst.VIEW_WIDTH, 0);
			this.rect.setHeight(viewWidth * GameConst.UI_WIDTH_REL);
			this.rect.setY(viewWidth/2 - rect.width/2);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Sets the display flag.
	 * 
	 * @param display flag
	 */
	public void setDisplay(final boolean display) {
		this.display = display;
	}

}
