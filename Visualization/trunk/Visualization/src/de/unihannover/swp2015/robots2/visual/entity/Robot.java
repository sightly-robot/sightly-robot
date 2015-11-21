package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of robots
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Robot extends Entity {
		//private boolean isVirtual; //TODO Einbauen
		private int direction=0; //0=NORTH, 1=EAST, 2=SOUTH, 3=WEST
		
		public Robot(int direction){
			this.direction=direction;
			this.texture = new Texture(Gdx.files.internal("robot.png")); //Dateinamen ggf noch anpassen
		}
		
		public Robot(){
			this.texture = new Texture(Gdx.files.internal("robot.png")); //Dateinamen ggf noch anpassen
		}
		
		private void changeDirection(int dir){ //TODO
			switch(dir){
				/*case 1:
					RobotGameHandler.spriteBatch.draw(texture, x, y, ???);//90°
					break;
				case 2:
					RobotGameHandler.spriteBatch.draw(texture, x, y, ???);//180°
					break;
				case 3:
					RobotGameHandler.spriteBatch.draw(texture, x, y, ???);//270°
					break;*/
				default:
					RobotGameHandler.spriteBatch.draw(texture, x, y);//keine Rotation	
					break;
			}
		}
		
		@Override
		public void setPosition(int x, int y){
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
			changeDirection();//RobotGameHandler.spriteBatch.draw(texture, x, y);
			RobotGameHandler.spriteBatch.end();		
		}

		@Override
		public void onModelUpdate(IEvent event) {
			// TODO Auto-generated method stub
			/*if(event == Robo_change_direction){//wie heißt das richtig? wie zieh ich die Information da raus?
				//TODO
				changeDirection();
			}*/
			if(event.getType() == ROBOT_POSITION){ //wie zieh ich die Information da raus?
				setPosition(x,y);
				render();
			}
		}

}
