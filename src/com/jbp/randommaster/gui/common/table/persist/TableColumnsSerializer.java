package com.jbp.randommaster.gui.common.table.persist;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 
 * <code>TableColumnsWriter</code> extracts the column information from a JTable
 * object and serialize it.
 * 
 * @author plchung
 * 
 */
public class TableColumnsSerializer {

	@SuppressWarnings("rawtypes")
	public static byte[] serializeColumns(JTable table) throws IOException {
		Collection result = getColumnSettings(table);

		return serializeColumns(result);
	}

	@SuppressWarnings("rawtypes")
	public static byte[] serializeColumns(Collection columnSettings)
			throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bout);
		os.writeObject(columnSettings);
		os.flush();

		return bout.toByteArray();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection getColumnSettings(JTable table) {
		// the result is a tree set.
		Set result = new TreeSet();

		TableColumnModel tcm = table.getColumnModel();
		for (Enumeration ee = tcm.getColumns(); ee.hasMoreElements();) {
			// System.out.println("------------");
			TableColumn tc = (TableColumn) ee.nextElement();

			ColumnSetting cs = new ColumnSetting();
			cs.setHeaderValue(tc.getHeaderValue());
			cs.setIdentifier(tc.getIdentifier());
			cs.setColumnIndex(tcm.getColumnIndex(tc.getIdentifier()));
			cs.setModelIndex(tc.getModelIndex());
			cs.setWidth(tc.getWidth());
			cs.setPreferredWidth(tc.getPreferredWidth());
			result.add(cs);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static void setColumnSettings(JTable table, Collection columnSettings) {
		for (Iterator it = columnSettings.iterator(); it.hasNext();) {
			ColumnSetting cs = (ColumnSetting) it.next();

			TableColumn tc = null;

			try {
				tc = table.getColumnModel().getColumn(cs.getColumnIndex());
				tc.setModelIndex(cs.getModelIndex());
				tc.setPreferredWidth(cs.getWidth());

				tc.setHeaderValue(cs.getHeaderValue());
				tc.setIdentifier(cs.getIdentifier());
				tc.setWidth(cs.getWidth());
			} catch (Exception e1) {
				// unable to load the column, just skip.
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void deserializeColumns(JTable table, byte[] data)
			throws IOException, ClassNotFoundException {
		Collection result = deserializeColumnSettings(data);

		for (Iterator it = result.iterator(); it.hasNext();) {
			ColumnSetting cs = (ColumnSetting) it.next();
			// System.out.println("--setting --");
			// System.out.println(cs);

			TableColumn tc = null;
			boolean isNew = false;
			try {
				tc = table.getColumnModel().getColumn(cs.getColumnIndex());
				tc.setModelIndex(cs.getModelIndex());
				tc.setPreferredWidth(cs.getWidth());
			} catch (ArrayIndexOutOfBoundsException e1) {
				tc = new TableColumn(cs.getModelIndex(), cs.getWidth());
				isNew = true;
			}
			tc.setHeaderValue(cs.getHeaderValue());
			tc.setIdentifier(cs.getIdentifier());
			tc.setWidth(cs.getWidth());
			if (isNew)
				table.addColumn(tc);
		}
	}

	/**
	 * Deserialize the data and return a collection of
	 * <code>ColumnSetting</code> objects.
	 */
	@SuppressWarnings("rawtypes")
	public static Collection deserializeColumnSettings(byte[] data)
			throws IOException, ClassNotFoundException {
		ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Set result = (Set) oi.readObject();
		oi.close();
		return result;
	}
}