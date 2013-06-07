package com.jbp.randommaster.gui.common.table.block;

import java.util.EventListener;

/**
 * 
 * <code>BlockChangeListener</code> detects the changes of the
 * <code>Block</code> instance. The change could be the encapsulated
 * data changes or the block dimension changes, etc.
 * 
 * @author plchung
 *
 */
public interface BlockChangeListener extends EventListener {
  
  public void blockSizeChanged(BlockChangeEvent e);
  
  public void blockDataChanged(BlockChangeEvent e);
  
}