package com.jbp.randommaster.gui.common.desktop;

import java.util.EventObject;
import javax.swing.JInternalFrame;

public class InternalFrameSelectorEvent extends EventObject {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 3666299157100319182L;

  protected JInternalFrame internalFrame;
  
  public InternalFrameSelectorEvent(InternalFrameSelector s, JInternalFrame f) {
    super(s);
    internalFrame=f;
  }
  
  public InternalFrameSelector getSelector() {
    return (InternalFrameSelector) super.getSource();
  }
  
  public JInternalFrame getInternalFrame() {
    return internalFrame;
  }
  
}