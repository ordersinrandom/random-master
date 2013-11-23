package com.jbp.randommaster.gui.common.table.block;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Comparator;

/**
 * 
 * <code>ComparatorSorter</code> provides the default implementation of sorting
 * with a comparator
 * 
 * @author plchung
 * 
 */
public class ComparatorSorter implements BlockSorter {

	private int targetRow;
	@SuppressWarnings("rawtypes")
	private Comparator comparator;

	@SuppressWarnings("rawtypes")
	public ComparatorSorter(Comparator comparator) {
		this(0, comparator);
	}

	@SuppressWarnings("rawtypes")
	public ComparatorSorter(int targetRow, Comparator comparator) {
		this.targetRow = targetRow;
		this.comparator = comparator;
	}

	/**
	 * Primitive operation to compare two blocks by a given column index and the
	 * sorting order.
	 * 
	 * @return -1 if b1 should be shown first, 1 if b2 should be shown first, 0
	 *         if both are equal.
	 */
	@SuppressWarnings("unchecked")
	public int compareBlocks(Block b1, Block b2, int columnIndex,
			boolean ascending) {
		// parameters check.
		if (b1 == null)
			throw new IllegalArgumentException("null Block b1");
		if (b2 == null)
			throw new IllegalArgumentException("null Block b2");
		if (columnIndex < 0)
			throw new IllegalArgumentException("Illegal column index given :"
					+ columnIndex);

		Object item1 = b1.getValueAt(columnIndex, targetRow);
		Object item2 = b2.getValueAt(columnIndex, targetRow);
		int v = 0;
		if (item1 != null && item2 != null)
			v = comparator.compare(item1, item2);
		else if (item1 != null && item2 == null)
			v = -1;
		else if (item1 == null && item2 != null)
			v = 1;
		else
			v = 0;

		if (ascending)
			return v;
		else {
			// reverse case if descending.
			if (v < 0)
				return 1;
			else if (v > 0)
				return -1;
			else
				return 0;
		}

	}

	/**
	 * Sort the given blocks. It is implemented by merge sort to provide more
	 * static output.
	 * 
	 * @param blocks
	 *            The input blocks to be sorted
	 * @param columnIndex
	 *            The column index to look up the sorting key
	 * @param ascending
	 *            The sorting order, true means ascending, false means
	 *            descending.
	 * 
	 * @return A list of sorted blocks by the given parameters.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List sortBlocks(List blocks, int columnIndex, boolean ascending) {
		// check parameter.
		if (blocks == null)
			throw new IllegalArgumentException("blocks is null");
		if (columnIndex < 0)
			throw new IllegalArgumentException("illegal column index");

		int inputSize = blocks.size();
		if (inputSize == 0)
			return blocks;
		else if (inputSize == 1)
			return blocks;
		else if (inputSize == 2) {
			Block b1 = (Block) blocks.get(0);
			Block b2 = (Block) blocks.get(1);
			int v = compareBlocks(b1, b2, columnIndex, ascending);
			// if b2 should be shown first.
			if (v > 0) {
				blocks.set(0, b2);
				blocks.set(1, b1);
			}
			return blocks;
		} else {
			List firstList = new LinkedList();
			List secondList = new LinkedList();
			int index = 0;
			for (Iterator it = blocks.iterator(); it.hasNext(); index++) {
				if (index < inputSize / 2)
					firstList.add(it.next());
				else
					secondList.add(it.next());
			}

			List r1 = sortBlocks(firstList, columnIndex, ascending);
			List r2 = sortBlocks(secondList, columnIndex, ascending);

			List result = new LinkedList();
			for (int i = 0; i < inputSize; i++) {
				if (r1.isEmpty())
					result.add(r2.remove(0));
				else if (r2.isEmpty())
					result.add(r1.remove(0));
				else {
					Block b1 = (Block) r1.get(0);
					Block b2 = (Block) r2.get(0);
					int v = compareBlocks(b1, b2, columnIndex, ascending);
					if (v <= 0)
						result.add(r1.remove(0));
					else
						result.add(r2.remove(0));
				}
			}

			return result;
		}
	}

	/*
	 * OLD INSERTION SORT IMPLEMENTATION public List sortBlocks(java.util.List
	 * blocks, int columnIndex, boolean ascending) { List result=new
	 * LinkedList(); if (ascending) { while (!blocks.isEmpty()) { Block
	 * my=(Block) blocks.remove(0); boolean matched=false; int targetIndex=-1;
	 * for (int k=0;k<result.size();k++) { Block b=(Block) result.get(k); int
	 * v=0;
	 * 
	 * Object item1=my.getValueAt(columnIndex, targetRow); Object
	 * item2=b.getValueAt(columnIndex, targetRow);
	 * 
	 * if (item1!=null && item2!=null) v=comparator.compare(item1, item2); else
	 * if (item1!=null && item2==null) v=-1; else if (item1==null &&
	 * item2!=null) v=1; else v=0;
	 * 
	 * if (v<0) { matched=true; targetIndex=k; break; } } if (matched)
	 * result.add(targetIndex, my); else result.add(my); } } else { while
	 * (!blocks.isEmpty()) { Block my=(Block) blocks.remove(0); boolean
	 * matched=false; int targetIndex=-1; for (int k=0;k<result.size();k++) {
	 * Block b=(Block) result.get(k);
	 * 
	 * int v=0;
	 * 
	 * Object item1=my.getValueAt(columnIndex, targetRow); Object
	 * item2=b.getValueAt(columnIndex, targetRow);
	 * 
	 * if (item1!=null && item2!=null) v=comparator.compare(item1, item2); else
	 * if (item1!=null && item2==null) v=-1; else if (item1==null &&
	 * item2!=null) v=1; else v=0;
	 * 
	 * if (v>0) { matched=true; targetIndex=k; break; } } if (matched)
	 * result.add(targetIndex, my); else result.add(my); } } return result; }
	 */
}