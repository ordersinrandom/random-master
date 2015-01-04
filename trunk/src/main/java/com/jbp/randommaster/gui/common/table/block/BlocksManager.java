package com.jbp.randommaster.gui.common.table.block;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * <code>BlocksManager</code> is the interface that the
 * <code>BlockTableModel</code> would use to manage its blocks.
 * 
 * @author plchung
 * 
 */
public interface BlocksManager {

	/**
	 * Add a collection of blocks to this manager, according to the sorted
	 * column model index and sorting order.
	 * 
	 * @param blocks
	 *            A set of unique <code>Block</code> instances to be added.
	 */
	@SuppressWarnings("rawtypes")
	public void addBlocks(Collection blocks);

	/**
	 * Update the internal position tables with the new size.
	 * 
	 * @param b
	 *            The block that changed size.
	 * @param delta
	 *            The number of rows that it added, or removed.
	 */
	public void updateSize(Block b, int delta);

	/**
	 * Find the block by a given row.
	 * 
	 * @return The block instance or null if it is not found.
	 */
	public Block findBlock(int row);

	/**
	 * Get the collection of blocks managed by this manager.
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getBlocks();

	/**
	 * Find the row indice occupied by the given block.
	 * 
	 * @param b
	 *            A block that is managed by this manager.
	 * @return The row indice.
	 */
	public int[] findBlockRows(Block b);

	/**
	 * Get the total number of blocks currently managed by this manager.
	 */
	public int getBlocksCount();

	/**
	 * Get the number of rows totally occupied by the blocks managed by this
	 * manager.
	 */
	public int getRowCount();

	/**
	 * Remove all blocks in this manager.
	 * 
	 */
	public void removeAll();

	/**
	 * Remove a single block in this manager.
	 */
	public void removeBlock(Block b);

	/**
	 * Show a block that is managed by this manager.
	 */
	public void showBlock(Block b);

	/**
	 * Hide a block that is managed by this manager.
	 */
	public void hideBlock(Block b);

	/**
	 * Check if a particular block is currently hidden in the table.
	 */
	public boolean isBlockHiding(Block b);

	/**
	 * Get the maximum width of all blocks currently managed by this manager.
	 */
	public int getMaximumBlockWidth();

	/**
	 * Sort the blocks and remember the current sorting setting information.
	 * 
	 * @param sorter
	 *            The sorter to use.
	 * @param modelIndex
	 *            The column model index.
	 * @param ascending
	 *            The sorting order.
	 */
	public void sort(BlockSorter sorter, int modelIndex, boolean ascending);

	/**
	 * Get the sorting column index.
	 * 
	 * @return -1 means no column is currently sorted.
	 */
	public int getSortingColumnIndex();

	/**
	 * Get the sorting order.
	 * 
	 * @return true means ascending, false means descending.
	 */
	public boolean isAscendingOrdered();

}