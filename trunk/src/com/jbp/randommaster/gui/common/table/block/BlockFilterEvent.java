package com.jbp.randommaster.gui.common.table.block;

import java.util.EventObject;

/**
 * 
 * <code>BlockFilterEvent</code> encapsulates the filter changes and the
 * BlockTableModel receives this event and determines the table rows showing or
 * hidden.
 * 
 */
public class BlockFilterEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8776500502109171614L;

	public BlockFilterEvent(BlockFilter src) {
		super(src);
	}

	public BlockFilter getFilter() {
		return (BlockFilter) getSource();
	}

}