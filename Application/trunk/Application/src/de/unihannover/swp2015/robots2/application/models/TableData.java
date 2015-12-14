package de.unihannover.swp2015.robots2.application.models;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

/**
 * Model for participant list.
 * @author Tim Ebbeke
 */
public class TableData {
	private List <TableElement> data;
	
	public TableData() {
		super();
		data = new ArrayList <>();
	}

	/**
	 * Get the table in a whole.
	 * @return The table data.
	 */
	public List<TableElement> getData() {
		return data;
	}

	/**
	 * Set the tables data.
	 * @param data
	 */
	public void setData(List<TableElement> data) {
		this.data = data;
	}
	
	/**
	 * Returns the robot by index. This index is equivalent to the view index.
	 * @param index Index of the table element.
	 * @return A table element.
	 */
	public TableElement getByIndex(int index) {
		return data.get(index);
	}
	
	/**
	 * Clears all robots from the list.
	 */
	public void clear() {
		data.clear();
	}
	
	/**
	 * Add a single item to the list at the end of it.
	 * @param elem A table element.
	 */
	public void add(TableElement elem) {
		data.add(elem);
	}
}
