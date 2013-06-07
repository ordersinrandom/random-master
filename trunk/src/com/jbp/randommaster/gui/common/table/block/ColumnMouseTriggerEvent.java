package com.jbp.randommaster.gui.common.table.block;

import java.util.EventObject;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

public class ColumnMouseTriggerEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1857838840193031692L;
	private int modelIndex;
	private MouseEvent event;

	public ColumnMouseTriggerEvent(JTable src, int modelIndex, MouseEvent e) {
		super(src);
		this.modelIndex = modelIndex;
		event = e;
	}

	public JTable getTable() {
		return (JTable) super.getSource();
	}

	public int getModelIndex() {
		return modelIndex;
	}

	public MouseEvent getMouseEvent() {
		return event;
	}
}