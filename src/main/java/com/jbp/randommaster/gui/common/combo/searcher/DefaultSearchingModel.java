package com.jbp.randommaster.gui.common.combo.searcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jbp.randommaster.gui.common.threading.ScreenUpdateUtil;

public class DefaultSearchingModel implements SearchingModel, SearchResultListener {
  
  protected long timeBeforeStartSearching;
  protected SearchingField searchingField;
  protected DataSearcher searcher;

  protected LinkedHashSet<ListDataListener> listeners;
  
  // most simple implementation 
  @SuppressWarnings("rawtypes")
  protected LinkedList searchedData;
  
  protected Object selectedItem;
  
  //protected String latestUserInput;

  protected TriggerSearchRunnable searchTrigger;
  
  
  public DefaultSearchingModel(DataSearcher searcher) {
    this(150, searcher);
  }

  @SuppressWarnings("rawtypes")
  public DefaultSearchingModel(long timeBeforeStartSearching, DataSearcher searcher) {
    this.timeBeforeStartSearching=timeBeforeStartSearching;

    searchedData=new LinkedList();
    
    listeners=new LinkedHashSet<ListDataListener>();
    this.searcher=searcher;
    
    searcher.addSearchResultListener(this);
  }
  
  /**
   * Dispose this object.
   *
   */
  public void dispose() {
    searcher.removeSearchResultListener(this);
  }
  
  public long getIdleTimeBeforeSearching() {
    return timeBeforeStartSearching;
  }
  
  public void setSearchingField(SearchingField field) {
    this.searchingField=field;
    searchTrigger=new TriggerSearchRunnable();
    Thread t=new Thread(searchTrigger);
    t.setDaemon(true);
    t.start();
  }
  
  /**
   * Implementation of <code>SearchingModel</code>.
   */
  public void userInputUpdated(String input) {
    // ignore case
    if (input.equals(selectedItem))
      return;

    clearResults();
    
    searchTrigger.setLatestInput(input);
    
  }
  
  /**
   * Implemnetation of <code>SearchingModel</code>.
   */
  public void removeAll() {
    clearResults();
  }
  
  private void clearResults() {
    String textInField=searchingField.getTextFieldString();
    searchingField.clearResults(textInField);

    ListDataEvent e=new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, 
        0, searchedData.size());
    searchedData.clear();
    
    fireListDataEvent(e);
  }
  
  
  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public void setSelectedItem(Object anItem) {

    selectedItem=anItem;
    
    // fire content changed
    ListDataEvent e=new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
    fireListDataEvent(e);
    
  }

  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public Object getSelectedItem() {
    return selectedItem;
  }

  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public int getSize() {
    return searchedData.size();
  }

  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public Object getElementAt(int index) {
    try {
      return searchedData.get(index);
    } catch (Exception e1) {
      return "";
    }
  }

  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public void addListDataListener(ListDataListener l) { 
    listeners.add(l);
  }

  /**
   * Implementation of <code>ComboBoxModel</code>.
   */
  public void removeListDataListener(ListDataListener l) {
    listeners.remove(l);
  }
  
  /**
   * Helper to fire the <code>ListDataEvent</code> object.
   */
  protected void fireListDataEvent(ListDataEvent e) {
    @SuppressWarnings("unchecked")
	LinkedHashSet<ListDataListener> targets=(LinkedHashSet<ListDataListener>) listeners.clone();
    
    switch (e.getType()) {
      case ListDataEvent.CONTENTS_CHANGED:
        for (Iterator<ListDataListener> it=targets.iterator();it.hasNext();) {
          ListDataListener l= it.next();
          l.contentsChanged(e);
        }
        break;
      case ListDataEvent.INTERVAL_ADDED:
        for (Iterator<ListDataListener> it=targets.iterator();it.hasNext();) {
          ListDataListener l= it.next();
          l.intervalAdded(e);
        }
        break;
      case ListDataEvent.INTERVAL_REMOVED:
        for (Iterator<ListDataListener> it=targets.iterator();it.hasNext();) {
          ListDataListener l= it.next();
          l.intervalRemoved(e);
        }
        break;
    }
  }
  
  /**
   * Implementation of <code>SearchResultListener</code>.
   */
  public void searchResultReceived(final SearchResultEvent e) {
    
    // check the current user result
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          // if found something.
          if (e.getResultType()==SearchResultEvent.SEARCHED_RESULT
              || e.getResult()!=null) {
            // editable combo box.
            String textInField=searchingField.getTextFieldString();
            if (e.getUserInput()!=null && e.getUserInput().equals(textInField)) {
              addData(textInField, e.getResult());
            }
            else {
            }
          }
          else if (e.getResultType()==SearchResultEvent.NO_RESULT_FOUND) {
            searchingField.reportSearchFailed();
          }
        }
      });
  }

  /**
   * Helper to add an entry to the combo box.
   */
  @SuppressWarnings("rawtypes")
protected void addData(final String userInput, final Collection result) {
    
    
    ScreenUpdateUtil.waitFor(new Runnable() {
        @SuppressWarnings("unchecked")
		public void run() {
          int oldSize=searchedData.size();
          searchedData.addAll(result);
          if (oldSize==searchedData.size())
            return;
          
          String textInField=searchingField.getTextFieldString();
          
          ListDataEvent e=new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 
              oldSize, searchedData.size());


          fireListDataEvent(e);

          searchingField.showResults(textInField);

        }
      });
  }
  
  
  /**
   * 
   * <code>TriggerSearchRunnable</code> is the thread to trigger start searching.
   *
   */
  private class TriggerSearchRunnable implements Runnable {
    
    private String searchedInput;
    
    private String latestInput;
    private boolean running;
    

    public TriggerSearchRunnable() {
      running=true;
    }
    
    public void setLatestInput(String latest) {
      latestInput=latest;
    }
    
    @SuppressWarnings("unused")
	public void dispose() {
      running=false;
    }
    
    public void run() {
      while (running) {
        try {
          Thread.sleep(getIdleTimeBeforeSearching());
        } catch (InterruptedException e1) {
          // ignore.
        }
        if (!running)
          break;
        
        // if the current status is invalid.. just ignore and sleep more.
        if (latestInput!=null && latestInput.length()>0 && !latestInput.equals(searchedInput)) {
          searchedInput=latestInput;
  
          searcher.setLatestInput(latestInput);
          if (searcher.shouldStartSearching()) {
            // spawn a thread to search
            Thread t=new Thread(new Runnable() {
                public void run() {
                  // assume blocking for long time
                  // and it will generate SearchResultRevent and notify the listeners.
                  searcher.search();
                }
              });
            t.start();
          }
        }
        
      }
    }
  }

}