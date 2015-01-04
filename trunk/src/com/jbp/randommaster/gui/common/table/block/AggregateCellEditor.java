package com.jbp.randommaster.gui.common.table.block;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.EventObject;
import java.util.Iterator;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.jbp.randommaster.gui.common.table.block.BlockTableModel;
import com.jbp.randommaster.gui.common.table.block.Block;

/**
 * 
 * <code>AggregateCellEditor</code> is a special table cell editor for
 * <code>JTable</code> that using <code>BlockTableModel</code>.
 * 
 * Everytime when the <code>getTableCellEditorComponent</code> method is
 * invoked, it searches the <code>BlockTableModel</code> of the given
 * <code>JTable</code> instance and locates the right editor in the block.
 * 
 * @see com.jandp.ui.common.table.block.Block#getCellEditorAt
 * 
 * @author plchung
 * 
 */
public class AggregateCellEditor implements TableCellEditor, CellEditorListener {

	// the collection of listeners added to this editor.
	@SuppressWarnings("rawtypes")
	private Collection listeners;
	// the current table cell editor in use.
	private TableCellEditor currentEditor;

	/**
	 * Create a new instance of <code>AggregateCellEditor</code>.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public AggregateCellEditor() {
		listeners = new LinkedHashSet();
	}

	/**
	 * The static API to use this cell editor. Just call this method and the
	 * table automatically uses the editors associated in the blocks.
	 * 
	 * @see com.jandp.ui.common.table.block.Block#getCellEditorAt
	 * 
	 */
	public static void applyBlockEditors(JTable table) {
		/*
		 * int count=tcm.getColumnCount(); AggregateCellEditor e=new
		 * AggregateCellEditor(); for (int i=0;i<count;i++)
		 * tcm.getColumn(i).setCellEditor(e);
		 */
		table.setDefaultEditor(Object.class, new AggregateCellEditor());
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		// convert the column (view index) to model index
		TableColumn tc = table.getColumnModel().getColumn(column);
		if (tc == null)
			return null;

		currentEditor = findEditor(table, row, tc.getModelIndex());
		if (currentEditor != null) {
			currentEditor.addCellEditorListener(this);
			return currentEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);
		} else
			return null;
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	@SuppressWarnings("unchecked")
	public void addCellEditorListener(CellEditorListener l) {
		listeners.add(l);
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public void cancelCellEditing() {
		if (currentEditor != null) {
			currentEditor.cancelCellEditing();
			currentEditor.removeCellEditorListener(this);
			currentEditor = null;
		}
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public Object getCellEditorValue() {
		if (currentEditor != null)
			return currentEditor.getCellEditorValue();
		else
			return null;
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public boolean isCellEditable(EventObject anEvent) {
		if (currentEditor != null)
			return currentEditor.isCellEditable(anEvent);
		else
			return true;
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		if (currentEditor != null)
			return currentEditor.shouldSelectCell(anEvent);
		else
			return true;
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public boolean stopCellEditing() {
		if (currentEditor != null) {
			boolean r = currentEditor.stopCellEditing();
			if (r == true) {
				currentEditor.removeCellEditorListener(this);
				currentEditor = null;
			}
			return r;
		} else
			return true;
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public void editingStopped(ChangeEvent e) {
		fireStop(e);
	}

	/**
	 * Implementation of <code>TableCellEditor</code>.
	 */
	public void editingCanceled(ChangeEvent e) {
		fireCancel(e);
	}

	/**
	 * Utility method to fire stop editing event.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private void fireStop(ChangeEvent e) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			CellEditorListener l = (CellEditorListener) it.next();
			l.editingStopped(e);
		}
	}

	/**
	 * Utility method to fire cancel editing event.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private void fireCancel(ChangeEvent e) {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			CellEditorListener l = (CellEditorListener) it.next();
			l.editingCanceled(e);
		}
	}

	/**
	 * Find the cell editor.
	 * 
	 * @param table
	 *            The table object.
	 * @param row
	 *            Row in table.
	 * @param column
	 *            The column model index.
	 * @return The cell editor of the block, or null if it is not found.
	 */
	private TableCellEditor findEditor(JTable table, int row, int column) {

		BlockTableModel tableModel = (BlockTableModel) table.getModel();
		Block b = tableModel.findBlock(row);

		if (b == null)
			return null;

		int[] blockRows = tableModel.findBlockRows(b);
		int relativeRow = row - blockRows[0];

		return b.getCellEditorAt(column, relativeRow);
	}

}