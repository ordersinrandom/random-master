package com.jbp.randommaster.gui.common.table.block;

import java.util.Comparator;

/**
 * 
 * <code>LongSorter</code> provides the default implementation of Long cell
 * sorting.
 * 
 * @author plchung
 * 
 */
public class LongSorter extends ComparatorSorter {
	public LongSorter() {
		super(new LongComparator());
	}

	public LongSorter(int targetRow) {
		super(targetRow, new LongComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class LongComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			Long d1 = null;
			Long d2 = null;
			try {
				d1 = (Long) o1;
			} catch (Exception e1) {
				// ignore
			}

			try {
				d2 = (Long) o2;
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
