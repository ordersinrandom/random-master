package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JLabel;

import javax.swing.border.EmptyBorder;

/**
 * 
 * <code>BasicPainter</code> is 
 * a specialized <code>JLabel</code> for table cell rendering.
 * 
 * @author plchung
 *
 */
@SuppressWarnings("serial")
public class BasicPainter extends JLabel implements CellPainter {

  private AggregateStyle aggregateStyle;

  public BasicPainter() {
    this(new BasicStyle());
  }
  
  public BasicPainter(PainterStyle basicStyle) {
    super();

    setOpaque(true);
    setBorder(new EmptyBorder(1, 1, 1, 1));
    
    aggregateStyle=new AggregateStyle();
    
    // add the default display
    addStyle(basicStyle);
  }
  
  public void addStyle(PainterStyle s) {
    aggregateStyle.addStyle(s);
  }
  
  public PainterStyle getFinalStyle() {
    return aggregateStyle;
  }
  
  public JLabel getRenderingLabel() {
    return this;  
  }

  /**
   * Notification from the <code>UIManager</code> that the look and feel
   * [L&F] has changed.
   * Replaces the current UI object with the latest version from the 
   * <code>UIManager</code>.
   *
   * @see JComponent#updateUI
   */
  public void updateUI() {
    super.updateUI(); 
    setForeground(null);
    setBackground(null);
  }

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public boolean isOpaque() { 
    Color back = getBackground();
    Component p = getParent(); 
    if (p != null) { 
        p = p.getParent(); 
    }
    // p should now be the JTable. 
    boolean colorMatch = (back != null) && (p != null) && 
    back.equals(p.getBackground()) && 
    p.isOpaque();
    return !colorMatch && super.isOpaque(); 
  }

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public void validate() {}

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public void revalidate() {}

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public void repaint(long tm, int x, int y, int width, int height) {}

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public void repaint(Rectangle r) { }

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {  
    // Strings get interned...
    if (propertyName=="text") {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  /**
   * Overridden for performance reasons.
   * See the <a href="#override">Implementation Note</a> 
   * for more information.
   */
  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }

}