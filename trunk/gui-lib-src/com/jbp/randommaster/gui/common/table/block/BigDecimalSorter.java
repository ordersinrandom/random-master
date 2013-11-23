package com.jbp.randommaster.gui.common.table.block;

import java.math.BigDecimal;

import java.util.Comparator;

/**
 * 
 * <code>BigDecimalSorter</code> provides the default implementation of
 * BigDecimal cell sorting.
 * 
 * @author plchung
 * 
 */
public class BigDecimalSorter extends ComparatorSorter {
	public BigDecimalSorter() {
		super(new BigDecimalComparator());
	}

	public BigDecimalSorter(int targetRow) {
		super(targetRow, new BigDecimalComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class BigDecimalComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			BigDecimal d1 = null;
			BigDecimal d2 = null;
			try {
				d1 = (BigDecimal) o1;
			} catch (Exception e1) {
				// ignore
			}

			try {
				d2 = (BigDecimal) o2;
			} catch (Exception e2) {
				// ignore.
			}

			if (d1 == null && d2 == null)
				return 0;
			else if (d1 != null && d2 == null)
				return -1;
			else if (d1 == null && d2 != null)
				return 1;
			else
				return d1.compareTo(d2);
		}
	}
}
