package de.unihannover.swp2015.robots2.visual.game;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.game.ui.RobotList;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

public class NewUI implements IRenderable, IUpdateable, IPreferencesObserver<PrefKey> {

	private final Stage stage;
	private final RobotList ranking;
	private final ScrollPane panel;
	private final Label title;
	
	public NewUI(IPreferences<PrefKey> pref, List<IRobot> robots, Viewport view, Batch batch, Skin skin) {
		this.stage = new Stage(view, batch);
		this.ranking = new RobotList(skin, robots);
		this.panel = new ScrollPane(ranking);
		this.title = new Label("", skin);
		this.setupStage(robots);
		
		pref.addObserver(this);
	}
	
	private void setupStage(List<IRobot> robots) {
		stage.addActor(panel);
		stage.addActor(title);
		stage.addActor(ranking);
	}

	@Override
	public void update() {
		stage.act();
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		// TODO Auto-generated method stub
	}

}
