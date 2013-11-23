package com.jbp.randommaster.gui.common.table.renderer;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * 
 * <code>RotateColorRenderProcess</code> is the implementation
 * of <code>RenderProcess</code> that draw different background
 * color on table cell according to their row index.
 * 
 * @author plchung
 *
 */
public class RotateColorRenderProcess extends RenderProcessAdapter {
  
  private Color[] rotatingColors;
  
  /**
   * Create a new instance of <code>RotateColorRenderProcess</code>,
   * which rotates by white and gray color.
   * 
   *
   */
  public RotateColorRenderProcess() {
    this(new Color[] {Color.white, new Color(210,210,210)});
  }

  /**
   * Create the new instance of <code>RotateColorRenderProcess</code>.
   * 
   * @param rotatingColors An array of <code>Color</code>
   * that rotates along the row index.
   */
  public RotateColorRenderProcess(Color[] rotatingColors) {
    super();
    
    if (rotatingColors==null || rotatingColors.length==0)
      throw new IllegalArgumentException("empty rotating colors input");
    
    this.rotatingColors=rotatingColors;
  }

  /**
   * Implementation of <code>RenderProcess</code> interface.
   */
  public void drawUnselected(CellPainter p,
                      JTable table, Object value,
                      boolean isSelected, boolean hasFocus, 
                      int row, int column) {
    super.drawUnselected(p, table, value, isSelected, hasFocus, row, column);
    JLabel l=p.getRenderingLabel();
    
    int r=row%rotatingColors.length;
    
    l.setBackground(rotatingColors[r]);
    
  }
}
