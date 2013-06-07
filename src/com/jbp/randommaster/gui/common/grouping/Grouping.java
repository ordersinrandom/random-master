package com.jbp.randommaster.gui.common.grouping;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.Action;

@SuppressWarnings("rawtypes")
public class Grouping implements Comparable {
  private Object id;
  private String name;
  private Grouping parent;
  private List child;
  
  private List listeners;
  private boolean selected;
  private Action action;

  public Grouping(Object id, String name) {
    if (id==null)
      throw new IllegalArgumentException("ID cannot be null");
    if (name==null)
      throw new IllegalArgumentException("name cannot be null");
      
    this.id=id;
    this.name=name;
    child=new LinkedList();
    
    listeners=new LinkedList();
    selected=false;
  }
  
  public Object getId() {
    return id;
  }
  
  public void setAction(Action action) {
    this.action=action;
  }
  
  public Action getAction() {
    return action;
  }
  
  public void setName(String name) {
    this.name=name;
    GroupingEvent e=new GroupingEvent(this, GroupingEvent.GROUP_RENAMED);
    this.fireGroupingEvent(e);
  }
  
  public String getName() {
    return name;
  }

  public void setParent(Grouping p) {
    parent=p;
  }
  
  public Grouping getParent() {
    return parent;
  }

  public void select() {
    if (parent!=null) {
      Iterator it=parent.getGroupings().iterator();
      while (it.hasNext()) {
        Grouping g=(Grouping) it.next();
        if (g!=this)
          g.unselect();
      }
    }
    selected=true;
    GroupingEvent e=new GroupingEvent(this, GroupingEvent.GROUP_SELECTED);
    this.fireGroupingEvent(e);
  }
  
  public boolean isSelected() {
    return selected;
  }
  
  public void unselect() {
    selected=false;
    GroupingEvent e=new GroupingEvent(this, GroupingEvent.GROUP_UNSELECTED);
    this.fireGroupingEvent(e);
  }
  
  @SuppressWarnings("unchecked")
  public Grouping addGrouping(Grouping item) {
    if (!child.contains(item)) {
      child.add(item);
      item.setParent(this);
    }
    
    return item;
  }
  
  public Collection getGroupings() {
    return child;
  }

  @SuppressWarnings("unchecked")
  public void addGroupingListener(GroupingListener l) {
    if (!listeners.contains(l))
      listeners.add(l);      
  }
  
  public void removeGroupingListener(GroupingListener l) {
    listeners.remove(l);
  }
  
  protected void fireGroupingEvent(GroupingEvent e) {
    switch (e.getType()) {
      case GroupingEvent.GROUP_RENAMED:
        for (Iterator it=listeners.iterator();it.hasNext();) {
          ((GroupingListener) it.next()).groupNameChanged(e);
        }
        break;
      case GroupingEvent.GROUP_SELECTED:
        for (Iterator it=listeners.iterator();it.hasNext();) {
          ((GroupingListener) it.next()).groupSelected(e);
        }
        break;
      case GroupingEvent.GROUP_UNSELECTED:
        // not yet supported.
        break;
      default:
        throw new IllegalStateException("unknown event type "+e.getType());
    }
    
  }
  
  
  public boolean equals(Object obj) {
    if (obj==null)
      return false;
    else if (obj instanceof Grouping) {
      Grouping g=(Grouping) obj;
      return (this.id.equals(g.id));
    }
    else return false;
  }
  
  public int hashCode() {
    return id.hashCode();
  }
  
  public int compareTo(Object obj) {
    Grouping g=(Grouping) obj;
    return java.text.Collator.getInstance().compare(this.name, g.name);
  }
  
  public String toString() {
    StringBuffer buf=new StringBuffer();
    buf.append("Group { id=");
    buf.append(id);
    buf.append(", name=");
    buf.append(name);
    buf.append(", child count=");
    buf.append(child.size());
    buf.append(" }");
    return buf.toString();
  }
}