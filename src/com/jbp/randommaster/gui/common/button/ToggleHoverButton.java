package com.jbp.randommaster.gui.common.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ToggleHoverButton extends JButton implements Cloneable {

	private static final long serialVersionUID = -2757346731671964603L;
	
	public Color onFocusBackground = new Color(116, 194, 100);
    public Color onFocusForeground = new Color(192, 230, 192);
    public Color outOfFocusBackground = new Color(192, 230, 192);
    public Color outOfFocusForeground = new Color(116, 194, 100);

    //private String onFocusLabel;
    //private String outOfFocusLabel;

    public ToggleHoverButton(String name) {
        this(name, null, null);
    }
    public ToggleHoverButton(String name, Color outOfFocusForeground, Color outOfFocusBackground) {
        super(name);

        super.setBorder(null);
        super.setFocusPainted(false);
        if (outOfFocusBackground != null) {
            this.outOfFocusBackground = outOfFocusBackground;
            this.onFocusBackground = null;
        }
        super.setBackground(this.outOfFocusBackground);
        super.setForeground(this.outOfFocusForeground);

        if (outOfFocusForeground != null) {
            super.setForeground(outOfFocusForeground);
            this.outOfFocusForeground = outOfFocusForeground;
            this.onFocusForeground = null;
        }
        super.addMouseListener(new MyMouseAdapter());
    }
    /**
    public ToggleHoverButton(String onFocusLabel, String outOfFocusLabel) {
        super(outOfFocusLabel);

        this.onFocusLabel = onFocusLabel;
        this.outOfFocusLabel = outOfFocusLabel;

        super.setBorder(null);
        super.setBackground(outOfFocusBackground);
        super.setForeground(outOfFocusForeground);
        super.addMouseListener(new MyMouseAdapter());
    }
    */

    /**
    public void setOnFocusLabel(String onFocusLabel) {
        this.onFocusLabel = onFocusLabel;
    }

    public String getOnFocusLabel() {
        return onFocusLabel;
    }

    public void setOutOfFocusLabel(String outOfFocusLabel) {
        this.outOfFocusLabel = outOfFocusLabel;
    }

    public String getOutOfFocusLabel() {
        return outOfFocusLabel;
    }
    */

    public void setOnFocusForeground(Color onFocusForeground) {
        this.onFocusForeground = onFocusForeground;
    }
    
    public Color getOnFocusForeground() {
        return onFocusForeground;
    }

    public void setOnFocusBackground(Color onFocusBackground) {
        this.onFocusBackground = onFocusBackground;
    }

    public Color getOnFocusBackground() {
        return onFocusBackground;
    }

    public void setOutOfFocusBackground(Color outOfFocusBackground) {
        this.outOfFocusBackground = outOfFocusBackground;
    }

    public Color getOutOfFocusBackground() {
        return outOfFocusBackground;
    }

    public void setOutOfFocusForeground(Color outOfFocusForeground) {
        this.outOfFocusForeground = outOfFocusForeground;
    }

    public Color getOutOfFocusForeground() {
        return outOfFocusForeground;
    }

    public class MyMouseAdapter extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            Object obj = e.getSource();
            changeColor(obj, true);
        }
        public void mouseExited(MouseEvent e) {
            Object obj = e.getSource();
            changeColor(obj, false);
        }
        public void changeColor(Object obj, boolean on) {
            if (obj instanceof JButton) {
                JButton bu = (JButton) obj;
                if (on) {
                    //bu.setText(onFocusLabel);
                    bu.setBackground(onFocusBackground != null ? onFocusBackground : outOfFocusBackground);
                    bu.setForeground(onFocusForeground != null ? onFocusForeground : outOfFocusForeground);
                }
                else {
                    //bu.setText(outOfFocusLabel);
                    bu.setBackground(outOfFocusBackground);
                    bu.setForeground(outOfFocusForeground);
                }
            }
        }
    }

    public Object clone() {
        ToggleHoverButton tthb = new ToggleHoverButton(null, outOfFocusForeground, outOfFocusBackground);
        tthb.setOnFocusBackground(onFocusBackground);
        tthb.setOnFocusForeground(onFocusForeground);
        return tthb;
    }
}
