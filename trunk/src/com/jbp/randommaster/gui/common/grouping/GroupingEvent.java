package com.jbp.randommaster.gui.common.grouping;

import java.util.EventObject;

public class GroupingEvent extends EventObject {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = -4827236829676535985L;
	
  public static final int GROUP_SELECTED=0;
  public static final int GROUP_UNSELECTED=1;
  public static final int GROUP_RENAMED=2;
  
  private int type;
  
  public GroupingEvent(Grouping src, int type) {
    super(src);
    this.type=type;
  }
  
  public Grouping getGrouping() {
    return (Grouping) super.getSource();
  }
  
  public int getType() {
    return type;
  }
  
  public String toString() {
    StringBuffer buf=new StringBuffer();
    buf.append("GroupingEvent { type=");
    buf.append(type);
    buf.append(" }");
    return buf.toString();
  }
}