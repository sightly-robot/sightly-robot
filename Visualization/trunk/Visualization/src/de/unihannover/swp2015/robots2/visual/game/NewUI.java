package de.unihannover.swp2015.robots2.visual.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.core.IResizable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;
import de.unihannover.swp2015.robots2.visual.game.ui.RobotList;

public class NewUI implements IRenderable, IUpdateable, Disposable, IResizable {

	private final Stage stage;
	private final RobotList ranking;
	private final Container<RobotList> panel;
	private final Label title;
	
	private boolean visible;
	
	public NewUI(List<IRobot> robots, Viewport view, Batch batch, Skin skin) {
		this.stage = new Stage(view, batch);
		this.ranking = new RobotList(skin, robots);
		this.title = new Label("Supertitel", skin);
		this.panel = new Container<>(ranking);
		this.setupStage(robots, skin);
		this.onResize(view);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void setupStage(List<IRobot> robots, Skin skin) {
		panel.setPosition(200,200);
		panel.setColor(GameConst.UI_COLOR);
		panel.setBackground(skin.getDrawable("default-rect"));
		stage.addActor(panel);
	}
	
	public void onAddingRobot() {
		panel.invalidate();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update() {
		if (visible) {
			stage.act();
		}
	}

	@Override
	public void render() {
		if (visible) {
			stage.draw();
			stage.getBatch().setColor(Color.WHITE);
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void onResize(Viewport view) {
		panel.setSize(view.getWorldWidth()/2, view.getWorldHeight()/2);
		panel.setPosition(view.getWorldWidth()/2-panel.getWidth()/2, view.getWorldHeight()/2-panel.getHeight()/2);
		panel.invalidate();
		//ranking.setPadding(view.getWorldWidth()/30);
		ranking.onResize(view);
	}

}
