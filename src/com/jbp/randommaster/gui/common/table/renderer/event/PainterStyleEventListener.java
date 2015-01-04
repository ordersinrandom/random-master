package com.jbp.randommaster.gui.common.table.renderer.event;

import java.util.EventListener;


/**
 * 
 * <code>PainterStyleEventListener</code> is the interface
 * that receives events when a particular <code>PainterStyle</code>
 * instance updated.
 * 
 * @author plchung
 *
 */
public interface PainterStyleEventListener extends EventListener {
  
  public void selectedForegroundStyleChanged(PainterStyleEvent e);
  
  public void selectedBackgroundStyleChanged(PainterStyleEvent e);
  
  public void unselectedForegroundStyleChanged(PainterStyleEvent e);
  
  public void unselectedBackgroundStyleChanged(PainterStyleEvent e);
  
  public void selectedFontStyleChanged(PainterStyleEvent e);
  
  public void unselectedFontStyleChanged(PainterStyleEvent e);
  
  public void focusBorderStyleChanged(PainterStyleEvent e);
  
  public void noFocusBorderStyleChanged(PainterStyleEvent e);
  
  public void textFormatStyleChanged(PainterStyleEvent e);
  
  public void horizontalAlignmentStyleChanged(PainterStyleEvent e);
  
}