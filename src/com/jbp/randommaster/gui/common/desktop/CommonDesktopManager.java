package com.jbp.randommaster.gui.common.desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CommonDesktopManager extends DefaultDesktopManager {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = -3016782313179434927L;
	
  protected JDesktopPane desktop;
  protected JScrollPane desktopScroll;

  @SuppressWarnings("rawtypes")
  protected Collection maxFrames;
  
  @SuppressWarnings("rawtypes")
  protected Collection iconFrames;    

  protected Point viewPositionBeforeMaximize;
  
  // if -1 it means no grid.
  private int gridSize;

  /**
   * Create a <code>CommonDesktopManager</code> instance.
   * 
   * @param desktop The <code>JDesktop</code> object to manage.
   * @param scroll The <code>JScrollPane</code> object to support scrolling.
   */
  @SuppressWarnings("rawtypes")
  public CommonDesktopManager(JDesktopPane desktop, 
                              JScrollPane scroll) {
    super();
    // no grid by default
    gridSize=-1;
    
    maxFrames=new LinkedHashSet();
    iconFrames=new LinkedHashSet();
    
    this.desktop=desktop;
    this.desktopScroll=scroll;
    
//   force the empty icon ui the same package as the emty icon ui.
    UIDefaults defaults = UIManager.getDefaults(); 
    defaults.put( "DesktopIconUI",
               getClass().getPackage().getName() + ".EmptyDesktopIconUI"); 

    this.desktopScroll.addComponentListener(
      new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          refreshScrollPane();
        }
      }
    );
  }

  public void setGridSize(int size) {
    if (size==0)
      gridSize=-1;
    else gridSize=size;
  }

  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  public void endDraggingFrame(JComponent f) {
    super.endDraggingFrame(f);
    
    adjustByGrid(f);
    
    this.refreshScrollPane();
  }


  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  public void endResizingFrame(JComponent f) {
    super.endResizingFrame(f);
    
    adjustByGrid(f);
//   log
//Dimension d=f.getSize();
//System.out.println("new size: "+d.width+", "+d.height);    
    this.refreshScrollPane();
  }

  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  public void deiconifyFrame(JInternalFrame f) {
    super.deiconifyFrame(f);
    iconFrames.remove(f);
    this.refreshScrollPane();
  }

  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  @SuppressWarnings("unchecked")
  public void iconifyFrame(JInternalFrame f) {
    super.iconifyFrame(f);
    iconFrames.add(f);
    this.refreshScrollPane();
  }

  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  @SuppressWarnings("unchecked")
  public void maximizeFrame(JInternalFrame f) {
    
    // immediate change the view position to 0,0 
    // and the size of the desktop equal to the view port
    JViewport viewPort=desktopScroll.getViewport();
    Rectangle viewRect=viewPort.getViewRect();
    Dimension newSize=new Dimension(viewRect.width, viewRect.height);
    desktop.setSize(newSize);
    desktop.setPreferredSize(newSize);

    viewPositionBeforeMaximize=viewPort.getViewPosition();
    viewPort.setViewPosition(new Point(0,0));
    super.maximizeFrame(f);
    
    maxFrames.add(f);
  }

  /**
   * Implementation of <code>DesktopManager</code> instance.
   */
  public void minimizeFrame(JInternalFrame f) {
    
    super.minimizeFrame(f);
    JViewport viewPort=desktopScroll.getViewport();
    viewPort.setViewPosition(viewPositionBeforeMaximize);
    maxFrames.remove(f);
    this.refreshScrollPane();
  }

  /**
   * Utility to adjust the desktop size, objects position and 
   * refresh the scroll pane.
   *
   */
  public void refreshScrollPane() {
    // compute the minimum bounding rectangle of all intername frames    
    Rectangle minBoundRect=this.computeMinBoundRect();

    // get the viewport rectangle
    Rectangle viewRect=desktopScroll.getViewport().getViewRect();


    // find the union between min bound rect and the view rect.
    Rectangle union=SwingUtilities.computeUnion(minBoundRect.x, 
                                                minBoundRect.y, 
                                                minBoundRect.width, 
                                                minBoundRect.height, 
                                                new Rectangle(viewRect));

    Dimension newDesktopSize=null;
    if (hasMaximizedFrame()) {
      newDesktopSize=new Dimension(viewRect.width, viewRect.height);
    }
    else {// change the desktop size
      newDesktopSize=new Dimension(union.width, union.height);
    }
    desktop.setSize(newDesktopSize);
    desktop.setPreferredSize(newDesktopSize);

    // the transition of the objects
    Point transition=new Point(-1*union.x, -1*union.y);

    // move all internal frames.
    JInternalFrame[] all=desktop.getAllFrames();
    for (int i=0;i<all.length;i++) {
      Rectangle bounds=all[i].getBounds();
      all[i].setBounds(new Rectangle(bounds.x+transition.x,
                                     bounds.y+transition.y,
                                     bounds.width,
                                     bounds.height));
    }
    // change the view position
    desktopScroll.getViewport().setViewPosition(
          new Point(viewRect.x+transition.x,viewRect.y+transition.y));
  }
  
  /**
   * Utility to compute the minimum bounding rectangle of all internal frames.
   */
  private Rectangle computeMinBoundRect() {
    JInternalFrame[] all=desktop.getAllFrames();
    if (all.length==0)
      return desktop.getBounds();

    Rectangle minBoundRect=all[0].getBounds();
    if (all.length>1) {
      for (int i=1;i<all.length;i++) {
        minBoundRect=SwingUtilities.computeUnion(
            minBoundRect.x, 
            minBoundRect.y, 
            minBoundRect.width, 
            minBoundRect.height, 
            all[i].getBounds());
      }
    }
    return minBoundRect;
  }

  /**
   * Utility to check if there is a maximized frame.
   * @return
   */
  private boolean hasMaximizedFrame() {
    JInternalFrame[] all=desktop.getAllFrames();
    for (int i=0;i<all.length;i++) {
      if (all[i].isMaximum())
        return true;
    }
    return false;
  }

  // util to find the closest coordinate
  private int findClosestPoint(int currentPos) {
    int result=currentPos;
    if (currentPos%gridSize!=0) {
      int last=currentPos-currentPos%gridSize;
      int next=last+gridSize;
      int d1=currentPos-last;
      int d2=next-currentPos;
      if (d1<=d2) 
        result=last;
      else result=next;
    }
    return result;
  }

  /**
   * Finalize the location and size by the current grid size definition.
   * @param f
   */  
  private void adjustByGrid(JComponent f) {
    // if have grid setting
    if (gridSize>0) {
      Point currentPos=f.getLocation();
      int newX=findClosestPoint(currentPos.x);
      int newY=findClosestPoint(currentPos.y);
      
      Point newPos=new Point(newX, newY);
      if (!newPos.equals(currentPos))
        f.setLocation(newPos);
      
      Dimension currentSize=f.getSize();
      
      int newWidth=findClosestPoint(currentSize.width);
      int newHeight=findClosestPoint(currentSize.height);
      
      Dimension newSize=new Dimension(newWidth, newHeight);
      if (!newSize.equals(currentSize) && newSize.width!=0 && newSize.height!=0)
        f.setSize(newSize);
    }
  }
  
}