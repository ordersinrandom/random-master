package com.jbp.randommaster.gui.common.table.persist;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * <code>ColumnSetting</code> defines the setting of a 
 * single JTable column.
 * 
 * @author plchung
 *
 */
@SuppressWarnings("rawtypes")
public class ColumnSetting implements Externalizable, Comparable {

  // serial ID
  static final long serialVersionUID = -5170199913090048832L;
  
  
  private Object headerValue;
  private Object identifier;
  private int columnIndex;
  private int modelIndex;
  private int width;
  private int preferredWidth;

  public ColumnSetting() {
  }
  
  public boolean equals(Object obj) {
    if (obj==null)
      return false;
    else if (obj instanceof ColumnSetting) {
      ColumnSetting cs=(ColumnSetting) obj;
      return (this.identifier.equals(cs.identifier) && this.modelIndex==cs.modelIndex);
    }
    else return false;
  }
  
  public int compareTo(Object obj) {
    ColumnSetting cs=(ColumnSetting) obj;
    if (this.columnIndex<cs.columnIndex)
      return -1;
    else if (this.columnIndex==cs.columnIndex)
      return 0;
    else return 1;
  }

  public void setColumnIndex(int columnIndex) 
  {
    this.columnIndex=columnIndex;
  }

  public int getColumnIndex() 
  {
    return columnIndex;
  }

  public void setHeaderValue(Object headerValue) 
  {
    this.headerValue=headerValue;
  }

  public Object getHeaderValue() 
  {
    return headerValue;
  }

  public void setIdentifier(Object identifier) 
  {
    this.identifier=identifier;
  }

  public Object getIdentifier() 
  {
    return identifier;
  }

  public void setModelIndex(int modelIndex) 
  {
    this.modelIndex=modelIndex;
  }

  public int getModelIndex() 
  {
    return modelIndex;
  }

  public void setPreferredWidth(int preferredWidth) 
  {
    this.preferredWidth=preferredWidth;
  }

  public int getPreferredWidth() 
  {
    return preferredWidth;
  }

  public void setWidth(int width) 
  {
    this.width=width;
  }

  public int getWidth() 
  {
    return width;
  }

  public String toString()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("ColumnSetting {");
    buf.append(" columnIndex=");
    buf.append(columnIndex);
    buf.append(", headerValue=");
    buf.append(headerValue);
    buf.append(", identifier=");
    buf.append(identifier);
    buf.append(", modelIndex=");
    buf.append(modelIndex);
    buf.append(", preferredWidth=");
    buf.append(preferredWidth);
    buf.append(", width=");
    buf.append(width);
    buf.append(" }");
    return buf.toString();
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    headerValue=in.readObject();
    identifier=in.readObject();
    columnIndex=in.readInt();
    modelIndex=in.readInt();
    width=in.readInt();
    preferredWidth=in.readInt();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(headerValue);
    out.writeObject(identifier);
    out.writeInt(columnIndex);
    out.writeInt(modelIndex);
    out.writeInt(width);
    out.writeInt(preferredWidth);
  }
}