package com.jbp.randommaster.gui.common.table.block;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.jbp.randommaster.gui.common.table.block.BlockTableModel;
import com.jbp.randommaster.gui.common.table.block.Block;


/**
 * 
 * <code>AggregateCellRenderer</code> is a special table cell renderer
 * for <code>JTable</code> that using <code>BlockTableModel</code>. 
 * 
 * Everytime when the <code>getTableCellRendererComponent</code> method is 
 * invoked, it searches the <code>BlockTableModel</code> of the given
 * <code>JTable</code> instance and locates the right renderer in the block.
 * 
 * @see com.jandp.ui.common.table.block.Block#getCellRendererAt
 * 
 * @author plchung
 *
 */
public class AggregateCellRenderer implements TableCellRenderer {

  /**
   * Create a new instance of <code>AggregateCellRenderer</code>.
   *
   */
  public AggregateCellRenderer() {
  }

  /**
   * The static method to uses the renderers associated with the blocks
   * in the table.
   */
  public static void applyBlockRenderers(JTable table) {
    table.setDefaultRenderer(Object.class, new AggregateCellRenderer());
  }
/*  
  public static void applyBlockRenderers(TableColumnModel tcm) {
    int count=tcm.getColumnCount();
    AggregateCellRenderer r=new AggregateCellRenderer();
    for (int i=0;i<count;i++) {
      tcm.getColumn(i).setCellRenderer(r);
    }
  }*/

  /**
   * Implementation of <code>TableCellRenderer</code>.
   */  
  public Component getTableCellRendererComponent(JTable table, 
        Object value, boolean isSelected, boolean hasFocus, 
        int row, int column) {

    // convert the column (view index) to model index.
    TableColumn tc=table.getColumnModel().getColumn(column);

    if (tc==null)
      return null;
    
    TableCellRenderer r=findRenderer(table, row, tc.getModelIndex());
    if (r!=null) {
      return r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    else return null;
  }
  
  /**
   * Utility method to find the renderer in the <code>BlockTableModel</code>.
   */
  private TableCellRenderer findRenderer(JTable table, int row, int column) {
    
    BlockTableModel tableModel=(BlockTableModel) table.getModel();
    Block b=tableModel.findBlock(row);
    if (b==null)
      return null;

    int[] blockRows=tableModel.findBlockRows(b);
    int relativeRow=row-blockRows[0];

//System.out.println("Finding renderer: "+b+", result="+b.getCellRendererAt(column, relativeRow));
    
    return b.getCellRendererAt(column, relativeRow);
  }
  
}