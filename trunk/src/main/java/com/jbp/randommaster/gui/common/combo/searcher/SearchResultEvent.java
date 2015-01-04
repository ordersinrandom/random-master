package com.jbp.randommaster.gui.common.combo.searcher;

import java.util.Collection;
import java.util.EventObject;

public class SearchResultEvent extends EventObject {
  
	private static final long serialVersionUID = 1119331986721122551L;
	
	public static final int SEARCHED_RESULT=1;
	public static final int NO_RESULT_FOUND=2; 
  
  protected String userInput;
  @SuppressWarnings("rawtypes")
  protected Collection result;
  protected int resultType;
  
  @SuppressWarnings("rawtypes")
  public SearchResultEvent(Object src, int resultType, String userInput, Collection result) {
    super(src);
    if (resultType!=SEARCHED_RESULT && resultType!=NO_RESULT_FOUND)
      throw new IllegalArgumentException("unknown result type: "+resultType);
    
    this.userInput=userInput;
    this.result=result;
    this.resultType=resultType;
  }
  
  
  public int getResultType() {
    return resultType;
  }
  

  @SuppressWarnings("rawtypes")
  public Collection getResult() {
    return result;
  }

  public String getUserInput() {
    return userInput;
  }

  
}