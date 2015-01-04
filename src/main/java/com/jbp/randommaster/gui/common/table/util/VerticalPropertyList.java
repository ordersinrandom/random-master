package com.jbp.randommaster.gui.common.table.util;

import java.util.Collection;
import java.util.LinkedList;
import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * <code>VerticalPropertyList</code> is used in conjunction with
 * <code>BeanListTableModel</code>. It wraps up a single bean into two columns:
 * "property" and "value", and returns a collection of "pseudo" beans which have
 * exactly two properties "property" and "value" so that a single bean can be
 * viewed in "vertical" style using the <code>BeanListTableModel</code>.
 * 
 * @author plchung
 * 
 */
public class VerticalPropertyList {

	/**
	 * Wraps a single bean as a collection of "property-value" beans.
	 * 
	 * @param bean
	 *            The bean to wrap up.
	 * @return A collection of beans describing the property name and the value.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection wrapBean(Object bean) {
		LinkedList result = new LinkedList();

		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(bean);
		for (int i = 0; i < pd.length; i++) {
			String name = pd[i].getName();
			Object value = null;
			try {
				value = PropertyUtils.getProperty(bean, name);
			} catch (Exception e1) {
				value = e1.getMessage();
			}
			result.add(new PropertyValue(name, value));
		}
		return result;
	}

	/**
	 * 
	 * <code>PropertyValue</code> is the "wrapped" bean which consists of two
	 * properties: "property" and "value".
	 * 
	 * @author plchung
	 * 
	 */
	public static class PropertyValue {
		private String propertyName;
		private Object value;

		public PropertyValue(String propertyName, Object value) {
			this.propertyName = propertyName;
			this.value = value;
		}

		public String getProperty() {
			return propertyName;
		}

		public Object getValue() {
			return value;
		}
	}
}