package de.unihannover.swp2015.robots2.visual.entity;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObserver;

public interface IEntity extends IRenderable, IModelObserver, Comparable<IEntity>, IPreferencesObserver {
	//TODO methods
	int getZIndex();
	void setZIndex(final int zIndex);
	void setPosition(final float x, final float y);
	float getPositionX();
	float getPositionY();
}
