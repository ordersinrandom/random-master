package com.jbp.randommaster.gui.common.table.block;

import java.util.EventListener;

public interface TreeBlockChangeListener extends EventListener {
  
  public void treeBlockNodeAdded(TreeBlockChangeEvent e);
  
  public void treeBlockNodeRemoved(TreeBlockChangeEvent e);
  
  public void treeBlockNodeValueChanged(TreeBlockChangeEvent e);
  
}