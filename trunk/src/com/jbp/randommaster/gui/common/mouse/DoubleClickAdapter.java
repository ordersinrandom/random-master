package com.jbp.randommaster.gui.common.mouse;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Iterator;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;

/**
 * 
 * <code>DoubleClickAdapter</code> is a mouse listener that
 * forward the mouse events to the given
 * <code>DoubleClickProcessor</code> instance with 
 * two methods: (1) beforeDoubleClick, (2) mouseDoubleClicked.
 * 
 * @author plchung
 *
 */
public class DoubleClickAdapter implements MouseListener {
    
  protected long lastClick;
  protected long gap;
  
  @SuppressWarnings("rawtypes")
  protected LinkedHashSet processors;
  
  /**
   * Create a new instance of <code>DoubleClickAdapter</code>
   * @param gap The max time gap between the two mouse click.
   */
  @SuppressWarnings("rawtypes")
public DoubleClickAdapter(long gap) {
    lastClick=0;
    this.gap=gap;
    processors=new LinkedHashSet();
  }
  
  public DoubleClickAdapter() {
    this(400L);
  }
  
  public DoubleClickAdapter(long gap, DoubleClickProcessor l) {
    this(gap);
    addDoubleClickProcessor(l);
  }
  
  public DoubleClickAdapter(DoubleClickProcessor l) {
    this();
    addDoubleClickProcessor(l);
  }
  
  @SuppressWarnings("unchecked")
public void addDoubleClickProcessor(DoubleClickProcessor l) {
    processors.add(l);
  }
  
  public void removeDoubleClickProcessor(DoubleClickProcessor l) {
    processors.remove(l);
  }
  
    
  public void mouseClicked(MouseEvent e) {
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }
  public void mousePressed(MouseEvent e) {
  }
  @SuppressWarnings("rawtypes")
public void mouseReleased(MouseEvent e) {
    // if not left click, forget it.
    int m=e.getModifiers();
    if ((m & InputEvent.BUTTON1_MASK)==0)
      return;

    Date d=new Date();
      
    if (d.getTime()-lastClick<gap) {
      LinkedHashSet targets=(LinkedHashSet) processors.clone();
      for (Iterator it=targets.iterator();it.hasNext();) {
        DoubleClickProcessor l=(DoubleClickProcessor) it.next();
        l.mouseDoubleClicked(e);
      }
    }
    else {
      LinkedHashSet targets=(LinkedHashSet) processors.clone();
      for (Iterator it=targets.iterator();it.hasNext();) {
        DoubleClickProcessor l=(DoubleClickProcessor) it.next();
        l.beforeDoubleClick(e);
      }
    }
    lastClick=d.getTime();
  }    
}
