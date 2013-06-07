package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Color;
import java.awt.Font;
import java.text.Format;
import javax.swing.border.Border;

import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEventListener;

public interface PainterStyle {
  
  public Color getSelectedForeground();
  
  public Color getSelectedBackground();
  
  public Color getUnselectedForeground();
  
  public Color getUnselectedBackground();
  
  public Font getSelectedFont();
  
  public Font getUnselectedFont();
  
  public Border getFocusBorder();
  
  public Border getNoFocusBorder();
  
  public Format getTextFormat();
  
  /**
   * SwingConstants: LEFT, CENTER, RIGHT, LEADING or TRAILING.
   */ 
  public Integer getHorizontalAlignment();
  
  public void addPainterStyleListener(PainterStyleEventListener l);
  
  public void removePainterStyleListener(PainterStyleEventListener l);
}