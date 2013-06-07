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
public class SortHeaderRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7950893296504716036L;

	private Icon nonSortedIcon;
	private Icon ascendingIcon;
	private Icon descendingIcon;

	public SortHeaderRenderer() {
		this(new SortArrowIcon(SortArrowIcon.NONE), new SortArrowIcon(
				SortArrowIcon.ASCENDING), new SortArrowIcon(
				SortArrowIcon.DECENDING));
	}

	public SortHeaderRenderer(Icon nonSorted, Icon ascending, Icon descending) {
		setHorizontalTextPosition(LEFT);
		setHorizontalAlignment(CENTER);
		this.nonSortedIcon = nonSorted;
		this.ascendingIcon = ascending;
		this.descendingIcon = descending;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		if (table.getModel() instanceof BlockTableModel) {
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
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}
			Icon icon = ascending ? ascendingIcon : descendingIcon;
			setIcon(index == modelIndex ? icon : nonSortedIcon);
			setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return this;
		} else
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, col);
	}
}
