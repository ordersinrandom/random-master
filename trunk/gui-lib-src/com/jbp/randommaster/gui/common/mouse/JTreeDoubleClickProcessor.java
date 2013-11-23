package com.jbp.randommaster.gui.common.mouse;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

public class JTreeDoubleClickProcessor implements DoubleClickProcessor {

  private JTree tree;
  private TreePath lastPath;
  
  public JTreeDoubleClickProcessor(JTree tree) {
    if (tree==null)
      throw new IllegalArgumentException("given tree cannot be null");
      
    this.tree=tree;
    lastPath=null;
  }

  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void beforeDoubleClick(MouseEvent e) {
    lastPath=tree.getPathForLocation(e.getX(), e.getY());
  }
  
  /**
   * Implementation of <code>DoubleClickProcessor</code>.
   */
  public void mouseDoubleClicked(MouseEvent e) {
    
    TreePath newPath=tree.getPathForLocation(e.getX(), e.getY());
    
    // compare the path
    if (lastPath==null || newPath==null)
      return;
    
    Object[] path1=lastPath.getPath();
    Object[] path2=newPath.getPath();
    
    if (path1==null || path2==null || path1.length!=path2.length || path1.length==0)
      return;
    
    boolean unmatched=false;
    for (int i=0;i<path1.length;i++) {
      if (!path1[i].equals(path2[i])) {
        unmatched=true;
        break;
      }
    }
    
    if (unmatched)
      return;
    
    pathDoubleClicked(newPath);
    
  }

  /**
   * Must implement this method to get the double clicked tree path.
   * Empty implementation, subclass override.
   * 
   * @param value The selected path of the JTree.
   */  
  protected void pathDoubleClicked(TreePath path) {
  }
  
}  