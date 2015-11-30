package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.ColorUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

public class Bubble extends Entity {

	private TextureRegion bubble;
	private IRobot model;
	
	private float width;
	private float height;
	private Color color;
	
	public Bubble(final IRobot model, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
				
		this.model = model;
		this.model.observe(this);
		this.bubble = resHandler.getRegion(ResConst.DEFAULT_BUBBLE);
		this.color = ColorUtil.fromAwtColor(model.getColor());
		//temporary
		this.color = color.set(color.r, color.g, color.b, color.a * 0.7f);
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

		this.renderX = model.getPosition().getX() * fieldWidth;
		this.renderY = model.getPosition().getY() * fieldHeight;
		
		this.width = fieldWidth * 0.4f;
		this.height = fieldHeight * 0.4f;
	}

	@Override
	public void render() {
		
		batch.setColor(color);
		batch.draw(bubble, renderX, renderY, width, height);
		batch.setColor(Color.WHITE);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch(event.getType()) {
			case ROBOT_ADD:
				break;
			case ROBOT_POSITION:
				final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
				final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
				this.renderX = model.getPosition().getX() * fieldWidth;
				this.renderY = model.getPosition().getY() * fieldHeight;
				break;
			case ROBOT_SCORE:
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
