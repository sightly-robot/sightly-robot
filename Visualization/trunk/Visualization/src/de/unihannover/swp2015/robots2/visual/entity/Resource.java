package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of resources
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Resource extends Entity {

	private int stage = 10; //default
	
	public Resource(int stage){
		this.stage=stage;
		this.texture = new Texture(Gdx.files.internal("resource"+stage+".png")); //Dateinamen ggf noch anpassen
	}
	
	public Resource(){
		this.texture = new Texture(Gdx.files.internal("resource10.png")); //Dateinamen ggf noch anpassen
	}
	
	@Override
	public void setPosition(int x, int y){ //oder gleich beim Erstellen die Position mitgeben?
		this.renderX=x;
		this.renderY=y;
	}
	
	@Override
	public void hide(){
		//TODO Optional
	}

	@Override
	public void render() {
		RobotGameHandler.spriteBatch.begin();
		RobotGameHandler.spriteBatch.draw(texture, x, y);
		RobotGameHandler.spriteBatch.end();		
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO ?
		if(event.getType() == FIELD_FOOD){
			if(stage<10){
				stage++;
				this.texture = new Texture(Gdx.files.internal("resource"+stage+".png")); //Dateinamen ggf noch anpassen
				render();
			}
		}
	}
}
