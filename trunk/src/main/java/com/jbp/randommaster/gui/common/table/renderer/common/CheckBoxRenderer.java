package com.jbp.randommaster.gui.common.table.renderer.common;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jbp.randommaster.gui.common.table.renderer.RenderersFacade;

public class CheckBoxRenderer extends RenderersFacade {
  
  private JCheckBox checkBox;
  
  public CheckBoxRenderer() {
    super();
    checkBox=new JCheckBox();
    checkBox.setHorizontalAlignment(SwingConstants.CENTER);
  }
  
  
  public Component getTableCellRendererComponent(JTable table, Object value, 
          boolean isSelected, boolean hasFocus, int row, int column) {
    
    Component ref=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
    if (value!=null && (value instanceof Boolean)) {
      checkBox.setSelected(((Boolean) value).booleanValue());
      checkBox.setBackground(ref.getBackground());
      return checkBox;
    }
    else return ref;
  }
  
}