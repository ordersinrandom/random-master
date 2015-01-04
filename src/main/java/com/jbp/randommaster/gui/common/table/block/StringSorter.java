package com.jbp.randommaster.gui.common.table.block;

import java.text.Collator;

/**
 * 
 * <code>StringSorter</code> provides the default implementation
 * of string cell sorting.
 * 
 * @author plchung
 *
 */
public class StringSorter extends ComparatorSorter {
  public StringSorter() {
    super(Collator.getInstance());
  }
  
  public StringSorter(int targetRow) {
    super(targetRow, Collator.getInstance());
  }
}


/* OLD IMPLEMENTATION
public class StringSorter implements BlockSorter {

  private int targetRow;

  public StringSorter() {
    targetRow=0;
  }
  
  public StringSorter(int targetRow) {
    this.targetRow=targetRow;
  }

  public List sortBlocks(java.util.List blocks, 
              int columnIndex, boolean ascending) {
    List result=new LinkedList();
    if (ascending) {
      while (!blocks.isEmpty()) {
        Block my=(Block) blocks.remove(0);
        boolean matched=false;
        int targetIndex=-1;
        for (int k=0;k<result.size();k++) {
          Block b=(Block) result.get(k);
          int v=Collator.getInstance().compare(
            my.getValueAt(columnIndex, targetRow), b.getValueAt(columnIndex, targetRow));

          if (v<0) {
            matched=true;
            targetIndex=k;
            break;
          }
        }
        if (matched)
          result.add(targetIndex, my);
        else result.add(my);
      }
    }
    else {
      while (!blocks.isEmpty()) {
        Block my=(Block) blocks.remove(0);
        boolean matched=false;
        int targetIndex=-1;
        for (int k=0;k<result.size();k++) {
          Block b=(Block) result.get(k);
          int v=Collator.getInstance().compare(
            my.getValueAt(columnIndex, targetRow), b.getValueAt(columnIndex, targetRow));

          if (v>0) {
            matched=true;
            targetIndex=k;
            break;
          }
        }
        if (matched)
          result.add(targetIndex, my);
        else result.add(my);
      }
    }
    return result;
  }
  
}*/