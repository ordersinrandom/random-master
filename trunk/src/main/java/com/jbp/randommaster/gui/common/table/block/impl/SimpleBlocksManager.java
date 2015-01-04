package com.jbp.randommaster.gui.common.table.block.impl;

import java.util.Comparator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;

import com.jbp.randommaster.gui.common.table.block.BlockChangeListener;
import com.jbp.randommaster.gui.common.table.block.BlockChangeEvent;
import com.jbp.randommaster.gui.common.table.block.BlocksManager;
import com.jbp.randommaster.gui.common.table.block.Block;
import com.jbp.randommaster.gui.common.table.block.BlockTableModel;
import com.jbp.randommaster.gui.common.table.block.BlockSorter;

/**
 * 
 * <code>SimpleBlocksManager</code> is the basic implementation of blocks
 * manager.
 * 
 * @author plchung
 * 
 */
public class SimpleBlocksManager implements BlocksManager, BlockChangeListener {

	// the next block id to assign if a new one comes.
	private int nextBlockId;

	// mapping from block id to Block object
	@SuppressWarnings("rawtypes")
	private Map blockIdToBlock;
	// the ordered block objects.
	@SuppressWarnings("rawtypes")
	private List orderedBlocks;

	// block id collection.
	@SuppressWarnings("rawtypes")
	private Collection hiddenBlocksId;

	// position table
	// mapping from Block instance to BlockPosition instance.
	@SuppressWarnings("rawtypes")
	private Map positionTable;
	// mapping from BlockPosition instance to Block instance.
	@SuppressWarnings("rawtypes")
	private Map rowSearchMap;

	// record the maximum block width
	private int maxBlockWidth;

	// the reference to the table model.
	private BlockTableModel tableModel;

	// the sorter in use.
	private BlockSorter currentSorter;
	// the current sorting column
	private int sortingColumnIndex;
	// sorting order.
	private boolean ascending;

	/**
	 * Create an instance of <code>SimpleBlocksManager</code>.
	 * 
	 * @param tableModel
	 *            The table model to interact.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SimpleBlocksManager(BlockTableModel tableModel) {
		// the maximum block width
		maxBlockWidth = 1;

		// mapping from block ID to block
		blockIdToBlock = new TreeMap();

		// a list of Block objects that would be maintained in order
		// of the current sorting setting.
		orderedBlocks = new LinkedList();

		// a collection of block id that should be hidden currently.
		hiddenBlocksId = new HashSet();

		positionTable = new HashMap();
		rowSearchMap = new TreeMap(new BlockPositionComparator());

		// the next block id to be assigned.
		nextBlockId = 0;

		// reference to the table model.
		this.tableModel = tableModel;

		// set the default value of sorting info
		currentSorter = null;
		sortingColumnIndex = -1;
		ascending = true;
	}

	/**
	 * Update the internal position tables with the new size.
	 * 
	 * @param b
	 *            The block that changed size.
	 * @param delta
	 *            The number of rows that it added, or removed.
	 */
	@SuppressWarnings("rawtypes")
	public void updateSize(Block b, int delta) {
		int w = b.getWidth();
		if (maxBlockWidth < w)
			maxBlockWidth = w;

		// update the positionTable
		int index = orderedBlocks.indexOf(b);
		for (ListIterator it = orderedBlocks.listIterator(index); it.hasNext();) {
			Block obj = (Block) it.next();

			BlockPosition bp = (BlockPosition) positionTable.get(obj);
			bp.end += delta;
			if (b != obj)
				bp.start += delta;
		}

		this.recomputeRowSearchMap();
	}

	/**
	 * Add a collection of blocks to this manager, according to the sorted
	 * column model index and sorting order.
	 * 
	 * @param blocks
	 *            A set of unique <code>Block</code> instances to be added.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addBlocks(Collection blocks) {

		for (Iterator it = blocks.iterator(); it.hasNext();) {
			Block b = (Block) it.next();

			int blockId = nextBlockId;
			nextBlockId++;

			Integer k = new Integer(blockId);
			b.setBlockId(k);
			blockIdToBlock.put(k, b);

			// register listeners
			b.addBlockChangeListener(tableModel);
			b.addBlockChangeListener(this);

			// update the maximum block width.
			if (b.getWidth() > maxBlockWidth)
				maxBlockWidth = b.getWidth();

			// if unsorted, just append
			int targetIndex = -1;
			if (currentSorter != null) {

				targetIndex = binarySearchInsertionIndex(orderedBlocks, b, 0,
						orderedBlocks.size());
			}

			// insert
			BlockPosition finalPos = new BlockPosition(-1, -1);
			if (targetIndex != -1) {
				// insert the new block
				orderedBlocks.add(targetIndex, b);
				// add a new dummy record to position table.
				positionTable.put(b, finalPos);
			}
			// append
			else {
				// append the new block
				orderedBlocks.add(b);
				// add a new dummy record to position table.
				positionTable.put(b, finalPos);
			}

			// refresh the position table

			this.shiftPositionTable(true, b);

		}

	}

	/**
	 * Remove all blocks in this manager.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public void removeAll() {
		// remove the listener
		for (Iterator it = orderedBlocks.iterator(); it.hasNext();) {
			Block b = (Block) it.next();
			b.removeBlockChangeListener(tableModel);
			b.removeBlockChangeListener(this);
		}

		if (!orderedBlocks.isEmpty()) {
			blockIdToBlock.clear();
			orderedBlocks.clear();
			hiddenBlocksId.clear();
			positionTable.clear();
			rowSearchMap.clear();
		}
	}

	/**
	 * Remove a single block in this manager.
	 */
	public void removeBlock(Block b) {
		Block ref = (Block) blockIdToBlock.remove(b.getBlockId());
		if (ref != null && ref == b) {
			hiddenBlocksId.remove(b.getBlockId());
		}
		// remove the listener
		b.removeBlockChangeListener(tableModel);
		b.removeBlockChangeListener(this);
		// adjust the new block width
		maxBlockWidth = this.findMaxBlockWidth();

		// adjust the position
		this.shiftPositionTable(false, b);

		// remove the entries.
		positionTable.remove(b);
		orderedBlocks.remove(b);

	}

	/**
	 * Get the maximum width of all blocks currently managed by this manager.
	 */
	public int getMaximumBlockWidth() {
		return maxBlockWidth;
	}

	/**
	 * Get the total number of blocks currently managed by this manager.
	 */
	public int getBlocksCount() {
		return orderedBlocks.size();
	}

	/**
	 * Find the block by a given row.
	 * 
	 * @return The block instance or null if it is not found.
	 */
	public Block findBlock(int row) {
		if (row < 0)
			return null;

		Block result = (Block) rowSearchMap.get(new Integer(row));
		return result;
	}

	/**
	 * Get the collection of blocks managed by this manager.
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getBlocks() {
		// return orderedBlocks.iterator();

		return ((List) ((LinkedList) orderedBlocks).clone()).iterator();
	}

	/**
	 * Find the row indice occupied by the given block.
	 * 
	 * @param b
	 *            A block that is managed by this manager.
	 * @return The row indice.
	 */
	public int[] findBlockRows(Block b) {
		BlockPosition bp = (BlockPosition) positionTable.get(b);
		if (bp == null || bp.isInvalid())
			return new int[] { -1, -1 };
		else
			return new int[] { bp.start, bp.end - 1 };
	}

	/**
	 * Get the number of rows totally occupied by the blocks managed by this
	 * manager.
	 */
	@SuppressWarnings("rawtypes")
	public int getRowCount() {
		if (orderedBlocks.isEmpty())
			return 0;
		else {
			ListIterator lit = orderedBlocks.listIterator(orderedBlocks.size());
			int result = 0;
			while (lit.hasPrevious()) {
				Block b = (Block) lit.previous();
				if (hiddenBlocksId.contains(b.getBlockId()))
					continue;

				BlockPosition bp = (BlockPosition) positionTable.get(b);
				result = bp.end;
				break;
			}

			return result;
		}
	}

	/**
	 * Show a block that is managed by this manager.
	 */
	public void showBlock(Block b) {
		if (hiddenBlocksId.contains(b.getBlockId())) {
			hiddenBlocksId.remove(b.getBlockId());
			this.shiftPositionTable(true, b);
			maxBlockWidth = this.findMaxBlockWidth();
		}

	}

	/**
	 * Hide a block that is managed by this manager.
	 */
	@SuppressWarnings("unchecked")
	public void hideBlock(Block b) {
		if (!hiddenBlocksId.contains(b.getBlockId())) {
			hiddenBlocksId.add(b.getBlockId());
			this.shiftPositionTable(false, b);
			maxBlockWidth = this.findMaxBlockWidth();
		}

	}

	/**
	 * Check if a particular block is currently hidden in the table.
	 */
	public boolean isBlockHiding(Block b) {
		return hiddenBlocksId.contains(b.getBlockId());
	}

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
	public void sort(BlockSorter sorter, int modelIndex, boolean ascending) {
		this.currentSorter = sorter;
		this.sortingColumnIndex = modelIndex;
		this.ascending = ascending;

		orderedBlocks = sorter.sortBlocks(orderedBlocks, modelIndex, ascending);
		// all are changed, recompute all position
		this.recomputeRowSearchMap();
	}

	/**
	 * Get the sorting column index.
	 * 
	 * @return -1 means no column is currently sorted.
	 */
	public int getSortingColumnIndex() {
		return sortingColumnIndex;
	}

	/**
	 * Get the sorting order.
	 * 
	 * @return true means ascending, false means descending.
	 */
	public boolean isAscendingOrdered() {
		return ascending;
	}

	/**
	 * Implementation of <code>BlockChangeListener</code>
	 */
	public void blockSizeChanged(BlockChangeEvent e) {
		maxBlockWidth = this.findMaxBlockWidth();
	}

	/**
	 * Implementation of <code>BlockChangeListener</code>
	 */
	public void blockDataChanged(BlockChangeEvent e) {
		/*
		 * STILL BUGGY, FORGET IT FOR THE MOMENT // check if need to re-order
		 * this. if (sortingColumnIndex>=e.getStartX() &&
		 * sortingColumnIndex<=e.getEndX()) {
		 * 
		 * // call the model instead tableModel.removeBlock(e.getBlock());
		 * tableModel.addBlock(e.getBlock()); }
		 */
	}

	/**
	 * Helper method to re-compute the maximum block width in the current data.
	 */
	@SuppressWarnings("rawtypes")
	private int findMaxBlockWidth() {
		int value = 1;
		for (Iterator it = blockIdToBlock.values().iterator(); it.hasNext();) {
			Block b = (Block) it.next();
			if (!hiddenBlocksId.contains(b.getBlockId())
					&& b.getWidth() > value)
				value = b.getWidth();
		}
		return value;
	}

	// assume that the block b is in the orderedBlocks
	// and the BlockPosition object of b is in the positionTable
	// but just incorrect value.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shiftPositionTable(boolean adding, Block b) {
		// get the target position reference
		int targetIndex = orderedBlocks.indexOf(b);
		BlockPosition targetPos = (BlockPosition) positionTable.get(b);

		// check for error
		if (targetIndex == -1)
			throw new IllegalStateException(
					"orderedBlocks does not contains the given block");
		if (targetPos == null)
			throw new IllegalStateException("targetPos cannot be found");

		// update the row search map
		rowSearchMap.remove(targetPos);

		// try to locate an item which is the next one showing on screen
		// with this new item.
		Block shiftStart = null;
		BlockPosition originalPos = null;
		int shiftStartIndex = targetIndex + 1;
		if (shiftStartIndex < orderedBlocks.size()) {
			for (ListIterator lit = orderedBlocks.listIterator(shiftStartIndex); lit
					.hasNext(); shiftStartIndex++) {
				Block tryBlock = (Block) lit.next();
				if (!hiddenBlocksId.contains(tryBlock.getBlockId())) {
					shiftStart = tryBlock;
					originalPos = (BlockPosition) positionTable.get(shiftStart);
					break;
				}
			}
		}

		// if nothing else to shift
		if (shiftStart == null) {
			// two possible case,
			// it is either the only item showing on screen
			if (orderedBlocks.size() - hiddenBlocksId.size() == 1) {
				// if adding the item, update its position.
				if (adding) {
					targetPos.start = 0;
					targetPos.end = b.getHeight();
				}
				// if removing, just mark invalid.
				else
					targetPos.markInvalid();
			}
			// or it is the last item showing on screen
			else {
				// if adding this item, update the position
				if (adding) {
					Block prev = null;
					// locate a previous item which is showing on screen.
					// this must be found
					for (ListIterator lit = orderedBlocks
							.listIterator(targetIndex); lit.hasPrevious();) {
						Block p = (Block) lit.previous();
						// if not hidden
						if (!hiddenBlocksId.contains(p.getBlockId())) {
							prev = p;
							break;
						}
					}

					if (prev != null) {
						BlockPosition bp = (BlockPosition) positionTable
								.get(prev);
						targetPos.start = bp.end;
						targetPos.end = targetPos.start + b.getHeight();
					} else
						throw new IllegalStateException(
								"unable to locate the previoius block for shifting "
										+ b);
				}
				// if removing, just mark as invalid.
				else
					targetPos.markInvalid();
			}
		}
		// else need shifting its tail.
		else {
			if (originalPos == null)
				throw new IllegalStateException(
						"Need shifting by the shift start original position is not found");

			int shiftDelta = 0;
			// if adding this new item.
			if (adding) {
				// update the target position first
				targetPos.start = originalPos.start;
				targetPos.end = targetPos.start + b.getHeight();
				// set the shift delta
				shiftDelta = b.getHeight();

			}
			// if removing
			else {
				targetPos.markInvalid();
				// set the shift delta
				shiftDelta = -b.getHeight();
			}

			// shift the tail
			Map newRowSearchEntries = new HashMap();
			for (ListIterator lit = orderedBlocks.listIterator(shiftStartIndex); lit
					.hasNext();) {
				Block t = (Block) lit.next();
				BlockPosition bp = (BlockPosition) positionTable.get(t);
				if (!bp.isInvalid()) {
					rowSearchMap.remove(bp);
					bp.adjust(shiftDelta);
					// save the temp new result
					newRowSearchEntries.put(bp, t);
				}
			}
			// update the row search map with the new results.
			rowSearchMap.putAll(newRowSearchEntries);

		}

		// update the rowSearchMap if required.
		if (!targetPos.isInvalid())
			rowSearchMap.put(targetPos, b);

	}

	// recompute the position table.
	// this method computes everything and take care of hidden rows.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void recomputeRowSearchMap() {
		// clear the row search map.
		rowSearchMap.clear();

		int currentRow = 0;
		for (Iterator it = orderedBlocks.iterator(); it.hasNext();) {
			Block b = (Block) it.next();
			BlockPosition bp = (BlockPosition) positionTable.get(b);
			// if the block is hidden
			if (hiddenBlocksId.contains(b.getBlockId())) {
				bp.start = -1;
				bp.end = -1;
			}
			// if it is showning.
			else {
				bp.start = currentRow;
				bp.end = currentRow + b.getHeight();
				currentRow += b.getHeight();
			}
			// update the row search map
			if (!bp.isInvalid())
				rowSearchMap.put(bp, b);
		}
	}

	// return the right insertion index to the orderedBlocks
	// start index inclusive, end index exclusive
	@SuppressWarnings("rawtypes")
	private int binarySearchInsertionIndex(List subList, Block newBlock,
			int startIndex, int endIndex) {

		if (subList.isEmpty())
			return startIndex;
		else if (subList.size() == 1) {
			Block ref = (Block) subList.get(0);
			int v = currentSorter.compareBlocks(newBlock, ref,
					sortingColumnIndex, ascending);
			if (v < 0)
				return startIndex;
			else
				return startIndex + 1;
		} else {
			int mid = subList.size() / 2;
			Block ref = (Block) subList.get(mid);
			int v = currentSorter.compareBlocks(newBlock, ref,
					sortingColumnIndex, ascending);
			if (v < 0) {
				return binarySearchInsertionIndex(subList.subList(0, mid),
						newBlock, startIndex, startIndex + mid);
			} else if (v > 0) {
				return binarySearchInsertionIndex(
						subList.subList(mid, subList.size()), newBlock,
						startIndex + mid, endIndex);
			} else
				return startIndex + mid + 1;
		}
	}

	/**
	 * 
	 * <code>BlockPosition</code> is a helper class to locate the block position
	 * quickly.
	 * 
	 * @author plchung
	 * 
	 */
	private class BlockPosition {
		// inclusive
		public int start;
		// exclusive
		public int end;

		public BlockPosition(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public boolean isInvalid() {
			return start < 0 || end < 0;
		}

		public void markInvalid() {
			start = -1;
			end = -1;
		}

		/**
		 * Adjust the block position by a delta value.
		 */
		public void adjust(int delta) {
			start += delta;
			end += delta;
		}

		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("BlockPosition { start=");
			buf.append(start);
			buf.append(", end=");
			buf.append(end);
			buf.append(" }");
			return buf.toString();
		}
	}

	@SuppressWarnings("rawtypes")
	private class BlockPositionComparator implements Comparator {
		public boolean equals(Object obj) {
			return false;
		}

		public int compare(Object o1, Object o2) {
			if (o1 instanceof BlockPosition && o2 instanceof BlockPosition) {
				BlockPosition b1 = (BlockPosition) o1;
				BlockPosition b2 = (BlockPosition) o2;
				if (b1.start == b2.start && b1.end == b2.end)
					return 0;
				else if (b1.start < b2.start)
					return -1;
				else
					return 1;
			} else if (o1 instanceof BlockPosition && o2 instanceof Integer) {
				BlockPosition b1 = (BlockPosition) o1;
				Integer i2 = (Integer) o2;
				int v = i2.intValue();
				if (b1.start <= v && v < b1.end)
					return 0;
				else if (v < b1.start)
					return 1;
				else
					return -1;
			} else if (o1 instanceof Integer && o2 instanceof BlockPosition) {
				Integer i1 = (Integer) o1;
				BlockPosition b2 = (BlockPosition) o2;
				int v = i1.intValue();
				if (b2.start <= v && v < b2.end)
					return 0;
				else if (v < b2.start)
					return -1;
				else
					return 1;
			} else
				throw new IllegalArgumentException(
						"unsupporting comparable class: " + o1 + ", " + o2);
		}
	}
}