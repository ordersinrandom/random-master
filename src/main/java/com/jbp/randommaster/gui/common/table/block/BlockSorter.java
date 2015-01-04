package com.jbp.randommaster.gui.common.table.block;

import java.util.List;

/**
 * 
 * <code>BlockSorter</code> is called when the table determines the blocks in
 * the table has to be sorted.
 * 
 */
public interface BlockSorter {

	/**
	 * Sort the blocks.
	 * 
	 * @param blocks
	 *            The blocks to sort. (copied, the list can be altered)
	 * @param columnIndex
	 *            The current table column model index to sort.
	 * @param ascending
	 *            Ascending order.
	 * @return An ordered list of the original blocks.
	 */
	@SuppressWarnings("rawtypes")
	public List sortBlocks(List blocks, int columnIndex, boolean ascending);

	/**
	 * Primitive operation to compare two blocks by a given column index and the
	 * sorting order.
	 * 
	 * @return -1 if b1 should be shown first, 1 if b2 should be shown first, 0
	 *         if both are equal.
	 */
	public int compareBlocks(Block b1, Block b2, int columnIndex,
			boolean ascending);
}