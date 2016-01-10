package de.unihannover.swp2015.robots2.visual.game;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IDrawable;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

/**
 * End-screen of the robot game.
 * 
 * @author Rico Schrage
 */
public class UI implements IDrawable, IPreferencesObserver<PrefKey> {

	/** Robot list, which is sorted by score */
	private final List<IRobot> robots;
	
	/** ResourceHandler, which contains all necessary resources for the UI */
	private final IResourceHandler res;
	
	/** Robot preferences. */
	private final IPreferences<PrefKey> pref;
	
	/** Bounds of the screen. */
	private final Rectangle rect;
	
	/** Font of the result. */
	private final BitmapFont font;
	
	/** Font of the result. */
	private BitmapFont fontTitle;
			
	/** Texture for the background of the UI. */
	private final RenderUnit background;
	
	/** Color of the background texture. */
	private final Color color;
	
	/** Color of the background texture. */
	private final Color startBg;
	
	/** True if the ranking should be displayed. */
	private boolean display = false;
	
	/**
	 * Constructs a UI.
	 * 
	 * @param robotList list of sorted robots (by score).
	 * @param gameHandler parent
	 */
	public UI(final List<IRobot> robotList, final IGameHandler gameHandler) {
		this.robots = robotList;
		this.res = gameHandler.getResourceHandler();
		this.pref = gameHandler.getPreferences();
		this.rect = new Rectangle();
		this.font = res.getFont(ResConst.DEFAULT_FONT_BIG);
		this.font.setColor(Color.BLACK);
		this.fontTitle = res.getFont(ResConst.DEFAULT_FONT_TITLE);
		this.fontTitle.setColor(Color.BLACK);
		this.background = res.createRenderUnit(ResConst.DEFAULT_BUBBLE);
		this.color = new Color(1, 1, 1, 0.7f);
		this.startBg = new Color(1,1,1,0.3f);
		this.pref.addObserver(this);
		
		final float viewWidth = pref.getFloat(PrefKey.VIEW_WIDTH);
		final float viewHeight = pref.getFloat(PrefKey.VIEW_HEIGHT); 
		this.rect.setSize(viewWidth * GameConst.UI_WIDTH_REL, viewHeight * GameConst.UI_HEIGHT_REL);
		this.rect.setPosition(viewWidth/2 - rect.width/2, viewHeight/2 - rect.height/2);
	}
	
	@Override
	public void draw(Batch batch) {
						
		if (robots.isEmpty() || !display) {
			batch.setColor(startBg);
			background.draw(batch, rect.x, rect.y, rect.width, rect.height);
			fontTitle.draw(batch, GameConst.UI_NO_DATA,
					rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_TOP_ABS + GameConst.UI_PADDING_ABS);
		}
		else {		
			batch.setColor(color);
			background.draw(batch, rect.x, rect.y, rect.width, rect.height);
			fontTitle.draw(batch, GameConst.UI_TITLE,
					rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_PADDING_ABS);
			for (int i = 0; i < robots.size(); ++i) {
				final IRobot robot = robots.get(i);
				font.draw(batch, (i+1) + " " + robot.getId().substring(0, 8) + " (" + robot.getScore() + ")",
						rect.x + GameConst.UI_PADDING_ABS, rect.y + GameConst.UI_TOP_ABS + GameConst.UI_PADDING_ABS + i * GameConst.UI_DIFF_ABS);
			}
		}
		
		batch.setColor(Color.WHITE);
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {		
		switch(updatedKey) {
		
		case VIEW_HEIGHT:
			final float viewHeight = (float) value; 
			rect.setHeight(viewHeight * GameConst.UI_HEIGHT_REL);
			rect.setY(viewHeight/2 - rect.height/2);
			break;
		
		case VIEW_WIDTH:
			final float viewWidth = (float) value;
			rect.setWidth(viewWidth * GameConst.UI_WIDTH_REL);
			rect.setX(viewWidth/2 - rect.width/2);
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
	public void setDisplay(final boolean displayUI) {
		this.display = displayUI;
	}

}
