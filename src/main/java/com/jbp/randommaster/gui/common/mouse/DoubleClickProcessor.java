package com.jbp.randommaster.gui.common.mouse;

import java.util.EventListener;
import java.awt.event.MouseEvent;

/**
 *  
 * <code>DoubleClickProcessor</code> is an event listener
 * that receives MouseEvent objects forwarded by the 
 * <code>DoubleClickAdapter</code>.
 * 
 * @author plchung
 *
 */
public interface DoubleClickProcessor extends EventListener {
  
  public void beforeDoubleClick(MouseEvent e);
  
  public void mouseDoubleClicked(MouseEvent e);
  
}