package com.jbp.randommaster.gui.common.combo.searcher;

import javax.swing.ComboBoxModel;

@SuppressWarnings("rawtypes")
public interface SearchingModel extends ComboBoxModel {
  
  /**
   * The time in milliseconds of idling before starting to search something.
   */
  public long getIdleTimeBeforeSearching();
  
  /**
   * Notified by the <code>SearchingField</code> object when the user input has just updated.
   * @param latestInput
   */
  public void userInputUpdated(String latestInput);
  
  /**
   * The searching field this model belongs to.
   * @param f Must be the owner of this model, or will cause unexpected behavior.
   */
  public void setSearchingField(SearchingField f);
  
  /**
   * Remove all searched entries.
   *
   */
  public void removeAll();
  
  /**
   * Release the resource used by this model.
   *
   */
  public void dispose();
}