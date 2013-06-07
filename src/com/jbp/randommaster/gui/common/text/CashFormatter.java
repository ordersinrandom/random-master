package com.jbp.randommaster.gui.common.text;

import java.text.DecimalFormat;

public class CashFormatter extends DecimalFormat {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1709514866519198545L;

public CashFormatter(String currency) {
    this(currency, 2);
  }
  
  public CashFormatter(String currency, int decimalPlacesCount) {
    super();
    
    StringBuffer b=new StringBuffer();
    if (decimalPlacesCount>0) {
      b.append('.');
      for (int i=0;i<decimalPlacesCount;i++)
        b.append('0');
    }
    applyPattern(currency+" ##,###,##0"+b.toString());
  }
  
}