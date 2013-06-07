package com.jbp.randommaster.gui.common.grouping.buttons;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Stephen Lee
 * @version 1.0
 */

import java.awt.*;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class TabButton extends JButton
{
    public TabButton(javax.swing.Action name)
    {
        super(name);
        setOpaque(true);
        setFocusPainted(false);
        setMargin(new Insets(2, 2, 2, 2));
        setMinimumSize(new Dimension(20, 20));
//        setPreferredSize(new Dimension(20, 20));
//      super.setBackground(new Color(222,223, 255));
    }

    public boolean isFocusTraversable()
    {
        return false;
    }

    public boolean isDefaultButton()
    {
        return false;
    }

}

