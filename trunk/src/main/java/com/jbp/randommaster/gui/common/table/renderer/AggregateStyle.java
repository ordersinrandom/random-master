package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Color;
import java.awt.Font;
import java.text.Format;
import javax.swing.border.Border;

import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEvent;
import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEventListener;

/**
 * 
 * <code>AggregateStyle</code> is internally used by 
 * <code>BasicPainter</code>.
 * 
 * @author plchung
 *
 */
class AggregateStyle extends PainterStyleAdapter implements PainterStyleEventListener {
  
  /*
  private Color selectedForeground;
  private Color selectedBackground;
  private Color unselectedForeground;
  private Color unselectedBackground;
  private Font selectedFont;
  private Font unselectedFont;
  private Border focusBorder;
  private Border noFocusBorder;
  private Format textFormat;
  private Integer horizontalAlignment;
  */
  // keep the references to the current style object in use
  // for each particular area: font, background...etc.
  private PainterStyle currentSelectedForegroundStyle;
  private PainterStyle currentSelectedBackgroundStyle;
  private PainterStyle currentUnselectedForegroundStyle;
  private PainterStyle currentUnselectedBackgroundStyle;
  private PainterStyle currentSelectedFontStyle;
  private PainterStyle currentUnselectedFontStyle;
  private PainterStyle currentFocusBorderStyle;
  private PainterStyle currentNoFocusBorderStyle;
  private PainterStyle currentTextFormatStyle;
  private PainterStyle currentHorizontalAlignmentStyle;

  
  public AggregateStyle() {
  }

  public void addStyle(final PainterStyle s) {
    // selected foreground
    if (currentSelectedForegroundStyle==null || s.getSelectedForeground()!=null) {
      if (currentSelectedForegroundStyle!=null)
        currentSelectedForegroundStyle.removePainterStyleListener(this);
      currentSelectedForegroundStyle=s;
      s.addPainterStyleListener(this);
      
    }
    
    // selected background  
    if (currentSelectedBackgroundStyle==null || s.getSelectedBackground()!=null) {
      if (currentSelectedBackgroundStyle!=null) {
        currentSelectedBackgroundStyle.removePainterStyleListener(this);
      }
      s.addPainterStyleListener(this);
      currentSelectedBackgroundStyle=s;
    }
    
    // unselected foreground
    if (currentUnselectedForegroundStyle==null || s.getUnselectedForeground()!=null) {
      if (currentUnselectedForegroundStyle!=null) {
        currentUnselectedForegroundStyle.removePainterStyleListener(this);
      }

      s.addPainterStyleListener(this);
      currentUnselectedForegroundStyle=s;
    }
    
    // unselected background
    if (currentUnselectedBackgroundStyle==null || s.getUnselectedBackground()!=null) {
      if (currentUnselectedBackgroundStyle!=null) {
        currentUnselectedBackgroundStyle.removePainterStyleListener(this);
      }

      s.addPainterStyleListener(this);
      currentUnselectedBackgroundStyle=s;
    }
    
    // selected font
    if (currentSelectedFontStyle==null || s.getSelectedFont()!=null) {
      if (currentSelectedFontStyle!=null) {
        currentSelectedFontStyle.removePainterStyleListener(this);
      }
      s.addPainterStyleListener(this);
      currentSelectedFontStyle=s;
    }
    
    // unselected font
    if (currentUnselectedFontStyle==null || s.getUnselectedFont()!=null) {
      if (currentUnselectedFontStyle!=null) {
        currentUnselectedFontStyle.removePainterStyleListener(this);
      }

      s.addPainterStyleListener(this);
      currentUnselectedFontStyle=s;
    }
    
    // focus border
    if (currentFocusBorderStyle==null || s.getFocusBorder()!=null) {
      if (currentFocusBorderStyle!=null) {
        currentFocusBorderStyle.removePainterStyleListener(this);
      }
      s.addPainterStyleListener(this);
      currentFocusBorderStyle=s;
    }
    
    // no focus border
    if (currentNoFocusBorderStyle==null || s.getNoFocusBorder()!=null) {
      if (currentNoFocusBorderStyle!=null) {
        currentNoFocusBorderStyle.removePainterStyleListener(this);
      }

      s.addPainterStyleListener(this);
      currentNoFocusBorderStyle=s;
    }
    
    // text format
    if (currentTextFormatStyle==null || s.getTextFormat()!=null) {
      if (currentTextFormatStyle!=null) {
        currentTextFormatStyle.removePainterStyleListener(this);
      }
      s.addPainterStyleListener(this);
      currentTextFormatStyle=s;
    }
    // alignment
    if (currentHorizontalAlignmentStyle==null || s.getHorizontalAlignment()!=null) {
      if (currentHorizontalAlignmentStyle!=null) {
        currentHorizontalAlignmentStyle.removePainterStyleListener(this);
      }
      s.addPainterStyleListener(this);
      currentHorizontalAlignmentStyle=s;
    }
  }

  public Integer getHorizontalAlignment() {
    return currentHorizontalAlignmentStyle==null? 
      null : currentHorizontalAlignmentStyle.getHorizontalAlignment();
  }

  public Color getSelectedForeground() {
    return currentSelectedForegroundStyle==null?
      null : currentSelectedForegroundStyle.getSelectedForeground();
  }
  
  public Color getSelectedBackground() {
    return currentSelectedBackgroundStyle==null?
      null : currentSelectedBackgroundStyle.getSelectedBackground();
  }
 
  public Color getUnselectedForeground() {
    return currentUnselectedForegroundStyle==null?
      null : currentUnselectedForegroundStyle.getUnselectedForeground();
  }
  
  public Color getUnselectedBackground() {
    return currentUnselectedBackgroundStyle==null?
      null : currentUnselectedBackgroundStyle.getUnselectedBackground();
  }
  
  public Font getSelectedFont() {
    return currentSelectedFontStyle==null?
      null : currentSelectedFontStyle.getSelectedFont();
  }
  
  public Font getUnselectedFont() {
    return currentUnselectedFontStyle==null?
      null : currentUnselectedFontStyle.getUnselectedFont();
  }
  
  public Border getFocusBorder() {
    return currentFocusBorderStyle==null?
      null : currentFocusBorderStyle.getFocusBorder();
  }
  
  public Border getNoFocusBorder() {
    return currentNoFocusBorderStyle==null?
      null : currentNoFocusBorderStyle.getNoFocusBorder();
  }
  
  public Format getTextFormat() {
    return currentTextFormatStyle==null?
      null : currentTextFormatStyle.getTextFormat();
  }

  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void selectedForegroundStyleChanged(PainterStyleEvent e) {
    if (currentSelectedForegroundStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.SELECTED_FOREGROUND_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void selectedBackgroundStyleChanged(PainterStyleEvent e) {
    if (currentSelectedBackgroundStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.SELECTED_BACKGROUND_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void unselectedForegroundStyleChanged(PainterStyleEvent e) {
    if (currentUnselectedForegroundStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.UNSELECTED_FOREGROUND_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void unselectedBackgroundStyleChanged(PainterStyleEvent e) {
    if (currentUnselectedBackgroundStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.UNSELECTED_BACKGROUND_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void selectedFontStyleChanged(PainterStyleEvent e) {
    if (currentSelectedFontStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.SELECTED_FONT_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void unselectedFontStyleChanged(PainterStyleEvent e) {
    if (currentUnselectedFontStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.UNSELECTED_FONT_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void focusBorderStyleChanged(PainterStyleEvent e) {
    if (currentFocusBorderStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.FOCUS_BORDER_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void noFocusBorderStyleChanged(PainterStyleEvent e) {
    if (currentNoFocusBorderStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.NO_FOCUS_BORDER_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void textFormatStyleChanged(PainterStyleEvent e) {
    if (currentTextFormatStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.TEXT_FORMAT_CHANGED));
  }
  
  /**
   * Implementation of <code>PainterStyleEventListener</code>.
   */
  public void horizontalAlignmentStyleChanged(PainterStyleEvent e) {
    if (currentHorizontalAlignmentStyle==e.getStyle())
      firePainterStyleEvent(
        new PainterStyleEvent(this, 
        PainterStyleEvent.HORIZONTAL_ALIGNMENT_CHANGED));
  }

  
}