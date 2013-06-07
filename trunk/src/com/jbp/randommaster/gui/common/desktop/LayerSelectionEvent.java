package com.jbp.randommaster.gui.common.desktop;

import java.util.EventObject;

public class LayerSelectionEvent extends EventObject {

	private static final long serialVersionUID = -6571672945386807476L;

	private Object key;

	public LayerSelectionEvent(LayerController c, Object key) {
		super(c);
		this.key = key;
	}

	public LayerController getLayerController() {
		return (LayerController) super.getSource();
	}

	public Object getKey() {
		return key;
	}
}