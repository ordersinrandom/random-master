package com.jbp.randommaster.gui.common.table.block;

import java.util.LinkedList;
import java.util.Iterator;

import java.awt.Dimension;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * <code>AbstractBlock</code> is the basic implementation of the
 * <code>Block</code> interface.
 * 
 * @author plchung
 * 
 */
public abstract class AbstractBlock implements Block {

	@SuppressWarnings("rawtypes")
	private LinkedList listeners;
	private Object id;

	/**
	 * Create an abstract block instance.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected AbstractBlock() {
		listeners = new LinkedList();

	}

	/**
	 * Register a listener for block changes (data or size).
	 */
	@SuppressWarnings("unchecked")
	public void addBlockChangeListener(BlockChangeListener l) {
		listeners.add(l);
	}

	/**
	 * Remove a listener.
	 */
	public void removeBlockChangeListener(BlockChangeListener l) {
		listeners.remove(l);
	}

	/**
	 * Internal Utility method.
	 */
	@SuppressWarnings("rawtypes")
	private void fireBlockChangeEvent(BlockChangeEvent e) {
		LinkedList targets = (LinkedList) listeners.clone();

		switch (e.getType()) {
		case BlockChangeEvent.DATA_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				BlockChangeListener l = (BlockChangeListener) it.next();
				l.blockDataChanged(e);
			}
			break;
		case BlockChangeEvent.SIZE_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				BlockChangeListener l = (BlockChangeListener) it.next();
				l.blockSizeChanged(e);
			}
			break;
		default:
			throw new IllegalArgumentException("unknown BlockChangeEvent type: " + e.getType());
		}
	}

	/**
	 * Internal Utility method.
	 */
	protected void notifySizeChanges(Dimension newSize) {
		BlockChangeEvent e = new BlockChangeEvent(this, newSize);
		fireBlockChangeEvent(e);
	}

	/**
	 * Internal Utility method.
	 */
	protected void notifyDataChanges(int startX, int endX, int startY, int endY) {
		BlockChangeEvent e = new BlockChangeEvent(this, startX, endX, startY, endY);
		fireBlockChangeEvent(e);
	}

	/**
	 * Set the block ID of this block (USE INTERNALLY BY
	 * <code>BlockTableModel</code>)
	 */
	public void setBlockId(Object id) {
		this.id = id;
	}

	/**
	 * Get the block ID of this block.
	 */
	public Object getBlockId() {
		return id;
	}

	/**
	 * Get the number of columns of this block.
	 */
	public abstract int getWidth();

	/**
	 * Get the number of rows of this block.
	 */
	public abstract int getHeight();

	/**
	 * Get the value in this block.
	 * 
	 * @param x
	 *            The x position relative to top left corner.
	 * @param y
	 *            The y position relative to top left corner.
	 */
	public abstract Object getValueAt(int x, int y);

	/**
	 * Set the value in this block. Empty default implementation.
	 */
	public void setValueAt(Object v, int x, int y) {
		// empty implementation
	}

	/**
	 * Check if the block data is editable at (x,y) relative to block upper left
	 * corner.
	 * 
	 * @return false by default, subclass to override.
	 */
	public boolean isEditableAt(int x, int y) {
		return false;
	}

	/**
	 * Get the cell editor at (x,y) relative to block upper left corner.
	 * 
	 * @return Default implementation returns null.
	 */
	public TableCellEditor getCellEditorAt(int x, int y) {
		return null;
	}

	/**
	 * Get the table cell renderer at (x,y) relative to block upper left corner.
	 * 
	 * @return Default implementation returns null.
	 */
	public TableCellRenderer getCellRendererAt(int x, int y) {
		return null;
	}

}