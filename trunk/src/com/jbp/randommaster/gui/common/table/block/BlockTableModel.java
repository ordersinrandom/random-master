package com.jbp.randommaster.gui.common.table.block;

import java.awt.Dimension;
import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent;
/*
 import com.jandp.log.TerminalLog;
 import com.jandp.module.trading.order.table.OrderStatusTableItem;
 import com.jandp.states.beans.OrderStatus;*/
import com.jbp.randommaster.gui.common.table.block.impl.SimpleBlocksManager;

/**
 * 
 * <code>BlockTableModel</code> allows conceptual <code>Block</code> instances
 * to be added to a JTable.
 * 
 * @see Block
 * @see AbstractBlock
 * 
 * @author plchung
 * 
 */
public class BlockTableModel extends AbstractTableModel implements
		BlockChangeListener, BlockFilterListener {

	private static final long serialVersionUID = 6225706351179010820L;

	protected String[] columnNames;

	// for sorting.
	// mapping fom model index to BlockSorter instance.
	@SuppressWarnings("rawtypes")
	protected Map columnSorters;

	protected BlocksManager blocksManager;

	// block filter
	private BlockFilter blockFilter;

	/**
	 * Create a new <code>BlockTableModel</code> instance.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public BlockTableModel() {
		blocksManager = new SimpleBlocksManager(this);

		columnNames = new String[] { "" };

		columnSorters = new HashMap();
	}

	/**
	 * Get an iterator for the blocks in current display order.
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getBlocks() {
		return blocksManager.getBlocks();
	}

	@SuppressWarnings("rawtypes")
	public void addBlocks(Collection blocks) {
		blocksManager.addBlocks(blocks);
		if (!blocks.isEmpty())
			// super.fireTableRowsInserted(minRow, maxRow);
			super.fireTableStructureChanged();

	}

	/**
	 * Add a block to the model.
	 * 
	 * @return whether the block is shown (because it may be hidden by the
	 *         filter)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addBlock(Block b) {
		Collection blocks = new LinkedList();
		blocks.add(b);
		blocksManager.addBlocks(blocks);

		// if there is a filter
		boolean hidden = false;
		if (blockFilter != null) {
			// determine whether it should be shown
			if (!blockFilter.shouldShow(b)) {
				hideBlock(b);
				hidden = true;
			}
		}

		// PENDING: refresh UI
		if (!hidden) {
			// find the new block rows.
			int[] rows = this.findBlockRows(b);
			// check if it is valid block rows
			if (rows != null && rows[0] >= 0 && rows[1] >= 0)
				super.fireTableRowsInserted(rows[0], rows[1]);
		}

		// return true if it is not hidden.
		return !hidden;
	}

	public void removeAll() {

		int count = blocksManager.getBlocksCount();

		blocksManager.removeAll();

		if (count > 0) {
			super.fireTableRowsDeleted(0, count - 1);
		}

	}

	/**
	 * Remove a block from the model.
	 */
	public void removeBlock(Block b) {

		int[] rows = this.findBlockRows(b);

		if (b.getBlockId() == null)
			throw new IllegalArgumentException(
					"null block ID, the item is not in the table model");

		blocksManager.removeBlock(b);

		if (rows != null && rows[0] >= 0 && rows[1] >= 0)
			super.fireTableRowsDeleted(rows[0], rows[1]);
	}

	/**
	 * Hide a block in the table model.
	 */
	public void hideBlock(Block b) {
		// if already hiding, forget this call.
		if (isBlockHiding(b))
			return;

		int[] rows = this.findBlockRows(b);

		blocksManager.hideBlock(b);

		if (rows != null && rows[0] >= 0 && rows[1] >= 0)
			super.fireTableRowsDeleted(rows[0], rows[1]);
	}

	/**
	 * Show a block in the table model.
	 */
	public void showBlock(Block b) {
		// if not hiding, just forget this call.
		if (!isBlockHiding(b))
			return;

		blocksManager.showBlock(b);
		// PENDING: refresh UI
		// can be optimized.
		int rowCount = this.getRowCount();
		super.fireTableRowsInserted(rowCount - b.getHeight(), rowCount - 1);
		super.fireTableRowsUpdated(0, rowCount - 1);
	}

	public boolean isBlockHiding(Block b) {
		return blocksManager.isBlockHiding(b);
	}

	/**
	 * Implementation of <code>BlockChangeListener</code>
	 */
	public void blockSizeChanged(BlockChangeEvent e) {

		Block b = e.getBlock();
		int[] pos = blocksManager.findBlockRows(b);
		int oldHeight = pos[1] - pos[0];
		Dimension newSize = e.getNewSize();

		int rowsDelta = newSize.height - oldHeight;
		// update the blocks manager.
		blocksManager.updateSize(b, rowsDelta);

		if (newSize.height > oldHeight) {
			// added rows: rowsDelta
			super.fireTableRowsInserted(pos[1] + 1, pos[1] + rowsDelta);
		} else {
			// removed rows: rowsDelta (negative)
			super.fireTableRowsDeleted(pos[1] + rowsDelta + 1, pos[1]);
		}

		// int rowCount=this.getRowCount();
		// super.fireTableRowsUpdated(0, rowCount-1);

		/*
		 * 
		 * // PENDING: refresh UI // can be optimized. int
		 * rowCount=this.getRowCount();
		 * super.fireTableRowsInserted(rowCount-e.getBlock().getHeight(),
		 * rowCount-1); super.fireTableRowsUpdated(0, rowCount-1);
		 */
	}

	/**
	 * Implementation of <code>BlockChangeListener</code>
	 */
	public void blockDataChanged(BlockChangeEvent e) {
		// PENDING: refresh UI
		Block b = e.getBlock();
		if (b == null)
			return;

		// filter checking and see if it should be hidden
		if (blockFilter != null) {
			// if should not be shown and it is not hiding.
			// or it should be shown and it is hiding.
			boolean shouldShow = blockFilter.shouldShow(b);
			boolean isHiding = isBlockHiding(b);

			/*
			 * if (b instanceof OrderStatusTableItem) { OrderStatus
			 * os=((OrderStatusTableItem) b).getOrderStatus();
			 * TerminalLog.info("BlockTableModel.blockDataChanged(): os="+os);
			 * TerminalLog
			 * .info("shouldShow="+shouldShow+", isHiding="+isHiding); }
			 */

			if (!shouldShow && !isHiding) {
				hideBlock(b);
				// TerminalLog.info("block hidden");
				return;
			} else if (shouldShow && isHiding) {
				showBlock(b);
				// TerminalLog.info("block shown");
				return;
			}
		}

		int[] rows = this.findBlockRows(b);

		// don't fire any event in case the block is not showing on screen.
		// because there is nothing updated.
		if (rows == null || rows.length < 2 || rows[0] < 0 || rows[1] < 0)
			return;

		// System.out.println("data changed for block id: "+e.getBlock().getBlockId());
		if (rows != null && rows[0] >= 0 && rows[1] >= 0)
			super.fireTableRowsUpdated(rows[0], rows[1]);

	}

	/**
	 * Set the column names of this model:<br/>
	 * If the "names" array having different size than the previous number of
	 * columns in a table, or the "createColumns" flag is true, a
	 * "TableChangeEvent" on the table header row will be fired and the table
	 * header will be re-created. (i.e. forget about the size and position
	 * changes) <br/>
	 * <br/>
	 * Otherwise the column header will not be updated until an explicit call to
	 * the <code>TableRepaintUtil.repaintHeader()</code>
	 * 
	 * @see com.jandp.ui.common.table.block.TableRepaintUtil#repaintHeader
	 */
	public void setColumnNames(String[] names, boolean createColumns) {
		// save the column count before setting colum names.
		int prevSize = getColumnCount();

		columnNames = names;

		// if the previous size is different, update everything.
		// or create columns is set to true.
		// reordered and resized
		if (prevSize != names.length || createColumns) {
			super.fireTableRowsUpdated(TableModelEvent.HEADER_ROW,
					TableModelEvent.HEADER_ROW);
		}
	}

	/**
	 * Set the column names and but do not create the columns unless the "names"
	 * array have different size than the column header.
	 */
	public void setColumnNames(String[] names) {
		setColumnNames(names, false);
	}

	public String getColumnName(int index) {
		/*
		 * int arrayIndex=0; if (blocksManager.getBlocksCount()==0) { // just
		 * return the column name. arrayIndex=index%columnNames.length; return
		 * columnNames[arrayIndex]; } else {
		 * arrayIndex=index%blocksManager.getMaximumBlockWidth(); if
		 * (arrayIndex>=columnNames.length) return " "; else return
		 * columnNames[arrayIndex]; }
		 */

		// just return the column name.
		int arrayIndex = index % columnNames.length;
		return columnNames[arrayIndex];

	}

	/**
	 * Overrided <code>AbstractTableModel</code> making it adapted to the
	 * "Block" data.
	 * 
	 */
	public Object getValueAt(int row, int column) {
		Block b = findBlock(row);
		if (b == null)
			return "";
		int[] blockRows = findBlockRows(b);
		int relativeRow = row - blockRows[0];
		Object v = b.getValueAt(column, relativeRow);
		// System.out.println("getting value ("+column+", "+relativeRow+"): "+v);
		if (v != null)
			return v;
		else
			return "";
	}

	/**
	 * Overrided <code>AbstractTableModel</code> making it adapted to the
	 * "Block" data.
	 */
	public void setValueAt(Object v, int row, int column) {
		Block target = findBlock(row);
		if (target != null) {
			int[] blockRows = findBlockRows(target);
			int relativeRow = row - blockRows[0];
			target.setValueAt(v, column, relativeRow);
		}
	}

	/**
	 * Override the super class implementation. It checks if the targeting block
	 * cell is editable.
	 */
	public boolean isCellEditable(int row, int column) {
		Block target = findBlock(row);
		if (target != null) {
			int[] blockRows = findBlockRows(target);
			int relativeRow = row - blockRows[0];
			return target.isEditableAt(column, relativeRow);
		} else
			return false;
	}

	/**
	 * Implementation of <code>AbstractTableModel</code>
	 */
	public int getRowCount() {
		return blocksManager.getRowCount();
	}

	/**
	 * Implementation of <code>AbstractTableModel</code>
	 */
	public int getColumnCount() {
		/*
		 * // if empty. if (blocksManager.getBlocksCount()==0) return
		 * columnNames.length; // if there is a block else return
		 * blocksManager.getMaximumBlockWidth();
		 */

		return columnNames.length;
	}

	/**
	 * check if a particular column is sortable.
	 * 
	 * @param modelIndex
	 */
	public boolean isColumnSortable(int modelIndex) {
		return columnSorters.containsKey(new Integer(modelIndex));
	}

	/**
	 * Get the current sorting model index.
	 * 
	 * @return the model index or -1 if nothing is sorted.
	 */
	public int getSortingModelIndex() {
		return blocksManager.getSortingColumnIndex();
	}

	/**
	 * Get the current sorting order.
	 * 
	 * @return true means Ascending, false means Descending.
	 */
	public boolean getSortingOrder() {
		return blocksManager.isAscendingOrdered();
	}

	/**
	 * Sort the column by model index.
	 */
	public void globalSortByColumn(int modelIndex, boolean ascending) {
		// if not sortable, return
		if (!isColumnSortable(modelIndex))
			return;

		// locate the sorter.
		BlockSorter sorter = (BlockSorter) columnSorters.get(new Integer(
				modelIndex));
		if (sorter == null)
			throw new IllegalStateException("BlockSorter of column "
					+ modelIndex + " is not found.");

		blocksManager.sort(sorter, modelIndex, ascending);

		int count = getRowCount();

		super.fireTableRowsUpdated(0, count - 1);
	}

	/**
	 * Set the block sorter for a particular column
	 * 
	 * @param columnModelIndex
	 * @param s
	 */
	@SuppressWarnings("unchecked")
	public void setSorter(int columnModelIndex, BlockSorter s) {
		columnSorters.put(new Integer(columnModelIndex), s);
	}

	/**
	 * Set the column mouse trigger to sort the columns.
	 * 
	 * @param t
	 * @see <code>DefaultColumnMouseTrigger</code>
	 */
	public void setColumnMouseTrigger(ColumnMouseTrigger t) {
		t.addColumnMouseTriggerListener(new ColumnMouseTriggerEventListener() {
			public void columnTriggered(ColumnMouseTriggerEvent e) {
				// System.out.println("BlockTableModel: clicked column index="+e.getModelIndex());
				int index = e.getModelIndex();
				if (isColumnSortable(index)) {
					boolean order = true;

					if (blocksManager.getSortingColumnIndex() == index)
						order = !blocksManager.isAscendingOrdered();

					// sorting.
					globalSortByColumn(index, order);
				}
			}
		});
	}

	// note: can be further tuned, if the block height is always the same
	/**
	 * Get the block by a given row index.
	 */
	public Block findBlock(int row) {
		return blocksManager.findBlock(row);
	}

	// note: can be further tuned, if the block height is always the same
	/**
	 * Get the row index used by this block currently.
	 */
	public int[] findBlockRows(Block b) {
		return blocksManager.findBlockRows(b);
	}

	/**
	 * Find the relative row index within a block by the given
	 * "global table row index".
	 * 
	 * @return the relative row index, or -1 if the block is not found.
	 */
	public int findBlockRelativeRow(int tableRow) {
		Block b = findBlock(tableRow);
		if (b == null)
			return -1;
		int[] blockRows = findBlockRows(b);
		if (blockRows == null)
			return -1;
		return tableRow - blockRows[0];
	}

	/**
	 * Set the block filter. It shows/hides table block when the filter changes.
	 * 
	 * @param filter
	 *            The block filter, or null to unset.
	 */
	public void setBlockFilter(BlockFilter filter) {
		if (blockFilter != null)
			blockFilter.removeBlockFilterListener(this);

		blockFilter = filter;

		if (blockFilter != null)
			blockFilter.addBlockFilterListener(this);
	}

	/**
	 * Get the block filter in use.
	 * 
	 * @return
	 */
	public BlockFilter getBlockFilter() {
		return blockFilter;
	}

	/**
	 * Implementation of <code>BlockFilterListener</code>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void blockFilterChanged(BlockFilterEvent e) {
		BlockFilter f = e.getFilter();
		if (f != null) {
			LinkedList showTargets = new LinkedList();
			LinkedList hideTargets = new LinkedList();

			for (Iterator it = getBlocks(); it.hasNext();) {
				Block b = (Block) it.next();
				boolean shouldShow = f.shouldShow(b);
				boolean isHidingNow = isBlockHiding(b);
				if (shouldShow && isHidingNow)
					showTargets.add(b);
				else if (!shouldShow && !isHidingNow)
					hideTargets.add(b);
			}

			for (Iterator it = hideTargets.iterator(); it.hasNext();)
				hideBlock((Block) it.next());

			for (Iterator it = showTargets.iterator(); it.hasNext();)
				showBlock((Block) it.next());

		}
	}

}