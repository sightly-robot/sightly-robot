package de.unihannover.swp2015.robots2.application.components;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.content.TableViewCellRenderer;

public class CustomTableCellRenderer extends TableViewCellRenderer {

	private boolean selected;
	
	@Override
	public void render(Object row, int rowIndex, int columnIndex, TableView tableView, String columnName,
			boolean selected, boolean highlighted, boolean disabled) {
		// TODO Auto-generated method stub
		super.render(row, rowIndex, columnIndex, tableView, columnName, selected, highlighted, disabled);
		
		this.selected = selected;
	}

	@Override
	public void paint(Graphics2D graphics) {
		// TODO Auto-generated method stub
		
		graphics.setPaint(Color.CYAN);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		super.paint(graphics);
	}
	
	
	
}
