package com.jbp.randommaster.gui.common.table.block;

/**
 * 
 * <code>ColumnMouseTrigger</code> fires event object
 * when the mouse clicked on a table header.
 * 
 * @author plchung
 *
 */
public interface ColumnMouseTrigger {
  
  public void addColumnMouseTriggerListener(ColumnMouseTriggerEventListener mt);
  
  public void removeColumnMouseTriggerListener(ColumnMouseTriggerEventListener mt);
}