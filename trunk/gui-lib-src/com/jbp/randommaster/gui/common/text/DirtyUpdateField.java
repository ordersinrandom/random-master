package com.jbp.randommaster.gui.common.text;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class DirtyUpdateField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5963515586233738096L;
	private String original;
	private boolean dirty;

	public DirtyUpdateField() {
		super();

		setup();
	}

	public DirtyUpdateField(String text) {
		super(text);
		setup();
	}

	public DirtyUpdateField(int columns) {
		super(columns);
		setup();
	}

	public DirtyUpdateField(String text, int columns) {
		super(text, columns);
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