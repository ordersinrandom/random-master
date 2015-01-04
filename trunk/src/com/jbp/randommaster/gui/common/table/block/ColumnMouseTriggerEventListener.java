package com.jbp.randommaster.gui.common.table.block;

import java.util.EventListener;

public interface ColumnMouseTriggerEventListener extends EventListener {
  public void columnTriggered(ColumnMouseTriggerEvent e);
}