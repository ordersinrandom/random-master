package com.jbp.randommaster.gui.common.table.block;

public interface TreeBlock extends Block {
  
  public TreeBlock[] getChildren();

  public TreeBlock getChildAt(int index);
  
  public TreeBlock setChildAt(int index, TreeBlock newChild);

  public TreeBlock getParent();
  
  public void setParent(TreeBlock p);
  
  public void appendChild(TreeBlock c);
  
  public TreeBlock removeChild(TreeBlock c);
  
  public int getChildIndex(TreeBlock child);
  
  public Object getTreeNodeValue();
  
  public void setTreeNodeValue(Object obj);

  public void addTreeBlockChangeListener(TreeBlockChangeListener l);
  
  public void removeTreeBlockChangeListener(TreeBlockChangeListener l);
}