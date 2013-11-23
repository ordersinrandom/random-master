package com.jbp.randommaster.gui.common.table.renderer;

import java.util.LinkedHashSet;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Font;
import java.text.Format;
import javax.swing.border.Border;

import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEventListener;
import com.jbp.randommaster.gui.common.table.renderer.event.PainterStyleEvent;

/**
 * 
 * <code>PainterStyleAdapter</code> is provided as a convenient table cell
 * renderer implementation.
 * 
 * By default all implementation returns null, and an additional listener
 * interface helper implementation is provided.
 * 
 * 
 * @author plchung
 * 
 */
public class PainterStyleAdapter implements PainterStyle {

	@SuppressWarnings("rawtypes")
	protected LinkedHashSet eventListeners;

	@SuppressWarnings("rawtypes")
	protected PainterStyleAdapter() {
		eventListeners = new LinkedHashSet();
	}

	@SuppressWarnings("unchecked")
	public void addPainterStyleListener(PainterStyleEventListener l) {
		eventListeners.add(l);
	}

	public void removePainterStyleListener(PainterStyleEventListener l) {
		eventListeners.remove(l);
	}

	/**
	 * Utility method to fire the <code>PainterStyleEvent</code> object.
	 */
	@SuppressWarnings("rawtypes")
	protected void firePainterStyleEvent(PainterStyleEvent e) {
		LinkedHashSet targets = (LinkedHashSet) eventListeners.clone();
		switch (e.getType()) {
		case PainterStyleEvent.SELECTED_FOREGROUND_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.selectedForegroundStyleChanged(e);
			}
			break;
		case PainterStyleEvent.SELECTED_BACKGROUND_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.selectedBackgroundStyleChanged(e);
			}
			break;
		case PainterStyleEvent.UNSELECTED_FOREGROUND_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.unselectedForegroundStyleChanged(e);
			}
			break;
		case PainterStyleEvent.UNSELECTED_BACKGROUND_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.unselectedBackgroundStyleChanged(e);
			}
			break;
		case PainterStyleEvent.SELECTED_FONT_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.selectedFontStyleChanged(e);
			}
			break;
		case PainterStyleEvent.UNSELECTED_FONT_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.unselectedFontStyleChanged(e);
			}
			break;
		case PainterStyleEvent.FOCUS_BORDER_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.focusBorderStyleChanged(e);
			}
			break;
		case PainterStyleEvent.NO_FOCUS_BORDER_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.noFocusBorderStyleChanged(e);
			}
			break;
		case PainterStyleEvent.TEXT_FORMAT_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.textFormatStyleChanged(e);
			}
			break;
		case PainterStyleEvent.HORIZONTAL_ALIGNMENT_CHANGED:
			for (Iterator it = targets.iterator(); it.hasNext();) {
				((PainterStyleEventListener) it.next())
						.horizontalAlignmentStyleChanged(e);
			}
			break;
		}
	}

	public Color getSelectedForeground() {
		return null;
	}

	public Color getSelectedBackground() {
		return null;
	}

	public Color getUnselectedForeground() {
		return null;
	}

	public Color getUnselectedBackground() {
		return null;
	}

	public Font getSelectedFont() {
		return null;
	}

	public Font getUnselectedFont() {
		return null;
	}

	public Border getFocusBorder() {
		return null;
	}

	public Border getNoFocusBorder() {
		return null;
	}

	public Format getTextFormat() {
		return null;
	}

	public Integer getHorizontalAlignment() {
		return null;
	}
}