package com.jbp.randommaster.gui.common.table.block;

import java.util.Comparator;

/**
 * 
 * <code>DoubleSorter</code> provides the default implementation of Double cell
 * sorting.
 * 
 * @author plchung
 * 
 */
public class DoubleSorter extends ComparatorSorter {
	public DoubleSorter() {
		super(new DoubleComparator());
	}

	public DoubleSorter(int targetRow) {
		super(targetRow, new DoubleComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class DoubleComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			Double d1 = null;
			Double d2 = null;
			try {
				d1 = (Double) o1;
			} catch (Exception e1) {
				// ignore
			}

			try {
				d2 = (Double) o2;
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
