package com.jbp.randommaster.gui.common.table.renderer;

import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * 
 * <code>DefaultRenderProcess</code> is the package-only
 * access implementation of the basic table cell rendering.
 * 
 * @author plchung
 *
 */
final class DefaultRenderProcess extends AbstractRenderProcess {

  public DefaultRenderProcess() {
    super();
  }

  public void drawUnselected(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {
    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style!=null) {
      if (style.getUnselectedBackground()!=null)
        l.setBackground(style.getUnselectedBackground());
      if (style.getUnselectedForeground()!=null)
        l.setForeground(style.getUnselectedForeground());
      if (style.getUnselectedFont()!=null)
        l.setFont(style.getUnselectedFont());
    }
  }
  
  public void drawSelected(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {
    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style!=null) {
      if (style.getSelectedBackground()!=null)
        l.setBackground(style.getSelectedBackground());
      if (style.getSelectedForeground()!=null)
        l.setForeground(style.getSelectedForeground());
      if (style.getSelectedFont()!=null)
        l.setFont(style.getSelectedFont());
    }
  }

  public void drawFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {
    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style!=null) {
      if (style.getFocusBorder()!=null)
        l.setBorder(style.getFocusBorder());
    }
  }  
  
  public void drawNoFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {

    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style!=null) {
      if (style.getNoFocusBorder()!=null)
        l.setBorder(style.getNoFocusBorder());
    }
  }  

  public void drawText(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {

    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style==null || style.getTextFormat()==null)
      l.setText(value==null? "": value.toString());
    else {
      try {
        String s=style.getTextFormat().format(value);
        l.setText(s);
      } catch (Exception e1) {
        // error case.
        //e1.printStackTrace();
        // ignore, and dump string as result
        l.setText(value==null? "": value.toString());
      }
    }
  }

  public void setHorizontalAlignment(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {
    JLabel l=p.getRenderingLabel();
    PainterStyle style=p.getFinalStyle();
    if (style!=null && style.getHorizontalAlignment()!=null) {
      Integer a=style.getHorizontalAlignment();
      l.setHorizontalAlignment(a.intValue());
    }
  }

}