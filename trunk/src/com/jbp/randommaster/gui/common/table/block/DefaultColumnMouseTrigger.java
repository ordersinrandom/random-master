package com.jbp.randommaster.gui.common.table.block;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class DefaultColumnMouseTrigger implements ColumnMouseTrigger {

	@SuppressWarnings("rawtypes")
	protected List listeners;
	protected JTable table;

	@SuppressWarnings("rawtypes")
	public DefaultColumnMouseTrigger(final JTable table) {
		listeners = new LinkedList();
		JTableHeader header = table.getTableHeader();
		this.table = table;

		header.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				try {
					TableColumnModel colModel = table.getColumnModel();
					int index = colModel.getColumnIndexAtX(event.getX());
					int modelIndex = colModel.getColumn(index).getModelIndex();
					fireMouseTrigger(modelIndex, event);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected void fireMouseTrigger(int modelIndex, MouseEvent e) {
		ColumnMouseTriggerEvent te = new ColumnMouseTriggerEvent(table,
				modelIndex, e);
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			ColumnMouseTriggerEventListener l = (ColumnMouseTriggerEventListener) it
					.next();
			l.columnTriggered(te);
		}
	}

	@SuppressWarnings("unchecked")
	public void addColumnMouseTriggerListener(ColumnMouseTriggerEventListener mt) {
		if (!listeners.contains(mt))
			listeners.add(mt);
	}

	public void removeColumnMouseTriggerListener(
			ColumnMouseTriggerEventListener mt) {
		listeners.remove(mt);
	}

}