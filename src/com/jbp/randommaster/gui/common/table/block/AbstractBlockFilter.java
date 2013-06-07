package com.jbp.randommaster.gui.common.table.block;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * <code>AbstractBlockFilter</code> provides default implementation
 * for <code>BlockFilter</code> interface. It mainly provides support
 * for the event listener implementation.
 *
 */
public abstract class AbstractBlockFilter implements BlockFilter {

  @SuppressWarnings("rawtypes")
protected LinkedList blockFilterListeners;
  
  public abstract boolean shouldShow(Block b);
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addBlockFilterListener(BlockFilterListener l) {
    if (blockFilterListeners==null)
      blockFilterListeners=new LinkedList();
    
    if (!blockFilterListeners.contains(l))
      blockFilterListeners.add(l);
  }
  
  public void removeBlockFilterListener(BlockFilterListener l) {
    if (blockFilterListeners!=null)
      blockFilterListeners.remove(l);
  }
  
  @SuppressWarnings("rawtypes")
protected void fireBlockFilterEvent(BlockFilterEvent e) {
    LinkedList targets=(LinkedList) blockFilterListeners.clone();
    for (Iterator it=targets.iterator();it.hasNext();) 
      ((BlockFilterListener) it.next()).blockFilterChanged(e);
  }

}