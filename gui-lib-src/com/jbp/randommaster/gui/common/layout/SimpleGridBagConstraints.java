package com.jbp.randommaster.gui.common.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * 
 * <code>SimpleGridBagConstraints</code> is a simple extension 
 * to the <code>java.awt.GridBagConstraints</code>. It adds
 * convenient operations to the constraint values.
 * 
 * @author plchung
 *
 */
@SuppressWarnings("serial")
public class SimpleGridBagConstraints extends GridBagConstraints {

  /**
   * Create a new <code>SimpleGridBagConstraints</code> instance.
   *
   */
  public SimpleGridBagConstraints() {
    super();
  }

  /**
   * Create a new <code>SimpleGridBagConstraints</code> instance
   * by the given parameters.
   *
   */
  public SimpleGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
    super(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady); 
  }
  
  /**
   * Reset gridx to zero, and increase gridy by the current gridheight.
   */
  public SimpleGridBagConstraints nextRow() {
    gridx=0;
    gridy+=gridheight;
    return this;
  }
  
  /**
   * Increase gridx by the current gridwidth.
   */
  public SimpleGridBagConstraints nextColumn() {
    gridx+=gridwidth;
    return this;
  }
  
  /**
   * Adjust the gridwidth and gridheight by the given delta.
   */
  public SimpleGridBagConstraints sizeChange(int widthDelta, int heightDelta) {
    this.gridwidth+=widthDelta;
    this.gridheight+=heightDelta;
    return this;
  }

  /**
   * Adjust the weight by the given delta.
   */  
  public SimpleGridBagConstraints weightChange(double weightXDelta, double weightYDelta) {
    this.weightx+=weightXDelta;
    this.weighty+=weightYDelta;
    return this;
  }
  
  /**
   * Adjust the ipadx and ipady by the given delta.
   */
  public SimpleGridBagConstraints ipadChange(int ipadxDelta, int ipadyDelta) {
    this.ipadx+=ipadxDelta;
    this.ipady+=ipadyDelta;
    return this;
  }
  
  /**
   * Change the insets to the given delta values.
   */
  public SimpleGridBagConstraints insetsChange(int topDelta, int leftDelta, int bottomDelta, int rightDelta) {
    insets.top+=topDelta;
    insets.left+=leftDelta;
    insets.bottom+=bottomDelta;
    insets.right+=rightDelta;
    return this;
  }
  
  /**
   * Change the anchor to the given new value.
   */
  public SimpleGridBagConstraints anchorChange(int anchor) {
    this.anchor=anchor;
    return this;
  }

  /**
   * Change the fill to the given new value.
   */  
  public SimpleGridBagConstraints fillChange(int fill) {
    this.fill=fill;
    return this;
  }
  

  
  
}