package com.jbp.randommaster.gui.common.grouping.buttons;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;


public class TabButton extends JButton
{
	private static final long serialVersionUID = -2211851445007351926L;

	public TabButton(javax.swing.Action name)
    {
        super(name);
        setOpaque(true);
        setFocusPainted(false);
        setMargin(new Insets(2, 2, 2, 2));
        setMinimumSize(new Dimension(20, 20));
    }

	@Override
    public boolean isFocusable()
    {
        return false;
    }

	@Override
    public boolean isDefaultButton()
    {
        return false;
    }

}

