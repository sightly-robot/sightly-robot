package de.unihannover.swp2015.robots2.model.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.*;

public class Stage extends AbstractModel implements IStage, IStageWriteable {

	private int width;
	private int height;
	private List<List<IFieldWriteable>> fields;
	private ReadWriteLock fieldsLock;
	private List<IPosition> startPositions;

	public Stage() {
		super();

		this.fields = new ArrayList<List<IFieldWriteable>>();
		this.startPositions = new Vector<IPosition>();
	}

	@Override
	public IFieldWriteable getFieldWriteable(int x, int y) {
		this.fieldsLock.readLock().lock();

		try {
			return this.fields.get(y).get(x);
		} finally {
			this.fieldsLock.readLock().unlock();
		}
	}

	@Override
	public int getWidth() {
		this.fieldsLock.readLock().lock();

		try {
			return this.width;
		} finally {
			this.fieldsLock.readLock().unlock();
		}
	}

	@Override
	public int getHeight() {
		this.fieldsLock.readLock().lock();

		try {
			return this.height;
		} finally {
			this.fieldsLock.readLock().unlock();
		}
	}

	@Override
	public IField getField(int x, int y) {
		this.fieldsLock.readLock().lock();

		try {
			return this.fields.get(y).get(x);
		} finally {
			this.fieldsLock.readLock().unlock();
		}
	}

	@Override
	public void clearStartPositions() {
		this.startPositions.clear();
	}

	@Override
	public void addStartPosition(int x, int y, Orientation orientation) {
		this.startPositions.add(new Position(x, y, orientation));
	}

	@Override
	public List<IPosition> getStartPositions() {
		return this.startPositions;
	}

	@Override
	public void changeSize(int width, int height) {

		// Prevent concurrent read from field lists
		this.fieldsLock.writeLock().lock();

		try {
			// Delete obsolete rows
			if (height < this.height) {
				for (int y = this.height - 1; y >= height - 1; y--) {
					this.fields.remove(y);
				}
				// ... or add new rows
			} else if (height > this.height) {
				for (int y = this.height; y < height; y++) {
					ArrayList<IFieldWriteable> newRow = new ArrayList<IFieldWriteable>();
					for (int x = 0; x < width; x++) {
						newRow.add(new Field(x, y));
					}
					this.fields.add(newRow);
				}
			}

			// Resize all rows still existing from before
			for (int y = 0; y < Math.min(this.height, height); y++) {
				if (width < this.width) {
					for (int x = this.height - 1; x >= height - 1; x--) {
						this.fields.get(y).remove(x);
					}
				} else if (width > this.width) {
					for (int x = this.width; x < width; x++) {
						this.fields.get(y).add(new Field(x, y));
					}
				}
			}

			this.width = width;
			this.height = height;

		} finally {
			this.fieldsLock.writeLock().unlock();
		}
	}

}
