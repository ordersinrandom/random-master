package com.jbp.randommaster.gui.common.desktop;

public interface LayerController {

  public void layerAdded(Object key);

  public void layerRemoved(Object key);
  
  public void addLayerSelectionListener(LayerSelectionListener l);
  
  public void removeLayerSelectionListener(LayerSelectionListener l);
  
  public Object getSelectedKey();  
}