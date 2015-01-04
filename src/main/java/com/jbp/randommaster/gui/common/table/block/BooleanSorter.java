package com.jbp.randommaster.gui.common.table.block;

import java.util.Comparator;

/**
 * 
 * <code>BooleanSorter</code> provides the default implementation of Boolean
 * cell sorting.
 * 
 * @author plchung
 * 
 */
public class BooleanSorter extends ComparatorSorter {
	public BooleanSorter() {
		super(new BooleanComparator());
	}

	public BooleanSorter(int targetRow) {
		super(targetRow, new BooleanComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class BooleanComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			Boolean d1 = null;
			Boolean d2 = null;
			try {
				d1 = (Boolean) o1;
			} catch (Exception e1) {
				// ignore
			}

			try {
				d2 = (Boolean) o2;
			} catch (Exception e2) {
				// ignore.
			}

			if (d1 == null && d2 == null)
				return 0;
			else if (d1 != null && d2 == null)
				return 1;
			else if (d1 == null && d2 != null)
				return -1;
			else {
				boolean b1 = d1.booleanValue();
				boolean b2 = d2.booleanValue();

				if (b1 == false && b2 == true)
					return -1;
				else if (b1 == true && b2 == false)
					return 1;
				else
					return 0;
			}
		}
	}
}
