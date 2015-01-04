package com.jbp.randommaster.gui.common.layout;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;

public class GridBagUtil {
  
  /**
   * Set the minimum size of this JComponent
   * the same as the preferred size.
   * <br/>
   * Usually it should be used against JTextField when it is put
   * to a panel with GridBagLayout.
   */
  public static void unifyMinimumAsPreferredSize(JComponent c) {
    unifyMinimumAsPreferredSize(new JComponent[] { c });
  }
  
  /**
   * Set the minimum size of the JComponents
   * the same as their corresponding preferred size.
   * <br/>
   * Usually it should be used against JTextField when it is put
   * to a panel with GridBagLayout.
   */
  public static void unifyMinimumAsPreferredSize(JComponent[] c) {
    for (int i=0;i<c.length;i++) {
      Dimension prefSize=c[i].getPreferredSize();
      c[i].setMinimumSize(prefSize);
    }
  }
  
  
  public static void layoutVertically(Container parent, Component[] child, GridBagConstraints base) {
    if (child==null || child.length==0)
      throw new IllegalArgumentException("invalid number of child components");
    if (parent==null)
      throw new IllegalArgumentException("parent container is null");
    if (!(parent.getLayout() instanceof GridBagLayout))
      throw new IllegalArgumentException("parent container is not in grid bag layout");
      
    GridBagConstraints g=new GridBagConstraints();
    // copy the basic properties
    g.anchor=base.anchor;
    g.fill=base.fill;
    g.insets=base.insets;
    g.ipadx=base.ipadx;
    g.ipady=base.ipady;
    
    for (int i=0;i<child.length;i++) {
      g.gridx=0;
      g.gridy=i;
      g.gridwidth=1;
      g.gridheight=1;
      g.weightx=1.0;
      g.weighty=0.0;
      parent.add(child[i], g);
    }
  }
  
  /**
   * Layout an array of components as a table form. 
   * @param parent The container object.
   * @param child The array of components to layout.
   * @param c The given initial <code>SimpleGridBagConstraints</code> to use.
   * @param columnCount The total number of columns
   * @param columnAnchor The anchor value of each column. 
   * The array size must be equal to <code>columnCount</code>.
   * @param columnFill The fill value of each column.
   * The array size must be equal to <code>columnCount</code>.
   * @param columnWeight The weight value of each column.
   * The array size must be equal to <code>columnCount</code>.
   * @return The input parameter "c" after laid out.
   */
  public static SimpleGridBagConstraints layoutTable(
      Container parent, Component[] child, SimpleGridBagConstraints c,
      int columnCount, int[] columnAnchor, int[] columnFill, 
      double[] columnWeight) {

    int beginningCol=c.gridx;
    int endingCol=c.gridx+columnCount-1;
    int currentCol=beginningCol;
    for (int i=0;i<child.length;i++) {
      c.anchorChange(columnAnchor[currentCol]);
      c.fillChange(columnFill[currentCol]);
      c.weightx=columnWeight[currentCol];
      parent.add(child[i], c);

      c.nextColumn();
      currentCol++;
      
      if (currentCol>endingCol) {
        currentCol=beginningCol;
        c.nextRow();
        c.gridx=beginningCol;
      }
    }
    return c;
  }
}