package com.jbp.randommaster.gui.common.text;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

public class DirtyUpdateArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4901931608470409122L;
	private String original;
	private boolean dirty;

	public DirtyUpdateArea() {
		super();
		setup();
	}

	public DirtyUpdateArea(String text) {
		super(text);
		setup();
	}

	public DirtyUpdateArea(int rows, int columns) {
		super(rows, columns);
		setup();
	}

	public DirtyUpdateArea(String text, int rows, int columns) {
		super(text, rows, columns);
		setup();
	}

	public void setText(String s) {
		dirty = false;
		original = s;
		super.setText(s);
	}

	private void setup() {
		dirty = false;
		original = super.getText();

		super.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				checkDirty();
			}
		});

		super.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				checkDirty();
			}
		});
	}

	public boolean isDirty() {
		return dirty;
	}

	private void checkDirty() {
		String currentValue = getText();
		if (!currentValue.equals(original)) {
			dirty = true;

			// debug
			// System.out.println("dirty: currentValue="+currentValue);
			// System.out.println("dirty: original="+original);

		}
	}
}