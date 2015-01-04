package com.jbp.randommaster.gui.common.table.renderer.event;

import java.util.EventObject;

import com.jbp.randommaster.gui.common.table.renderer.PainterStyle;

/**
 * 
 * <code>PainterStyleEvent</code> is the event object sent to the listener when
 * a painter style updated.
 * 
 * @author plchung
 * 
 */
public class PainterStyleEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7649868262818837285L;
	public static final int SELECTED_FOREGROUND_CHANGED = 1;
	public static final int SELECTED_BACKGROUND_CHANGED = 2;
	public static final int UNSELECTED_FOREGROUND_CHANGED = 3;
	public static final int UNSELECTED_BACKGROUND_CHANGED = 4;
	public static final int SELECTED_FONT_CHANGED = 5;
	public static final int UNSELECTED_FONT_CHANGED = 6;
	public static final int FOCUS_BORDER_CHANGED = 7;
	public static final int NO_FOCUS_BORDER_CHANGED = 8;
	public static final int TEXT_FORMAT_CHANGED = 9;
	public static final int HORIZONTAL_ALIGNMENT_CHANGED = 10;

	protected int type;

	public PainterStyleEvent(PainterStyle src, int type) {
		super(src);
		this.type = type;
	}

	public PainterStyle getStyle() {
		return (PainterStyle) super.getSource();
	}

	public int getType() {
		return type;
	}
}