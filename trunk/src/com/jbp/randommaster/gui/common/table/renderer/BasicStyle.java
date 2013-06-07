package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Color;
import java.awt.Font;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEvent;

/**
 * 
 * <code>BasicStyle</code> is the implementation of the
 * default rendering style of interface <code>PainterStyle</code>.
 * 
 * @author plchung
 *
 */
public class BasicStyle extends PainterStyleAdapter implements PropertyChangeListener {
  
//  private static final Color selectedBackground=Color.yellow;
//  private static final Color unselectedBackground=UIManager.getLookAndFeelDefaults().getColor("Table.background");
  
//  private static final Color selectedForeground=UIManager.getLookAndFeelDefaults().getColor("Table.foreground");
//  private static final Color unselectedForeground=UIManager.getLookAndFeelDefaults().getColor("Table.foreground");
  
//  private static final Font selectedFont=UIManager.getLookAndFeelDefaults().getFont("Table.font").deriveFont(Font.BOLD, 14);
//  private static final Font unselectedFont=UIManager.getLookAndFeelDefaults().getFont("Table.font");
  
  private static final Border focusBorder=new EmptyBorder(1,1,1,1);
  private static final Border noFocusBorder=new EmptyBorder(1,1,1,1);
  
  private static final Format textFormat=new NullFormat();

  /**
   * Create a new instance of <code>BasicStyle</code>.
   *
   */  
  public BasicStyle() {
    super();
    
    UIManager.getLookAndFeelDefaults().addPropertyChangeListener(this);
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Border getFocusBorder(){
    return focusBorder;
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Border getNoFocusBorder(){
    return noFocusBorder;
  }

  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Format getTextFormat() {
    return textFormat;
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Integer getHorizontalAlignment() {
    return new Integer(SwingConstants.LEFT);
  }
  
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Color getSelectedForeground() {
//    return selectedForeground;
    return UIManager.getLookAndFeelDefaults().getColor("Table.foreground");

  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Color getUnselectedForeground() {
//    return unselectedForeground;
    return UIManager.getLookAndFeelDefaults().getColor("Table.foreground");
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Color getSelectedBackground() {
    return Color.yellow;
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Color getUnselectedBackground() {
    return UIManager.getLookAndFeelDefaults().getColor("Table.background");
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Font getSelectedFont() {
    return UIManager.getLookAndFeelDefaults().getFont("Table.font").deriveFont(Font.BOLD);
    //return UIManager.getFont("Table.font").deriveFont(Font.BOLD);
    //return UIManager.getLookAndFeelDefaults().getFont("Table.font").deriveFont(Font.BOLD);
  }
  
  /**
   * Implementation of <code>PainterStyle</code>.
   */
  public Font getUnselectedFont() {
    return UIManager.getLookAndFeelDefaults().getFont("Table.font");
//    Font f=UIManager.getFont("Table.font");
//    Font f=UIManager.getLookAndFeelDefaults().getFont("Table.font");
//System.out.println("Basic Style font>>>> "+f);    
//    return f;
  }

  /**
   * PropertyChangeListener implementation.
   */
  public void propertyChange(PropertyChangeEvent e) {
    String n=e.getPropertyName();
    if ("Table.foreground".equals(n)) {
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.SELECTED_FOREGROUND_CHANGED));
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.UNSELECTED_FOREGROUND_CHANGED));
    }
    else if ("Table.background".equals(n)) {
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.SELECTED_BACKGROUND_CHANGED));
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.UNSELECTED_BACKGROUND_CHANGED));
    }
    else if ("Table.font".equals(n)) {
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.SELECTED_FONT_CHANGED));
      super.firePainterStyleEvent(
        new PainterStyleEvent(
        this, PainterStyleEvent.UNSELECTED_FONT_CHANGED));
    }
  }
  
  /**
   * 
   * <code>NullFormat</code> is the <code>Format</code>
   * interface implementation of
   * default text format presentation. It does nothing
   * but simply prints the content.
   * 
   * @author plchung
   *
   */
  @SuppressWarnings("serial")
private static class NullFormat extends Format {
    public NullFormat() {
      super();
    }
    /**
     * Implementation of <code>Format</code> interface.
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
      return toAppendTo.append(obj);    
    }
    
    /**
     * Implementation of <code>Format</code> interface.
     */
    public Object parseObject(String source, ParsePosition pos) {
      return source;
    }
  }
}