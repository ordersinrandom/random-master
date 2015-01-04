package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.JTable;

import javax.swing.table.TableCellRenderer;

/**
 * 
 * <code>RenderersFacade</code> is the base class of table cell renderer of MTSS
 * terminal.
 * 
 * @author plchung
 * 
 */
public class RenderersFacade implements TableCellRenderer {

	protected CellPainter cellPainter;

	@SuppressWarnings("rawtypes")
	protected Collection processes;

	/**
	 * Creates a default table cell renderer.
	 */
	public RenderersFacade() {
		this(new BasicPainter());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RenderersFacade(CellPainter p) {
		cellPainter = p;
		processes = new LinkedList();
		processes.add(new DefaultRenderProcess());
		processes.add(new RotateColorRenderProcess());
	}

	public void setCellPainter(CellPainter p) {
		cellPainter = p;
	}

	public CellPainter getPainter() {
		return cellPainter;
	}

	/**
	 * Append an additional render process.
	 */
	@SuppressWarnings("unchecked")
	public void addRenderProcess(RenderProcess p) {
		processes.add(p);
	}

	@SuppressWarnings("unchecked")
	public void setRenderProcesses(RenderProcess[] p) {
		processes.clear();
		if (p != null) {
			for (int i = 0; i < p.length; i++)
				processes.add(p[i]);
		}
	}

	/**
	 * 
	 * Returns the default table cell renderer.
	 * 
	 * @param table
	 *            the <code>JTable</code>
	 * @param value
	 *            the value to assign to the cell at <code>[row, column]</code>
	 * @param isSelected
	 *            true if cell is selected
	 * @param hasFocus
	 *            true if cell has focus
	 * @param row
	 *            the row of the cell to render
	 * @param column
	 *            the column of the cell to render
	 * @return the default table cell renderer
	 */
	@SuppressWarnings("rawtypes")
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		for (Iterator it = processes.iterator(); it.hasNext();) {
			RenderProcess p = (RenderProcess) it.next();
			p.render(cellPainter, table, value, isSelected, hasFocus, row,
					column);
		}
		return cellPainter.getRenderingLabel();

	}
}
