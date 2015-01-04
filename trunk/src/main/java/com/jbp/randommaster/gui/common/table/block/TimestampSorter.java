package com.jbp.randommaster.gui.common.table.block;

import java.sql.Timestamp;

import java.util.Comparator;

/**
 * 
 * <code>TimestampSorter</code> provides the default implementation of Timestamp
 * cell sorting.
 * 
 * @author plchung
 * 
 */
public class TimestampSorter extends ComparatorSorter {
	public TimestampSorter() {
		super(new TimestampComparator());
	}

	public TimestampSorter(int targetRow) {
		super(targetRow, new TimestampComparator());
	}

	@SuppressWarnings("rawtypes")
	private static class TimestampComparator implements Comparator {

		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			Timestamp d1 = (Timestamp) o1;
			Timestamp d2 = (Timestamp) o2;

			if (o1 == null && o2 == null)
				return 0;
			else if (o1 != null && o2 == null)
				return -1;
			else if (o1 == null && o2 != null)
				return 1;
			else {
				if (d1.before(d2))
					return -1;
				else if (d2.before(d1))
					return 1;
				else
					return 0;
			}
		}
	}
}
