package com.jbp.randommaster.gui.common.mouse;

import java.awt.event.MouseEvent;
import javax.swing.JTable;

/**
 * 
 * <code>JTableDoubleClickProcessor</code> implements
 * <code>DoubleClickProcessor</code> for handling double clicks
 * on table cell of jtable. 
 * 
 * @author plchung
 *
 */
public class JTableDoubleClickProcessor implements DoubleClickProcessor {
  
  public static final int CHECK_BY_ROW=1;
  public static final int CHECK_BY_CELL=2;
  
  private int checkCondition;
  
  private JTable table;
  private int lastRowIndex;
  private int lastColIndex;
  
  public JTableDoubleClickProcessor(JTable table, int checkCondition) {
    if (table==null)
      throw new IllegalArgumentException("given table cannot be null");
    
    this.checkCondition=checkCondition;
    
    this.table=table;
    lastRowIndex=-1;
    lastColIndex=-1;
  }

  public JTableDoubleClickProcessor(JTable table) {
    this(table, CHECK_BY_ROW);
  }
  
  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void beforeDoubleClick(MouseEvent e) {
    
    lastRowIndex=table.rowAtPoint(e.getPoint());
    lastColIndex=table.columnAtPoint(e.getPoint());
  }
  
  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void mouseDoubleClicked(MouseEvent e) {
    
    int ri=table.rowAtPoint(e.getPoint());
    int ci=table.columnAtPoint(e.getPoint());
    
    if (checkCondition==CHECK_BY_ROW) {
      if (lastRowIndex==ri && lastRowIndex!=-1)
        rowDoubleClicked(table, lastRowIndex);
    }
    else if (checkCondition==CHECK_BY_CELL) {
      if (lastRowIndex==ri && lastRowIndex!=-1 
          && lastColIndex==ci && lastColIndex!=-1)
        cellDoubleClicked(table, lastRowIndex, lastColIndex);
    }
  }

  /**
   * Must implement this method to get the double clicked row.
   * It will be called only when the check condition is "CHECK_BY_ROW".
   * Empty implementation. Subclass override.
   *
   * @param table The table object. 
   * @param row The double clicked row index.
   */  
  protected void rowDoubleClicked(JTable table, int row) {
  }
  
  /**
   * Must implement this method to get the double clicked cell.
   * It will be called only when the check condition is "CHECK_BY_CELL".
   * Empy imlementation. Subclass override.
   * 
   * @param table The table object.
   * @param row The view model row index.
   * @param column The view model column index.
   */
  protected void cellDoubleClicked(JTable table, int row, int column) {
  }
}