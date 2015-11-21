package de.unihannover.swp2015.robots2.visual.entity;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of 
 * 
 * @version 0.0
 * @author Daphne Schössow
 */
public class Map extends Entity {

	@Override
	public void setPosition(int x, int y) {
		this.renderX=x;
		this.renderY=y;
	}

	@Override
	public void hide() {
		// TODO Optional
	}
	
	@Override
	public void render() {
		RobotGameHandler.spriteBatch.begin();
		RobotGameHandler.spriteBatch.draw(texture, x, y);
		RobotGameHandler.spriteBatch.end();		
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
	}

}
