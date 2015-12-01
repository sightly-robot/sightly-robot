package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.ColorUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * Renders the bubble for a given {@link IRobot}. 
 * 
 * @author Rico Schrage
 * @author Daphne Schössow
 */
public class Bubble extends Entity {

	/**
	 * Texture of the bubble.
	 */
	private TextureRegion bubble;
	
	/**
	 * Width of the bubble.
	 */
	private float width;
	
	/**
	 * Height of the bubble.
	 */
	private float height;
	
	/**
	 * Color of the bubble. This value is based on the model.
	 */
	private Color color;
	
	/**
	 * 
	 */
	private BitmapFont font;
	private CharSequence points;
	
	public Bubble(final IRobot model, SpriteBatch batch, IGameHandler gameHandler) {
		super(model, batch, gameHandler);
				
		this.bubble = resHandler.getRegion(ResConst.DEFAULT_BUBBLE);
		this.color = ColorUtil.fromAwtColor(model.getColor());
		//temporary
		this.color = color.set(color.r, color.g, color.b, color.a * 0.7f);
		
		this.font = new BitmapFont(true);
        this.font.setColor(1-color.r, 1-color.g, 1-color.b,color.a);
        
        this.points = model.getId().substring(0, 4)+" : "+model.getScore()+"(-)";
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

		this.renderX = model.getPosition().getX() * fieldWidth;
		this.renderY = model.getPosition().getY() * fieldHeight;
		
		this.width = fieldWidth * 0.5f;
		this.height = fieldHeight * 0.2f;
	}

	@Override
	public void render() {
		
		batch.setColor(color);
		batch.draw(bubble, renderX, renderY, width, height);
		font.draw(batch, points, renderX+5, renderY+5);
		batch.setColor(Color.WHITE);
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		final IRobot robo = (IRobot) model;
		
		switch(event.getType()) {
			case ROBOT_ADD:
				break;
			case ROBOT_POSITION:
				final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
				final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
				this.renderX = robo.getPosition().getX() * fieldWidth;
				this.renderY = robo.getPosition().getY() * fieldHeight;
				break;
			case ROBOT_SCORE:
				this.points =  robo.getId().substring(0, 4) + "  : " + robo.getScore() +  "(" + gameHandler.getRanking(robo) + ")";
				break;
			case ROBOT_STATE:
				break;
			default:
				break;
		}		
	}
	
	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		if (updatedKey.getEnum() == PrefConst.FIELD_HEIGHT_KEY || updatedKey.getEnum() == PrefConst.FIELD_WIDTH_KEY) {
			final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
			final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

			this.width = fieldWidth * 0.5f;
			this.height = fieldHeight * 0.2f;
			
			final IRobot r = (IRobot) model;
			this.renderX = r.getPosition().getX() * fieldWidth;
			this.renderY = r.getPosition().getY() * fieldHeight;
		}
	}

}
