package com.jbp.randommaster.gui.common.table.renderer;

import javax.swing.JTable;

/**
 * 
 * <code>RenderProcess</code> is the implementation interface
 * of those being added to <code>RenderersFacade</code>
 * using its <code>addRenderProcess()</code> method.
 *  
 * @see com.jandp.ui.common.table.renderer.RenderersFacade#addRenderProcess
 * 
 * @author plchung
 *
 */
public interface RenderProcess {

  /**
   * Render the table cell content by the given information.
   * @param p The painter in use.
   * @param table The table object containing the cell.
   * @param value The table cell data model value.
   * @param isSelected Indicate whether it is selected.
   * @param hasFocus Indicate whether it has focus.
   * @param row The table view row index. (not model index)
   * @param column The table view column index. (not model index)
   */
  public void render(CellPainter p,
          JTable table, Object value,
          boolean isSelected, boolean hasFocus, 
          int row, int column);  

}