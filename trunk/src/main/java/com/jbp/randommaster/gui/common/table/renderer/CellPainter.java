package com.jbp.randommaster.gui.common.table.renderer;

import javax.swing.JLabel;

public interface CellPainter {
  
  public void addStyle(PainterStyle s);
  
  public PainterStyle getFinalStyle();

  public JLabel getRenderingLabel();
}