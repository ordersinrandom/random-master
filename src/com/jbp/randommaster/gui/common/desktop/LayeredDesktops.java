package com.jbp.randommaster.gui.common.desktop;

import java.awt.Component;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;

import com.jbp.randommaster.gui.common.desktop.CommonDesktopManager;
import com.jbp.randommaster.gui.common.desktop.EnhancedDesktop;

/**
 * 
 * <code>LayeredDesktops</code> implements a JLayeredPane that
 * holds a number of EnhancedDesktop instances, and provide API for desktop
 * swapping. 
 * 
 * @author plchung
 *
 */
public class LayeredDesktops extends JLayeredPane implements LayerSelectionListener {
  
	private static final long serialVersionUID = 7966906195060478328L;

/**
   * Mapping from key object to JDesktopPane object.
   */
  @SuppressWarnings("rawtypes")
  protected Map desktops;
  
  /**
   * Mapping from key object to the corresponding <code>JScrollPane</code>
   * holding the <code>JDesktopPane</code> object. 
   */
  @SuppressWarnings("rawtypes")
  protected Map scrolls;
  
  /**
   * Mapping from key object to other non-JDesktopPane components.
   */
  @SuppressWarnings("rawtypes")
  protected Map others;
  
  /**
   * The preferred size of this desktops.
   */
  private Dimension preferredSize;
  
  /**
   * The layer selection controller.
   */
  private LayerController layerController;

  @SuppressWarnings("rawtypes")
  public LayeredDesktops() {
    
    desktops=new LinkedHashMap();
    scrolls=new LinkedHashMap();
    others=new LinkedHashMap();
    
    // notify the scroll panes to resize when this object is resized.
    super.addComponentListener(
      new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          Dimension d=e.getComponent().getSize();
          refreshScrollSize(d);
        }
      }
    );
  }


  @SuppressWarnings("rawtypes")
  private void refreshScrollSize(Dimension d) {
    for (Iterator it=scrolls.values().iterator();it.hasNext();) {
      JComponent c=(JComponent) it.next();
      c.setSize(d);
    }
    for (Iterator it=others.values().iterator();it.hasNext();) {
      JComponent c=(JComponent) it.next();
      c.setSize(d);
    }
//    this.revalidate();
  }
  
  public void setBounds(int x, int y, int width, int height) {
    refreshScrollSize(new Dimension(width,height));
    super.setBounds(x,y,width,height);
  }
  
  /**
   * Get the <code>EnhancedDesktop</code> instance previously added.
   * 
   * @param key The key to retrieve the desktop.
   * @return The EnhancedDesktop instance or null if not found.
   */
  public EnhancedDesktop getDesktop(Object key) {
    return (EnhancedDesktop) desktops.get(key);
  }
  
  @SuppressWarnings("rawtypes")
  public Collection getDesktopKeys() {
    return desktops.keySet();
  }
  
  /**
   * Add a <code>EnhancedDesktop</code> instance to this
   * <code>LayeredDesktops</code> instance.
   * 
   * A <code>CommonDesktopManager</code> and a <code>JScrollPane</code>
   * will be automatically created for the <code>EnhancedDesktop</code>
   * instance.
   * 
   * @param key
   * @param desktop
   * @return The new <code>CommonDesktopManager</code> instance for 
   * the desktop.
   */
  @SuppressWarnings("unchecked")
  public CommonDesktopManager setDesktop(Object key, EnhancedDesktop desktop) {
    JScrollPane scroll=new JScrollPane(desktop);
    desktops.put(key, desktop);
    scrolls.put(key, scroll);
    
    CommonDesktopManager cdm=new CommonDesktopManager(desktop, scroll);
    desktop.setDesktopManager(cdm);
    
    Dimension p=getPreferredSize();
    desktop.setPreferredSize(p);
    // set this so the layered pane will know this has to be repaint
    // when position changes.
//System.out.println("setting scroll size="+p);    
//    scroll.setSize(p);
    
    super.add(scroll, JLayeredPane.DEFAULT_LAYER);
    super.setPosition(scroll, desktops.size()+others.size()-1);
    super.revalidate();
    
    // notify the layer controller if it is here.
    if (layerController!=null)
      layerController.layerAdded(key);
    
    return cdm;
  }
  
  @SuppressWarnings("unchecked")
  public void setOtherJComponent(Object key, JComponent comp) {
    Dimension p=getPreferredSize();
    comp.setPreferredSize(p);
    JScrollPane sc=new JScrollPane(comp);

    others.put(key, sc);
//    comp.setSize(p);
    super.add(sc, JLayeredPane.DEFAULT_LAYER);
    super.setPosition(sc, desktops.size()+others.size()-1);
    super.revalidate();
    // notify the layer controller if it is here.
    if (layerController!=null)
      layerController.layerAdded(key);

  }
  
  public void setPreferredSize(Dimension d) {
    preferredSize=d;
  }
  
  @SuppressWarnings("rawtypes")
  public Dimension getPreferredSize() {
    if (preferredSize==null) {
      int maxWidth=0;
      int maxHeight=0;
      for (Iterator it=desktops.values().iterator();it.hasNext();) {
        JDesktopPane desk=(JDesktopPane) it.next();
        Dimension d=desk.getPreferredSize();
        if (d.height>maxHeight)
          maxHeight=d.height;
        if (d.width>maxWidth)
          maxWidth=d.width;
      }
      return new Dimension(maxWidth, maxHeight);
    }
    else return preferredSize;
  }
  
/*
BUGGY
  public boolean isOptimizedDrawingEnabled() {
    return false;
  }
*/
  /**
   * Set the <code>LayerController</code> 
   * of this <code>LayeredDesktops</code> instance.
   * @param l
   */
  @SuppressWarnings("rawtypes")
  public void setLayerController(LayerController l) {
    if (layerController!=null) 
      layerController.removeLayerSelectionListener(this);
    
    layerController=l;
    layerController.addLayerSelectionListener(this);
    for (Iterator it=scrolls.keySet().iterator();it.hasNext();) {
      Object k=it.next();
      layerController.layerAdded(k);
    }
  }
  
  /**
   * The <code>LayerSelectionListener</code> implementation.
   */
  public void layerSelected(LayerSelectionEvent e) {
    Object key=e.getKey();
    if (key==null)
      throw new IllegalStateException("layer key is null from layer selection event");

    JComponent target=(JComponent) scrolls.get(key);
    if (target==null) {
      // try to grab from "others" map.
      target=(JComponent) others.get(key);
      if (target==null)
        throw new IllegalStateException("the given key is not found");
    }

//System.out.println("LayeredDesktop: moving "+target+" to front");
    super.moveToFront(target);
    target.requestFocus();

//System.out.println("LayeredDesktop: moving target "+target);
//System.out.println("target bounds="+target.getBounds());

//get all
/*
java.awt.Component[] c=super.getComponents();
for (int i=0;i<c.length;i++) {
  int pos=super.getPosition(c[i]);
  System.out.println(c[i].getClass().getName()+" : "+pos);
}
super.revalidate();
*/
    
  }
  
  
  // performance enhancement related.
  // could be removed, and this object still functional.
  
  /**
   * Overrided for performance enhancement.
   */
  protected void addImpl(Component comp, Object constraints, int index) {
    int layer=DEFAULT_LAYER.intValue();

    if (constraints instanceof Integer) {
      layer=((Integer)constraints).intValue();
      setLayer(comp, layer);
    } 
    else layer=super.getLayer(comp);

    int pos=super.insertIndexForLayer(layer, index);
    super.addImpl(comp, constraints, pos);
//    comp.validate();
//    comp.repaint();
  }

  /**
   * Overrided for performance enhancement.
   */
  public void remove(int index) {
    Component c=getComponent(index);
    super.remove(index);
    if (c!=null && !(c instanceof JComponent))
      super.getComponentToLayer().remove(c);
  }


  /**
   * Overrided for performance enhancement,
   * and always returns false.
   * 
   * @return false
   */
  public boolean isOptimizedDrawingEnabled() {
    return false;
  }

  
}