package com.jbp.randommaster.gui.common.table.renderer;


import javax.swing.JTable;

/**
 * 
 * <code>RenderProcessAdapter</code> is a convenient implementation
 * of <code>RenderProcess</code>, and subclass of 
 * <code>AbstractRenderProcess</code>.
 * 
 * @author plchung
 *
 */
public class RenderProcessAdapter extends AbstractRenderProcess {

  /**
   * Create an instance of <code>RenderProcessAdapter</code>.
   *
   */
  public RenderProcessAdapter() {
    super();
  }

  /**
   * Empty implementation.
   */
  public void drawText(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {
  }
                      
  /**
   * Empty implementation.
   */
  public void setHorizontalAlignment(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {  
  }

  /**
   * Empty implementation.
   */
  public void drawUnselected(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {
  }
  
  /**
   * Empty implementation.
   */
  public void drawSelected(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {
  }

  /**
   * Empty implementation.
   */
  public void drawFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {
  }
  
  /**
   * Empty implementation.
   */
  public void drawNoFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column) {
  }
                          
}