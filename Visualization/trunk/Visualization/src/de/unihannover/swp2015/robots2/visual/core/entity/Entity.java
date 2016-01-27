package de.unihannover.swp2015.robots2.visual.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

/**
 * the base class of all objects displayed at the visualization
 *
 * Every object has placement coordinates, a rotation and a model, that contains
 * all information about the object, along with a list of modifiers for
 * rendering. The coordinates have a z-dimension for rendering the objects in
 * the right order.
 * 
 * @author Daphne Schössow
 * 
 * @param <G>
 *            the game handler type to which the entity should belong
 * @param <M>
 *            the model type of the entity
 */
public abstract class Entity<G extends IGameHandler, M extends IAbstractModel>
		implements IEntity, IPreferencesObserver<PrefKey>, IModelObserver {

	/** list of all modifiers created for the entity */
	protected final List<IEntityModifier> modList;

	/** list of all components registered by the entity */
	protected final List<IComponent<? extends IEntity>> componentList;

	/** data model of the entity */
	protected final M model;

	/** x-coordinate of the entity on the screen */
	protected float renderX;

	/** y-coordinate of the entity on the screen */
	protected float renderY;

	/** width of the entity on the screen */
	protected float width;

	/** height of the entity on the screen */
	protected float height;

	/** z-coordinate of the entity on the screen */
	protected int zIndex = 0;

	/** rotation of the entity in degrees */
	protected float rotation = 0;

	/** color of the entity (default is Color.WHITE) */
	protected Color color = new Color(1f, 1f, 1f, 1f);

	/** ResourceHandler which should be used to obtain resources */
	protected final IResourceHandler resHandler;

	/** preference object which contains all necessary preferences */
	protected final IPreferences<PrefKey> prefs;

	/** GameHandler which owns the entity */
	protected final G gameHandler;

	/**
	 * Construction of an entity which will make it observe the
	 * <code>model</code> and the preference-object of the
	 * <code>gameHandler</code>
	 * 
	 * @param model
	 *            data model of the entity
	 * @param gameHandler
	 *            parent
	 */
	public Entity(M model, G gameHandler) {
		this.modList = new ArrayList<>();
		this.componentList = new ArrayList<>();

		this.gameHandler = gameHandler;
		this.resHandler = gameHandler.getResourceHandler();

		this.prefs = gameHandler.getPreferences();
		this.prefs.addObserver(this);

		this.model = model;
		this.model.observe(this);
	}

	/**
	 * @return model of the entity
	 */
	public M getModel() {
		return model;
	}

	/**
	 * @return owner of the entity
	 */
	public G getHandler() {
		return gameHandler;
	}

	@Override
	public void update() {
		for (int i = modList.size() - 1; i >= 0; --i) {
			final IEntityModifier mod = modList.get(i);
			if (mod.hasFinished()) {
				mod.onFinish();
				modList.remove(i);
				break;
			}
			mod.update();
		}
		for (int i = 0; i < componentList.size(); ++i) {
			final IComponent<? extends IEntity> comp = componentList.get(i);
			comp.update();
		}
	}

	@Override
	public void draw(Batch batch) {
		batch.setColor(color);

		for (int i = 0; i < componentList.size(); ++i) {
			final IComponent<? extends IEntity> comp = componentList.get(i);
			comp.draw(batch);
		}
	}

	@Override
	public void registerModifier(IEntityModifier mod) {
		modList.add(mod);
		mod.onInit();
	}

	@Override
	public void clearModifier() {
		modList.clear();
	}

	@Override
	public void clearModifier(Class<? extends IEntityModifier> type) {
		for (int i = 0; i < modList.size(); ++i) {
			if (modList.get(i).getClass().isAssignableFrom(type)) {
				modList.remove(i);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IEntity> void registerComponent(IComponent<T> component) {
		componentList.add(component);
		component.onRegister((T) this);
	}

	@Override
	public <T extends IEntity> void unregisterComponent(IComponent<T> component) {
		componentList.remove(component);
	}

	@Override
	public void onModelUpdate(final IEvent event) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Entity.this.onManagedModelUpdate(event);

				for (int i = 0; i < componentList.size(); ++i) {
					componentList.get(i).onEvent(event);
				}
			}
		});
	}

	@Override
	public void clearReferences() {
		prefs.removeObserver(this);
		model.unobserve(this);
	}

	@Override
	public int getZIndex() {
		return zIndex;
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	@Override
	public int compareTo(IEntity o) {
		return zIndex - o.getZIndex();
	}

	@Override
	public void setPosition(float x, float y) {
		renderX = x;
		renderY = y;
	}

	@Override
	public float getPositionX() {
		return renderX;
	}

	@Override
	public float getPositionY() {
		return renderY;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float rot) {
		this.rotation = rot;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Should be called after you have added an entity to the
	 * {@link RobotGameHandler}.
	 * 
	 * As alternative you can call
	 * {@link EntityUtil#addEntitySorted(IEntity, List)d} to add a new entity.
	 * 
	 * @param entityList
	 *            list of entities to append
	 */
	public static void sortEntities(List<IEntity> entityList) {
		Collections.sort(entityList);
	}

	/**
	 * Adds a new entity to <code>renderUnits</code>.
	 * 
	 * This method uses insertion sort, so you don't have to call
	 * {@link SortUtil#sortEntities(List)}
	 * 
	 * @param entity
	 *            new entity
	 * @param entityList
	 *            list of entities to append
	 */
	public static void addEntitySorted(IEntity entity, List<IEntity> entityList) {
		for (int i = 0; i < entityList.size() + 1; ++i) {
			if (i == entityList.size()) {
				entityList.add(entity);
				break;
			}
			if (entity.getZIndex() < entityList.get(i).getZIndex()) {
				entityList.add(i, entity);
				return;
			}
		}
	}

}
