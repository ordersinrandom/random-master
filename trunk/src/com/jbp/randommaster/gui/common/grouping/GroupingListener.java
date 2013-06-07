package com.jbp.randommaster.gui.common.grouping;

import java.util.EventListener;

public interface GroupingListener extends EventListener {
  public void groupNameChanged(GroupingEvent e);
  
  public void groupSelected(GroupingEvent e);
}