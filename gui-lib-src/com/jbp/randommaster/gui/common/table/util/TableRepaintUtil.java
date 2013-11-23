package com.jbp.randommaster.gui.common.table.util;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;

/**
 * 
 * <code>TableRepaintUtil</code> provides methods to perform certain JTable
 * repaint operations conveniently.
 * 
 * @author plchung
 * 
 */
public class TableRepaintUtil {

	/**
	 * Repaint the <code>JTable</code> header by reloading the column names in
	 * the <code>TableModel</code>.
	 */
	@SuppressWarnings("rawtypes")
	public static void repaintHeader(JTable table) {
		TableModel tm = table.getModel();

		for (Enumeration ee = table.getColumnModel().getColumns(); ee
				.hasMoreElements();) {
			TableColumn col = (TableColumn) ee.nextElement();
			col.setHeaderValue(tm.getColumnName(col.getModelIndex()));
		}
		table.getTableHeader().resizeAndRepaint();
	}

	/**
	 * Auto adjust the jtable header by reading its column header and values.
	 * The table columns will be set with appropriate preferred width.
	 * 
	 * @param table
	 *            The table to adjust header.
	 */
	/*
	 * public static void autoFitHeader(JTable table) { autoFitHeader(table,
	 * 15); }
	 */

	/**
	 * Auto adjust the jtable header by reading its column header and values.
	 * The table columns will be set with appropriate preferred width.
	 * 
	 * @param table
	 *            The table to adjust header.
	 * @param margnSetting
	 *            The int array specifying the margin corresponding to the model
	 *            index.
	 */
	/*
	 * public static void autoFitHeader(JTable table, int[] marginSetting) {
	 * JTableHeader header=table.getTableHeader(); TableColumnModel
	 * cm=header.getColumnModel(); int count=cm.getColumnCount();
	 * 
	 * if (marginSetting==null || marginSetting.length!=count) throw new
	 * IllegalArgumentException
	 * ("marginSetting size must meet the number of total columns in table");
	 * 
	 * int rowCount=table.getRowCount();
	 * 
	 * Font f=header.getFont(); FontMetrics fm=header.getFontMetrics(f); for
	 * (int i=0;i<count;i++) { TableColumn tc=cm.getColumn(i); LinkedList
	 * check=new LinkedList(); check.add((String) tc.getHeaderValue()); for (int
	 * j=0;j<rowCount;j++) { Object v=table.getValueAt(j,i); if (v!=null) { //
	 * PENDING: need to handle item with specialized renderer.
	 * check.add(v.toString()); } }
	 * 
	 * int finalWidth=10; for (Iterator it=check.iterator();it.hasNext();) {
	 * String value=(String) it.next(); int w=(int)
	 * fm.getStringBounds(value==null? " ": value,
	 * header.getGraphics()).getWidth(); if (w>finalWidth) finalWidth=w; }
	 * //System.out.println("TableRepaintUtil, finalWidth="+finalWidth);
	 * tc.setPreferredWidth(finalWidth+marginSetting[tc.getModelIndex()]); } }
	 */

	/**
	 * Auto adjust the jtable header by reading its column header and values.
	 * The table columns will be set with appropriate preferred width.
	 * 
	 * @param table
	 *            The table to adjust header.
	 * @param margin
	 *            The margin of the table header.
	 */
	/*
	 * public static void autoFitHeader(JTable table, int margin) { int
	 * c=table.getColumnCount(); int[] arg=new int [c]; for (int i=0;i<c;i++)
	 * arg[i]=margin; autoFitHeader(table, arg); }
	 */

}