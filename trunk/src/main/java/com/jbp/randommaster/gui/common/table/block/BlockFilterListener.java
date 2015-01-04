package com.jbp.randommaster.gui.common.table.block;

import java.util.EventListener;

/**
 * 
 * <code>BlockFilterListener</code> detects changes from <code>BlockFilter</code>.
 *
 */
public interface BlockFilterListener extends EventListener {
  
  /**
   * Notify when the block filter settings changed.
   */
  public void blockFilterChanged(BlockFilterEvent e);
  
}