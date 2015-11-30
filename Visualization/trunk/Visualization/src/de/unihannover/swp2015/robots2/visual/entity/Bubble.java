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

public class Bubble extends Entity {

	private TextureRegion bubble;
	
	private float width;
	private float height;
	private Color color;
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
        
        this.points = model.getName()+" : "+model.getScore()+"("+gameHandler.getRanking(model)+")";
		
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
				this.points = robo.getName() + "  : " + robo.getScore() +  "(" + gameHandler.getRanking(robo) + ")";
				break;
			case ROBOT_STATE:
				break;
			default:
				break;
		}		
	}
	
	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
	}

}
