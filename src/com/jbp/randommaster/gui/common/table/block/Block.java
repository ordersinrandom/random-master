package com.jbp.randommaster.gui.common.table.block;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * A <code>Block</code> is the instance that could be added to the
 * <code>BlockTableModel</code> which encapsulates a user object and display
 * on multiple table columns/rows.
 * 
 * @author plchung
 *
 */
public interface Block {
  
  /**
   * Get the block width.
   */
  public int getWidth();
  
  /**
   * Get the block height.
   */
  public int getHeight();
  
  /**
   * Get the block data in (x,y) relative to the block upper left corner.
   */
  public Object getValueAt(int x, int y);
  
  /**
   * Set the block data at (x,y) relative to the block upper left corner.
   */
  public void setValueAt(Object v, int x, int y);
  
  /**
   * Check if the block data is editable at (x,y) relative to block upper left corner.
   */
  public boolean isEditableAt(int x, int y);
  
  /**
   * Get the table cell editor at (x,y) relative to block upper left corner.
   * @return The table cell editor or null if it does not provide any.
   */
  public TableCellEditor getCellEditorAt(int x, int y);
  
  /**
   * Get the table cell renderer at (x,y) relative to block upper left corner.
   * @return The table cell renderer or null if it does not provide any.
   */
  public TableCellRenderer getCellRendererAt(int x, int y);  
  
  /**
   * Register a listener to the block data changes.
   */
  public void addBlockChangeListener(BlockChangeListener l);

  /**
   * Remove a listener from the block data changes.
   */
  public void removeBlockChangeListener(BlockChangeListener l);
  
  /**
   * Set the block ID (Used by the BlockTableModel).
   */
  public void setBlockId(Object id);
  
  /**
   * Get the block ID.
   */
  public Object getBlockId();
}