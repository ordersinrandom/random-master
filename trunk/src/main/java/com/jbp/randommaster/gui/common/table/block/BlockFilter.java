package com.jbp.randommaster.gui.common.table.block;

/**
 * 
 * <code>BlockFilter</code> determines whether a particular table block
 * in the table model should be shown or hidden. It also notifies the
 * table model when the filter state changes through event listener 
 * mechanism. 
 * 
 *
 */
public interface BlockFilter {
  
  /**
   * Determine whether this table block should be shown.
   * @param b A table block in the table model.
   * @return true if the block should be shown.
   */
  public boolean shouldShow(Block b);
  
  /**
   * Add a listener to determine filter setting changes.
   * 
   */
  public void addBlockFilterListener(BlockFilterListener l);
  
  /**
   * Remove a filter event listener.
   */
  public void removeBlockFilterListener(BlockFilterListener l);
  
}