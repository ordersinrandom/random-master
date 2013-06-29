package com.jbp.randommaster.gui.common.table.block;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 
 * <code>SortHeaderRenderer</code> is the supporting table header renderer for
 * BlockTableModel sorting.
 * 
 * @author plchung
 * 
 */
public class SortHeaderRenderer implements TableCellRenderer {

	private Icon nonSortedIcon;
	private Icon ascendingIcon;
	private Icon descendingIcon;
	
	private TableCellRenderer original;

	public SortHeaderRenderer(TableCellRenderer original) {
		this(new SortArrowIcon(SortArrowIcon.NONE), new SortArrowIcon(
				SortArrowIcon.ASCENDING), new SortArrowIcon(
				SortArrowIcon.DECENDING), original);
	}

	public SortHeaderRenderer(Icon nonSorted, Icon ascending, Icon descending, TableCellRenderer original) {

		this.nonSortedIcon = nonSorted;
		this.ascendingIcon = ascending;
		this.descendingIcon = descending;
		
		this.original = original;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		
		Component result = original.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		
		if (table.getModel() instanceof BlockTableModel) {
			
			//result.setHorizontalTextPosition(SwingConstants.LEFT);
			//result.setHorizontalAlignment(CENTER);
			
			int index = -2;
			boolean ascending = true;
			BlockTableModel tm = (BlockTableModel) table.getModel();
			int modelIndex = tm.getSortingModelIndex();
			// if there is something sorted.
			if (modelIndex >= 0) {
				TableColumnModel tcm = table.getColumnModel();
				TableColumn tc = tcm.getColumn(col);
				index = tc.getModelIndex();
			}
			ascending = tm.getSortingOrder();
			if (table != null) {
				JTableHeader header = table.getTableHeader();
				if (header != null) {
					result.setForeground(header.getForeground());
					result.setBackground(header.getBackground());
					result.setFont(header.getFont());
				}
			}
			Icon icon = ascending ? ascendingIcon : descendingIcon;
			
			
			
			if (result instanceof JLabel) {
				JLabel l = (JLabel) result;
				l.setHorizontalTextPosition(SwingConstants.LEFT);
				l.setHorizontalAlignment(SwingConstants.CENTER);			
				l.setIcon(index == modelIndex ? icon : nonSortedIcon);
				l.setText((value == null) ? "" : value.toString());
			}
			

			return result;
		} 

		return result;
	}
}
