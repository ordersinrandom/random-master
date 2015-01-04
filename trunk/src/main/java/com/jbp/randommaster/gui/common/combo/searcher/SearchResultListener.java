package com.jbp.randommaster.gui.common.combo.searcher;

import java.util.EventListener;

public interface SearchResultListener extends EventListener {
  
  public void searchResultReceived(SearchResultEvent e);
}