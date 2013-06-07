package com.jbp.randommaster.gui.common.grouping.buttons;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Stephen Lee
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JButton;

public class RolloverButton extends JButton
    implements MouseListener
{
  /*
    public RolloverButton(String name)
    {
        
    }*/

    /**
	 * 
	 */
	private static final long serialVersionUID = -6509718855316295156L;
	public RolloverButton(Action action)
    {
        super(action);
        setOpaque(true);
        setBackground(Color.gray);
        setForeground(Color.white);
        setMargin(new Insets(2, 2, 2, 2));
        setBorderPainted(true);
        setFocusPainted(false);
        setVerticalAlignment(TOP);
        setHorizontalAlignment(CENTER);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
        addMouseListener(this);
    }

    public boolean isDefaultButton()
    {
        return false;
    }

    public void mouseEntered(MouseEvent event)
    {
        setForeground(Color.black);
        setBackground(Color.lightGray);
        repaint();
    }

    public void mouseExited(MouseEvent event)
    {
      setBackground(Color.gray);
      setForeground(Color.white);
        repaint();
    }

    public void mouseEnter(MouseEvent event) {}
    public void mousePressed(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}

}

