package com.jbp.randommaster.gui.common.combo.searcher;

public interface DataSearcher {
  
  /**
   * Will returns immediately.
   */
  public void setLatestInput(String latestInput);

  /**
   * Determines whether the current "latestInput" is valid for start searching.
   */
  public boolean shouldStartSearching();
  
  /**
   * Search based on the current "latestInput".
   * <br/>
   * Can be blocking for long time. (i.e. no need to spawn thread)
   * <br/>
   * Note that it could be called multiple times from different thread with 
   * different latestInput settings (i.e. calling setLatestInput()). Thread synchronization
   * must be handled. 
   * <br/>
   */
  public void search();
  
  /**
   * Register a listener to get the search result.
   */
  public void addSearchResultListener(SearchResultListener l);

  /**
   * Remove a listener getting the search result.
   */
  public void removeSearchResultListener(SearchResultListener l);
}