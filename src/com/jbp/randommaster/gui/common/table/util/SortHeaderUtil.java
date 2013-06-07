package com.jbp.randommaster.gui.common.table.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTable;

import com.jbp.randommaster.gui.common.table.block.BlockSorter;
import com.jbp.randommaster.gui.common.table.block.BlockTableModel;
import com.jbp.randommaster.gui.common.table.block.DefaultColumnMouseTrigger;
import com.jbp.randommaster.gui.common.table.block.SortHeaderRenderer;

public class SortHeaderUtil {

	/**
	 * Setup the table sorter headers.
	 * 
	 * @param table
	 * @param model
	 * @param sorterColumnsMap
	 *            Mapping from BlockSorter instance to an int[] of column model
	 *            index.
	 * @param defaultSorter
	 *            The default sorter if columns not specified in
	 *            sorterColumnsMap, can be null.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setupHeader(JTable table, BlockTableModel model,
			Map sorterColumnsMap, BlockSorter defaultSorter) {
		// set the sorters
		DefaultColumnMouseTrigger trigger = new DefaultColumnMouseTrigger(table);
		model.setColumnMouseTrigger(trigger);

		HashSet handledColumns = new HashSet();
		for (Iterator it = sorterColumnsMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry en = (Map.Entry) it.next();
			BlockSorter sorter = (BlockSorter) en.getKey();
			int[] column = (int[]) en.getValue();

			for (int i = 0; i < column.length; i++) {
				model.setSorter(column[i], sorter);
				handledColumns.add(new Integer(column[i]));
			}
		}

		if (defaultSorter != null) {
			int columnCount = model.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				if (!handledColumns.contains(new Integer(i))) {
					model.setSorter(i, defaultSorter);
				}
			}
		}

		table.getTableHeader().setDefaultRenderer(new SortHeaderRenderer());

	}

}