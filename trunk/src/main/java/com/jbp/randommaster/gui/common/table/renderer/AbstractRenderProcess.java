package com.jbp.randommaster.gui.common.table.renderer;


import javax.swing.JTable;

/**
 * 
 * <code>AbstractRenderProcess</code> is the implementation 
 * of the basic flow of table cell render process.
 * Implementation of <code>RenderProcess</code> should use 
 * its subclass <code>RenderProcessAdapter</code>.
 * 
 * @see com.jandp.ui.common.table.renderer.RenderProcessAdapter
 * @author plchung
 *
 */
public abstract class AbstractRenderProcess implements RenderProcess {

  /**
   * Create an instace of <code>AbstractRenderProcess</code>.
   *
   */
  public AbstractRenderProcess() {
  }

  /**
   * The implementation of render process flow: <br/>
   * (1) drawText, <br/> 
   * (2) setHorizontalAlignment, <br/>
   * (3) drawSelected/drawUnselected, <br/>
   * (4) drawFocus/drawNoFocus <br/>
   */
  public final void render(CellPainter p,
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus, 
                    int row, int column) {

    drawText(p, table, value, isSelected, hasFocus, row, column);

    setHorizontalAlignment(p, table, value, isSelected, hasFocus, row, column);

    if (isSelected) 
      drawSelected(p, table, value, isSelected, hasFocus, row, column);
    else drawUnselected(p, table, value, isSelected, hasFocus, row, column);
    
    if (hasFocus) 
      drawFocus(p, table, value, isSelected, hasFocus, row, column);
    else drawNoFocus(p, table, value, isSelected, hasFocus, row, column);
    
  }

  /**
   * Draw the text being displayed in the label cell.
   */
  public abstract void drawText(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column);

  /**
   * Set the horizontal alignment of the text being displayed.
   */                      
  public abstract void setHorizontalAlignment(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column);  

  /**
   * Draw unselected style such as background and font.
   */
  public abstract void drawUnselected(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column);

  /**
   * Draw selected style such as background and font.
   */  
  public abstract void drawSelected(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column);

  /**
   * Draw focused style such as background and font.
   */
  public abstract void drawFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column);
  
  /**
   * Draw no focused style such as background and font.
   */
  public abstract void drawNoFocus(CellPainter p,
                          JTable table, Object value,
                          boolean isSelected, boolean hasFocus, 
                          int row, int column);
                          

}