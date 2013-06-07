package com.jbp.randommaster.gui.common.mouse;

import javax.swing.JList;
import java.awt.event.MouseEvent;

/**
 * 
 * <code>JListDoubleClickProcessor</code> is a specific 
 * implementation of <code>DoubleClickProcessor</code> for
 * <code>JList</code>.
 * 
 * @author plchung
 *
 */
public class JListDoubleClickProcessor implements DoubleClickProcessor {

  @SuppressWarnings("rawtypes")
  private JList list;
  private int lastIndex;
  
  @SuppressWarnings("rawtypes")
  public JListDoubleClickProcessor(JList list) {
    if (list==null)
      throw new IllegalArgumentException("given list cannot be null");
      
    this.list=list;
    lastIndex=-1;
  }

  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void beforeDoubleClick(MouseEvent e) {
    lastIndex=list.getSelectedIndex();
  }
  
  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void mouseDoubleClicked(MouseEvent e) {
    int i=list.getSelectedIndex();
    if (lastIndex==i && lastIndex!=-1)
      itemDoubleClicked(list.getSelectedValue());
  }

  /**
   * Must implement this method to get the double clicked item.
   * Empty implementation, subclass override.
   * 
   * @param value The selected item of the JList.
   */  
  protected void itemDoubleClicked(Object value) {
  }
  
}  