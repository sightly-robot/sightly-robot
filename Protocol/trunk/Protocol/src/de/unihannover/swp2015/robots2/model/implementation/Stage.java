package de.unihannover.swp2015.robots2.model.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

/**
 * Basic implementation of the interfaces IStageWritable, containing a two
 * dimensional list of Fields.
 * 
 * All properties are protected by suitable locking mechanisms, providing
 * multithread safety.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public class Stage extends AbstractModel implements IStage, IStageWriteable {

	/** Width of the Stage as number of Fields. */
	private int width;
	/** Height of the Stage as number of Fields */
	private int height;
	/** List of the Fields of this Stage */
	private List<List<IFieldWriteable>> fields;
	/**
	 * Lock object to prohibit multiple concurrent write operations and
	 * concurrent write and read operations on the fields list.
	 */
	private ReadWriteLock fieldsLock;
	/**
	 * A list of start positions. This list is meant to be used in the game
	 * server program and may be empty in other environments.
	 */
	private List<IPosition> startPositions;

	/**
	 * Construct a new stage of size 0x0 Fields. The fields list will be
	 * initialized but empty.
	 */
	public Stage() {
		super();

		this.fields = new ArrayList<List<IFieldWriteable>>();
		this.startPositions = new Vector<IPosition>();
		this.fieldsLock = new ReentrantReadWriteLock();
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
				for (int y = this.height - 1; y > height - 1; y--) {
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
					for (int x = this.width - 1; x > width - 1; x--) {
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
