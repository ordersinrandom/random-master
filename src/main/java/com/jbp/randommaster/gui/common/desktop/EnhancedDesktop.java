package com.jbp.randommaster.gui.common.desktop;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.SwingUtilities;
import javax.swing.JDesktopPane;
import javax.swing.DesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;

public class EnhancedDesktop extends JDesktopPane implements 
            InternalFrameListener, InternalFrameSelectorListener {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 7305738872934113544L;
  
  protected InternalFrameSelector frameSelector;
  protected Dimension preferredSize;
  protected FramePositionFactory positionFactory;
  
  public EnhancedDesktop() {
    super();
    positionFactory=new DefaultFramePositionFactory();
  }

  public void setPositionFactory(FramePositionFactory f) {
    positionFactory=f;
  }

  public void addInternalFrame(JInternalFrame jif, Dimension size)  throws DesktopOperationException {
    if (size==null)
      jif.pack();
    else jif.setSize(size);
    
    this.add(jif);
    try {
      jif.setSelected(true);
    } catch (java.beans.PropertyVetoException pve) {
      throw new DesktopOperationException("unable to select the internal frame", pve);
    } finally {
      jif.setLocation(positionFactory.nextPosition());
      jif.show();
      
      DesktopManager dm=super.getDesktopManager();
      if (dm instanceof CommonDesktopManager) {
        ((CommonDesktopManager) dm).refreshScrollPane();
      }
    }
  }

  /**
   * Override the super class logic and register internal frame listener.
   */  
  public Component add(Component comp) {
    if (comp!=null && (comp instanceof JInternalFrame)) {
      JInternalFrame jif=(JInternalFrame) comp;
      jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
      jif.addInternalFrameListener(this);
      if (frameSelector!=null)
        frameSelector.add(jif);
    }
    return super.add(comp);
  }
  
  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameActivated(InternalFrameEvent e) {
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameActivated "+f.getTitle());

    if (frameSelector!=null)
      frameSelector.select(e.getInternalFrame());    
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameClosed(InternalFrameEvent e) {
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameClosed "+f.getTitle());

      // forget this frame.
      JInternalFrame jif=e.getInternalFrame();
      jif.removeInternalFrameListener(this);
      if (frameSelector!=null)  
        frameSelector.remove(jif);
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameClosing(InternalFrameEvent e) {
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameClosing "+f.getTitle());    
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameDeactivated(InternalFrameEvent e) { 
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameDeactivated "+f.getTitle());    
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameDeiconified(InternalFrameEvent e) { 
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameDeiconified "+f.getTitle());    
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameIconified(InternalFrameEvent e) {
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameIconified "+f.getTitle());    
  }

  /**
   * Implementation of <code>InternalFrameListener</code>.
   */
  public void internalFrameOpened(InternalFrameEvent e) { 
//    JInternalFrame f=e.getInternalFrame();
//    System.out.println("internalFrameOpened "+f.getTitle());    
  }
 
  
  /**
   * Implementation of the <code>InternalFrameSelectorListener</code>
   * interface.
   */
  public void internalFrameSelected(InternalFrameSelectorEvent f) {
    JInternalFrame jif=f.getInternalFrame();
//System.out.println("oh selector changed to "+jif.getTitle());    
    try {
      if (jif.isIcon())
        jif.setIcon(false);
      jif.setSelected(true);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }
  
  /**
   * Set the internal frame selector.
   * It changes the listener registration.
   */
  public void setInternalFrameSelector(InternalFrameSelector s) {
    if (frameSelector!=null)
      frameSelector.removeInternalFrameSelectorListener(this);
    
    frameSelector=s;
    if (frameSelector!=null) {
      frameSelector.addInternalFrameSelectorListener(this);
      JInternalFrame[] all=getAllFrames();
      for (int i=0;i<all.length;i++)
        frameSelector.add(all[i]);
    }
  }
  
  public void setPreferredSize(Dimension d) {
    this.preferredSize=d;
  }
  
  public Dimension getPreferredSize() {
    if (preferredSize==null) {
      JInternalFrame[] all=getAllFrames();
      if (all.length==0)
        return super.getPreferredSize();

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
      return minBoundRect.getSize();
    }
    else return preferredSize;
  }
}