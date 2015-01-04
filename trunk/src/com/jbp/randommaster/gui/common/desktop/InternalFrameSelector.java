package com.jbp.randommaster.gui.common.desktop;

import javax.swing.JInternalFrame;

/**
 * 
 * <code>InternalFrameSelector</code> provides  
 * the interface to control the internal frame selection
 * on the <code>EnhancedDesktop</code> instance.
 * 
 * @see EnhancedDesktop
 * 
 * @author plchung
 *
 */
public interface InternalFrameSelector {

  public void add(JInternalFrame f);
  
  public void remove(JInternalFrame f);
  
  public JInternalFrame getSelected();

  /**
   * Notify by the <code>EnhancedDesktop</code> instance
   * that a <code>JInternalFrame</code> instance is selected
   * and the selector should update its status appropriately.
   * 
   * @param f The internal frame selected.
   */
  public void select(JInternalFrame f);
  
  public void addInternalFrameSelectorListener(
    InternalFrameSelectorListener l);
  
  public void removeInternalFrameSelectorListener(
    InternalFrameSelectorListener l);
}