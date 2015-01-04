package com.jbp.randommaster.gui.common.table.util;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import java.beans.PropertyDescriptor;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * <code>BeanListTableModel</code> is a debugging purpose table model. With the
 * given reference collection of beans, it constructs a table model consisting
 * all property names and their corresponding values, which can be immediately
 * shown by a JTable instance.
 * 
 * @author plchung
 * 
 */
public class BeanListTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3581390938757635779L;
	@SuppressWarnings("rawtypes")
	private List beans;
	@SuppressWarnings("rawtypes")
	private Collection ref;
	private boolean showClass;
	private PropertyDescriptor[] pd;

	/**
	 * Create a new instance of <code>BeanListTableModel</code>.
	 * 
	 * @param beans
	 *            A collection of beans referencing to. It is read when the
	 *            <code>refresh()</code> method is called.
	 */
	@SuppressWarnings("rawtypes")
	public BeanListTableModel(Collection beans, boolean showClass) {
		ref = beans;
		this.showClass = showClass;
		refresh();
	}

	@SuppressWarnings("rawtypes")
	public BeanListTableModel(Collection beans) {
		this(beans, false);
	}

	/**
	 * Refresh the this model and the reference to the beans collection is
	 * re-read.
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refresh() {
		this.beans = new LinkedList();
		this.beans.addAll(ref);

		if (!beans.isEmpty()) {
			if (showClass)
				pd = PropertyUtils.getPropertyDescriptors(this.beans.get(0));
			else {
				PropertyDescriptor[] p = PropertyUtils
						.getPropertyDescriptors(this.beans.get(0));
				pd = new PropertyDescriptor[p.length - 1];
				int current = 0;
				for (int i = 0; i < p.length; i++) {
					if (!p[i].getName().equals("class")) {
						pd[current] = p[i];
						current++;
					}
				}
			}
		}

		super.fireTableDataChanged();
	}

	public int getRowCount() {
		return beans.size();
	}

	@SuppressWarnings("unused")
	public int getColumnCount() {
		if (beans.isEmpty())
			return 0;
		else {
			Object bean = beans.get(0);
			if (pd == null)
				return 0;
			else
				return pd.length;
		}
	}

	public String getColumnName(int column) {
		return pd[column].getName();
	}

	public Object getValueAt(int row, int column) {
		Object bean = beans.get(row);
		PropertyDescriptor desc = pd[column];
		try {
			return PropertyUtils.getProperty(bean, desc.getName());
		} catch (Exception e1) {
			return e1.getMessage();
		}
	}
}
