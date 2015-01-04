package com.jbp.randommaster.gui.common.table.block;

import java.util.Comparator;

/**
 * 
 * <code>IntegerSorter</code> provides the default implementation of Integer
 * cell sorting.
 * 
 * @author plchung
 * 
 */
public class IntegerSorter extends ComparatorSorter {
	public IntegerSorter() {
		super(new IntegerComparator());
	}

	public IntegerSorter(int targetRow) {
		super(targetRow, new IntegerComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class IntegerComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			Integer d1 = null;
			try {
				d1 = (Integer) o1;
			} catch (Exception e1) {
				// ignore
			}

			Integer d2 = null;
			try {
				d2 = (Integer) o2;
			} catch (Exception e2) {
				// ignore
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
